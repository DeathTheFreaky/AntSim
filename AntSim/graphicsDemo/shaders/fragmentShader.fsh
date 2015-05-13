#version 400 core

//FRAGMENT SHADER

//input to fragment shader is output from vertexShader -> 2-dim texture coords
in vec2 pass_textureCoords; //u,v positions for texture coordinates
in vec3 surfaceNormal; //direction at which surfaces "between" vertices are facing to
in vec3 toLightVector[4]; //vector from vertex to light sources in world space
in vec3 toCameraVector; //vector form vertex to camera in world space
in float visibility; //invisible = completely foggy if 0; clear if 1

out vec4 outColor; //outputs color of pixel which the shader is currently processing -> 4d because of RGBA

uniform sampler2D modelTexture; //basically represents textures we're going to use - the default value is 0, so we do not need to pass the texture id as uniform variable for 1 texture only
uniform vec3 lightColor[4]; //r,g,b values for lights
uniform vec3 attenuation[4]; //attunations to be used for the light sources -> pointed lighting - light gets weaker if distance increases
uniform float shineDamper; //how strong specular lighting appears when camera is not directly facing the reflected light
uniform float reflectivity; //how much light a surface reflects (roughness of terrain)
uniform vec3 fogColor1; //mix terrain with fog color in the distance
uniform vec3 fogColor2; //mix terrain with fog color in the distance
uniform float blendFactor; //0: just the first texture, 1: just the second texture

//Textures are not passed to a shader. They need to be bound (one or multiple textures) to the GL state, and they stay bound until a different texture is bound.
//Then the fragment shader samples (i.e. "texture fetch") the texture. The fragment shader uses sampler2D uniforms to determine which texture unit to sample from.

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal); //normalizing makes size of vector equal to 1 while direction of the vector stays the same
	vec3 unitVectorToCamera = normalize(toCameraVector); //normalize sets size of toCameraVector to 1 but keeps its direction	

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	//light calculations
	for (int i = 0; i < 4; i++) {
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].y * distance * distance); 
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDot1 = dot(unitNormal, unitLightVector); //dot product (Skalarprodukt) is used to calculate strength of light reflection on an object: 1 -> vectors are parallel, 0 -> vectors are perpenticular (highest reflection)
		float brightness = max(nDot1, 0.0); //make sure brightness never drops below 0.0
		vec3 lightDirection = -unitVectorToCamera; //direction where light is coming from is opposite direction of vector pointing towards the light
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal); //direction of light reflected from a surface
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera); //dot product is used to calculate strength of light reflection on an object
		specularFactor = max(specularFactor, 0.0); //dot product might return negative values but there are is no negative brightness
		float dampedFactor = pow(specularFactor, shineDamper); //raising specularFactor to the power of the damper value makes low dampered values even lower but does not affect strong dempered values so much
		
		//sum results of all light sources
		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attFactor;	//get final lighting color for a pixel (scalar - brightness is multiplied with each component of vector x,y,z)
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i]) / attFactor; //final specular value is calculated by multiplying dampedFactor with a light color and the reflectivity
	}
	
	totalDiffuse = max(totalDiffuse, 0.2); //diffuse never below 0.2 -> apply Ambient lighting to ensure that every part of a model gets a little bit of light

	vec4 textureColor = texture(modelTexture, pass_textureCoords); //retrieves texels from the texture bout to sampler by its texture coordinates
	if (textureColor.a < 0.5) {
		discard; //used to discard transparent parts of half-transparent textures (like fern or grass)
	}
	
	vec3 finalFogColor = mix(fogColor1, fogColor2, blendFactor); //adjust fogColor to match daytime    

	outColor = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0) ; //pixels color is created by applying diffuse lighting on the texture color and adding the reflection from specular lighting
	outColor = mix(vec4(finalFogColor, 1.0), outColor, visibility); //create mixture of skyColor and actual vertex color -> 0 visibility: completely foggy = skyColor, 1 visibility: Out_color (original object color)

}
