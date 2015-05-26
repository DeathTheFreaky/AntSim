#version 400 core

in vec2 textureCoords; //coordinates of a gui element's texture

out vec4 out_Color;

uniform sampler2D guiTexture; //the texture of the gui element - the default value is 0, so we do not need to pass the texture id as uniform variable for 1 texture only

//Textures are not passed to a shader. They need to be bound (one or multiple textures) to the GL state, and they stay bound until a different texture is bound.
//Then the fragment shader samples (i.e. "texture fetch") the texture. The fragment shader uses sampler2D uniforms to determine which texture unit to sample from.

void main(void){

	out_Color = texture(guiTexture,textureCoords); //calculate color of pixels onscreen by the given element's texture and its coordinates

}
