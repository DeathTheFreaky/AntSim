#version 400

//VERTEX SHADER

in vec3 position; //vertex's x,y,z positions as inputs (0 to 1, objectSpace)

//a cubemap uses 3d direction vector for applying textures to a geometry model,
//see: http://antongerdelan.net/opengl/cubemaps.html
out vec3 textureCoords; //3D texture coordinates (3D direction vector)

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){

	//no need to use a transformatin matrix: skybox will never be scaled, rotated or moved
	
	/*For more info about gl_Position see: https://www.opengl.org/sdk/docs/man/html/gl_Position.xhtml*/
	//write position of current vertex into gl_Position, to be used for clipping, culling and other fixed functionality operations	
	gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);  //apply projection and camera view to the skybox's positions
 	//texture directions match vertices positions since its a cube	
	//see: http://antongerdelan.net/opengl/cubemaps.html
	textureCoords = position;
	
}