#version 400

in vec3 position; //position of the skybox in object space (-1 to 1)
out vec3 textureCoords; //3D texture coordinates (3D direction vector)

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){

	//no need to use a transformatin matrix: skybox will never be scaled, rotated or moved
	
	gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);  //apply projection and camera view to the skybox's positions
	textureCoords = position;
	
}