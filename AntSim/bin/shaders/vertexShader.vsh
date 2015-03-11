#version 400 core

//VERTEX SHADER

in vec3 position; //takes x,y,z positions as input
in vec2 textureCoords; //takes u,v positions for texture coordinates

out vec2 pass_textureCoords; //outputs linearly-interpolated textureCoords

//uniform variables in which values from java code can be passed in
uniform mat4 transformationMatrix; 
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) { //main is run for every vertex which undergoes vertexShader

	//calculate the final position of vertices onscreen by multiplying its initial position with a projection and a transformation matrix
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0); 
	pass_textureCoords = textureCoords; //pass on taxture coordinates to the fragment shader
}