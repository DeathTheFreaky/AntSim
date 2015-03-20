#version 400

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap; //sample texture as a cube

void main(void){
    out_Color = texture(cubeMap, textureCoords); //calculate color of final pixel onscreen
}
