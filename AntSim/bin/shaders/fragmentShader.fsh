#version 400 core

//FRAGMENT SHADER

in vec2 pass_textureCoords; //input to fragment shader is output from vertexShader -> 2-dim texture coords

out vec4 outColor; //outputs color of pixel which the shader is currently processing

uniform sampler2D textureSampler; //basically represents textures we're going to use

void main(void) {

	outColor = texture(textureSampler, pass_textureCoords); //returns color of the pixel on the texture at given coordinates 
}
