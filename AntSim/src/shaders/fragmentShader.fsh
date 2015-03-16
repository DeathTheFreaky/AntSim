#version 400 core

//FRAGMENT SHADER

//input to fragment shader is output from vertexShader -> 2-dim texture coords
in vec2 pass_textureCoords; 
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 outColor; //outputs color of pixel which the shader is currently processing -> 4d because of RGBA

uniform sampler2D modelTexture; //basically represents textures we're going to use
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal); //normalizing makes size of vector equal to 1 while direction of the vector stays the same
	vec3 unitLightVector = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLightVector); //dot product is used to calculate strength of light reflection on an object: 1 -> vectors are parallel, 0 -> vectors are perpenticular
	float brightness = max(nDot1, 0.2); //make sure brightness never drops below 0.2 -> apply Ambient lighting to ensure that every part of a model gets a little bit of light
	vec3 diffuse = brightness * lightColor;	//get final lighting color for a pixel 

	vec3 unitVectorToCamera = normalize(toCameraVector); //normalize sets size of toCameraVector to 1 but keeps its direction
	vec3 lightDirection = -unitVectorToCamera; //direction where light is coming from is opposite direction of vector pointing towards the light
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal); //direction of light reflected from a surface

	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera); //dot product is used to calculate strength of light reflection on an object
	specularFactor = max(specularFactor, 0.0); //dot product might return negative values but there are is no negative brightness
	float dampedFactor = pow(specularFactor, shineDamper); //raising specularFactor to the power of the damper value makes low dampered values even lower but does not affect strong dempered values so much
	vec3 finalSpecular = dampedFactor * lightColor * reflectivity; //final specular value is calculated by multiplying dampedFactor with a light color and the reflectivity

	vec4 textureColor = texture(modelTexture, pass_textureCoords);
	if (textureColor.a < 0.5) {
		discard; //used to discard transparent parts of half-transparent textures (like fern or grass)
	}

	outColor = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0) ; //returns color of the pixel on the texture at given coordinates 
		//by multiplying the texture with the light color and adding the specularLighting value
	outColor = mix(vec4(skyColor, 1.0), outColor, visibility); //create mixture of skyColor and actual vertex color -> 0 visibility: completely foggy = skyColor, 1 visibility: Out_color (original object color)

}
