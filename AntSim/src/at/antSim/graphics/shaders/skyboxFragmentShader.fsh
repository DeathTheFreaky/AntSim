#version 400

//FRAGMENT SHADER

//input to fragment shader is output of vertexShader
in vec3 textureCoords;

//output: color values to be potentially written to the buffers in the current framebuffers
//see: https://www.opengl.org/wiki/Fragment_Shader
out vec4 outColor; //outputs color of pixel which the shader is currently processing -> 4d because of RGBA

//uniform variables used to pass parameters from javacode which stay the same for all vertices of an object
uniform samplerCube cubeMap; //sample texture as a cube
uniform samplerCube cubeMap2; //second sample texture (for night) as a cube
uniform float blendFactor; //0: just the first texture, 1: just the second texture
uniform vec3 fogColor1; //lower section of skybox shall fade into the fog
uniform vec3 fogColor2; //lower section of skybox shall fade into the fog

const float lowerLimit = 0.0; //below: skybox has fogcolor
const float upperLimit = 30.0; //above: skybox has skybox's original color

void main(void){

	vec4 texture1 = texture(cubeMap, textureCoords); //original skybox daytime color
	vec4 texture2 = texture(cubeMap2, textureCoords); //original skybox nighttime color
	vec4 finalColor = mix(texture1, texture2, blendFactor); //mix day/night cubes according to blendFactor

	vec3 finalFogColor = mix(fogColor1, fogColor2, blendFactor); //adjust fogColor to match daytime    
	
	float factor = (textureCoords.y - lowerLimit) / (upperLimit - lowerLimit); //factor of 0: below lower limit, factor of 1: abouve upper limit
	factor = clamp(factor, 0.0, 1.0); //anything outside lower/upper range does not really affect us
	out_Color = mix(vec4(finalFogColor, 1.0), finalColor, factor); //calculate color of final pixel onscreen
}
