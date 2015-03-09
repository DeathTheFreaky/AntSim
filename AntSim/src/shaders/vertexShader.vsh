#version 400 core

//VERTEX SHADER

in vec3 position; //takes x,y,z positions as input
in vec2 textureCoords; //takes u,v positions for texture coordinates

out vec2 pass_textureCoords; //outputs linearly-interpolated textureCoords

void main(void) { //main is run for every vertex which undergoes vertexShader

	gl_Position = vec4(position, 1.0); //tell OpenGL where to render this vertex on the screen
	pass_textureCoords = textureCoords; 
}