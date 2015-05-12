#version 400 core

//FRAGMENT SHADER

//input to fragment shader is output of vertexShader
in vec2 pass_textureCoords; //u,v positions for texture coordinates

//output: color values to be potentially written to the buffers in the current framebuffers
//see: https://www.opengl.org/wiki/Fragment_Shader
out vec4 outColor; //outputs color of pixel which the shader is currently processing -> 4d because of RGBA

//uniform variables used to pass parameters from javacode which stay the same for all vertices of an object
uniform sampler2D guiTexture; //the texture of the gui element - the default value is 0, so we do not need to pass the texture id as uniform variable for 1 texture only
uniform vec3 blendColor;
uniform float blendFactor;
uniform float transparency;

//Textures are not passed to a shader. They need to be bound (one or multiple textures) to the GL state, and they stay bound until a different texture is bound.
//Then the fragment shader samples (i.e. "texture fetch") the texture. The fragment shader uses sampler2D uniforms to determine which texture unit to sample from.

void main(void){

	vec4 textureColor = texture(guiTexture, pass_textureCoords); //calculate color of pixels onscreen by the given element's texture and its coordinates
	if (textureColor.a < 0.01) {
		discard; //used to discard transparent parts of half-transparent textures - otherwise they will appear black
	}
	outColor = mix(textureColor, vec4(blendColor, 1.0), blendFactor); //blend original texture pixel color with blendColor by blendFactor
	outColor = vec4(outColor.xyz, 1.0f - transparency); //apply transparancy to gui textures
}
