#version 400 core

//VERTEX SHADER

in vec3 position; //takes x,y,z positions as input

out vec3 color; //outputs color (RGB)

void main(void) { //main is run for every vertex which undergoes vertexShader

	gl_Position = vec4(position, 1.0); //tell OpenGL where to render this vertex on the screen
	color = vec3(position.x+0.5,1.0,position.y+0.5); //create an output color (RGB values)	
}