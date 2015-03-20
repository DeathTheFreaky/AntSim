#version 400 core

//VERTEX SHADER

in vec3 position; //takes a vertex's x,y,z positions as inputs (0-1, objectSpace)
in vec2 textureCoords; //takes u,v positions for texture coordinates
in vec3 normal; //tells us which direction vertices are facing at -> for light calculation

out vec2 pass_textureCoords; //outputs linearly-interpolated textureCoords
out vec3 surfaceNormal; //direction at which surfaces "between" vertices are facing to
out vec3 toLightVector[4]; //vector pointing towards the light source, there can be multiple light sources
out vec3 toCameraVector; //vector point from vertex towards the camera
out float visibility; //fog

//uniform variables in which values from java code can be passed in
uniform mat4 transformationMatrix; //transforms object's coordinates from object space to world space, resulting in world position
uniform mat4 projectionMatrix; //makes camera frustum into a perfect quad, making close objects appear bigger and further apart...
uniform mat4 viewMatrix; //contains negative inversion of camera position -> world needs to move left is camera seems to move right
uniform vec3 lightPosition[4]; //x,y,z of light sources in world space

uniform float useFakeLighting; //no booleans in GSLS, set to 0.0 to disable fakeLighting, 1.0 to enable it

uniform float numberOfRows; //number of rows in a texture atlas
uniform vec2 offset; //x and y offsets of desired texture in texture atlas

const float density = 0.0035; //fog density
const float gradient = 5.0; //fog gradient

void main(void) { //main is run for every vertex which undergoes vertexShader

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0); //calculate position of vertex in the world by performing a transformation on it (object space -> world space)
	vec4 positionRelativeToCam = viewMatrix * worldPosition; //position of vertex relative to Camera (world space -> camera space)

	//calculate the final position of vertices onscreen by multiplying its initial position with a projection and a transformation matrix
	gl_Position = projectionMatrix * positionRelativeToCam; 
	pass_textureCoords = (textureCoords/numberOfRows) + offset; //pass on texture coordinates to the fragment shader (also working with texture atlases)

	//fake lighting for half-transparent textures like grass -> enforce uniform lighting
	vec3 actualNormal = normal;
	if (useFakeLighting > 0.5) {
		actualNormal = vec3(0.0, 1.0, 0.0);
	}

	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz; //save xyz components of resulting 4d vector as a 3d vector in surfaceNormal
	for (int i = 0; i < 4; i++) {
		toLightVector[i] = lightPosition[i] - worldPosition.xyz; //difference between lightPosition and position of vertex in world space
	}
	
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz; //viewMatrix contains negative inversion of camera position - 
		// convert this to a 4d vector -> result is camera position; get vector from vertex to camera position by subtracting vertex position from camera position

	float distance = length(positionRelativeToCam.xyz); //length of relativePosition Vector indicates distance of a vertex from the camera
	visibility = exp(-pow((distance*density), gradient)); //calculate visibility of vertex
	visibility = clamp(visibility,0.0,1.0); //ensure that visibility stays between 0 and 1
}

/* all about matrices: http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/ */
/* multiplying two matrices: http://statistik.wu-wien.ac.at/~leydold/MOK/HTML/node17.html */
/* projection matrix: http://www.songho.ca/opengl/gl_projectionmatrix.html */
/* spaces: http://stackoverflow.com/questions/16225815/opengl-object-space-world-space*/
