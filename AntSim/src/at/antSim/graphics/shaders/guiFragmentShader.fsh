#version 400 core

//FRAGMENT SHADER

//input to fragment shader is output of vertexShader
in vec2 textureCoords; //u,v positions for texture coordinates

//output: color values to be potentially written to the buffers in the current framebuffers
//see: https://www.opengl.org/wiki/Fragment_Shader
out vec4 outColor; //outputs color of pixel which the shader is currently processing -> 4d because of RGBA

//uniform variables used to pass parameters from javacode which stay the same for all vertices of an object
uniform sampler2D guiTexture; //the texture of the gui element - the default value is 0, so we do not need to pass the texture id as uniform variable for 1 texture only

//Textures are not passed to a shader. They need to be bound (one or multiple textures) to the GL state, and they stay bound until a different texture is bound.
//Then the fragment shader samples (i.e. "texture fetch") the texture. The fragment shader uses sampler2D uniforms to determine which texture unit to sample from.

void main(void){

	outColor = texture(guiTexture,textureCoords); //calculate color of pixels onscreen by the given element's texture and its coordinates
}
