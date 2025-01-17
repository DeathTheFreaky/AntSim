#version 400 core

//VERTEX SHADER

in vec3 position; //vertex's x,y,z positions as inputs (0 to 1, objectSpace)
in vec2 textureCoords; //takes u,v positions for texture coordinates
in vec3 normal; //tells us which direction vertices are facing at -> for light calculation

out vec2 pass_textureCoords;
out vec3 surfaceNormal; //direction at which surfaces "between" vertices are facing to
out vec3 toLightVector[4]; //vector pointing towards the light source, there can be multiple light sources
out vec3 toCameraVector; //vector point from vertex towards the camera for specular lighting calculation//vector point from vertex towards the camera
out float visibility; //fog

//uniform variables used to pass parameters from javacode which stay the same for all vertices of an object
uniform mat4 transformationMatrix; //transforms object's coordinates from object space to world space, resulting in world coordinates
uniform mat4 viewMatrix; //position objects in world space relative to camera - negative inversion of camera position -> world needs to move left if camera seems to move right
uniform mat4 projectionMatrix; //projects a 3D camera frustum on a 2D plane (near plane)
uniform vec3 lightPosition[4]; //x,y,z of light sources in world space

//use fog to grey out objects which are further away from the camera
const float density = 0.0035; //fog density
const float gradient = 5.0; //fog gradient

void main(void) { //main is run for every vertex which undergoes vertexShader

	/* all about matrices: http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/ */
	/* multiplying two matrices: http://statistik.wu-wien.ac.at/~leydold/MOK/HTML/node17.html */
	/* projection matrix: http://www.songho.ca/opengl/gl_projectionmatrix.html */
	/* spaces: http://stackoverflow.com/questions/16225815/opengl-object-space-world-space */
	/* fog: http://www.mbsoftworks.sk/index.php?page=tutorials&series=1&tutorial=15 */
	/* normals and lighting: http://www.opengl-tutorial.org/beginners-tutorials/tutorial-8-basic-shading/ */

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0); //calculate position of vertex in the world by performing a transformation on it
	vec4 positionRelativeToCam = viewMatrix * worldPosition; //position of vertex relative to Camera (world space -> camera space)		

	/*For more info about gl_Position see: https://www.opengl.org/sdk/docs/man/html/gl_Position.xhtml*/
	//write position of current vertex into gl_Position, to be used for clipping, culling and other fixed functionality operations	
	gl_Position = projectionMatrix * positionRelativeToCam; 
	pass_textureCoords = textureCoords; //pass on texture coordinates to the fragment shader; multiply by 40 to make tiled textures

	//in case we rotate our object, the normals need to be rotated as well, while they don't change for translations
	//we use only uniform scales so we would still need to divide our surFaceNormal by the uniform scale, but we do that by normalizing the normal in the fragment shader
	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz; //save xyz components of resulting 4d vector as a 3d vector in surfaceNormal
	for (int i = 0; i < 4; i++) {
		toLightVector[i] = lightPosition[i] - worldPosition.xyz; //difference between lightPosition and position of vertex in world space
	}
	
	//get vector from vertex to camera position by subtracting vertex position in world space from camera position
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz; 
	
	//calculate vertex' visibility when fog is applied
	float distance = length(positionRelativeToCam.xyz); //length of relativePositon vector indicates distance of a vertex from the camera
	visibility = exp(-pow((distance*density), gradient)); //calculate visibility of vertex in fog
	visibility = clamp(visibility,0.0,1.0); //ensure that visibility stays between 0 and 1
}