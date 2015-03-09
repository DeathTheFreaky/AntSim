#version 400 core

//FRAGMENT SHADER

in vec3 color; //input to fragment shader is output from vertexShader -> 3-dim color

out vec4 outColor; //outputs color of pixel which the shader is currently processing

void main(void) {

	outColor = vec4(color, 1.0); //create output color with RGB values from input color and 1.0 for Alpha channel
}
