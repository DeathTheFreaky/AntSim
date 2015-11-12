#version 400 core

//FRAGMENT SHADER

//input to fragment shader is output of vertexShader
in vec2 pass_textureCoords; //u,v positions for texture coordinates
in vec3 surfaceNormal; //direction at which surfaces "between" vertices are facing to
in vec3 toLightVector[4]; //vector from vertex to light sources in world space
in vec3 toCameraVector; //vector form vertex to camera in world space
in float visibility; //invisible = completely foggy if 0; clear if 1

//output: color values to be potentially written to the buffers in the current framebuffers
//see: https://www.opengl.org/wiki/Fragment_Shader
out vec4 outColor; //outputs color of pixel which the shader is currently processing -> 4d because of RGBA

//uniform variables used to pass parameters from javacode which stay the same for all vertices of an object
uniform vec3 fogColor1; //mix terrain with fog color in the distance
uniform vec3 fogColor2; //mix terrain with fog color in the distance
uniform float blendFactor; //0: just the first texture, 1: just the second texture

//Textures are not passed to a shader. They need to be bound (one or multiple textures) to the GL state, and they stay bound until a different texture is bound.
//Then the fragment shader samples (i.e. "texture fetch") the texture. The fragment shader uses sampler2D uniforms to determine which texture unit to sample from.

//three other terrain textures
uniform sampler2D backgroundTexture; //grass texture - the default value is 0, so if we had only 1 texture, we would not need to pass the textureid as uniform variable
//other terrain textures
uniform sampler2D rTexture; 
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap; //blendMap determines how much of each of the different terrain textures (meshed together) to use on certain positions of the terrain
uniform vec3 lightColor[4]; //r,g,b values for lights
uniform vec3 attenuation[4]; //attunations to be used for the light sources -> pointed lighting - light gets weaker if distance increases
uniform float shineDamper; //how strong specular lighting appears when camera is not directly facing the reflected light
uniform float reflectivity; //how much light a surface reflects (roughness of terrain)
uniform float ambientLightIntensity; 

void main(void) {

	vec4 blendMapColor = texture(blendMap, pass_textureCoords); //how much of each terrain texture shall be rendered on this particular fragment of the terrain
	vec3 unitVectorToCamera = normalize(toCameraVector); //normalize sets size of toCameraVector to 1 but keeps its direction so it can be used for dot product calculation

	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b); //how much of backGroundTexture (grass) to render -> 100% when blendMap color is black (r = 0, g = 0, b = 0)
	vec2 tiledCoords = pass_textureCoords * 40.0; //results in texture coords bigger than 1 -> gpu will scale texture down and draw it repeatedly
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoords) * backTextureAmount; //backgroundTexture color's component (0% - 100% of original backgroundTExture color) to be drawn
	vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColor.r; //color component of the texture corresponding to red on the blendMap that shall be drawn on the terrain
	vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColor.g; //color component of the texture corresponding to green on the blendMap that shall be drawn on the terrain
	vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColor.b; //color component of the texture corresponding to blue on the blendMap that shall be drawn on the terrain
	vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor; //final terrain color = sum of all texture colors

	//normalize vectors so that their size does not affect the result of the dot product calculations used to determine the angle between two vectors -> strength of light, either reflected of diffused
	vec3 unitNormal = normalize(surfaceNormal); //normalizing makes size of vector equal to 1 while direction of the vector stays the same - need 
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	//light calculations for all light sources
	//see: http://www.opengl-tutorial.org/beginners-tutorials/tutorial-8-basic-shading/
	for (int i = 0; i < 4; i++) {

		//pointed light sources: light gets weaker if distance form vertex to light source increases
		float distance = length(toLightVector[i]);
		//Attenuation method taken from http://www.ozone3d.net/tutorials/glsl_lighting_phong_p4.php:
		//attenutation factor = constantAttenuation + LinearAttenuation + QuadraticAttenuation
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance); 
		
		//diffuse lighting
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDot1 = dot(unitNormal, unitLightVector); //dot product is used to calculate strength of light reflection on an object: 1 -> vectors are parallel (highest reflection), 0 -> vectors are perpenticular 
		float brightness = max(nDot1, 0.0); //make sure brightness never drops below 0.0

		//specular lighting
		vec3 lightDirection = -unitVectorToCamera; //direction where light is coming from is opposite direction of vector pointing towards the light
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal); //direction of light reflected from a surface
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera); //dot product is used to calculate strength of specular light reflection on an object
		specularFactor = max(specularFactor, 0.0); //dot product might return negative values (if angle > 90°) but there are is no negative brightness
		float dampedFactor = pow(specularFactor, shineDamper); //raising specularFactor to the power of the damper value makes low dampered values even lower but does not affect strong dempered values so much
		
		//sum results of all light sources - the larger the attenuation, the less brighter the light
		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attFactor;	//get final lighting color for a pixel (scalar - brightness is multiplied with each component of vector x,y,z)
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i]) / attFactor; //final specular value is calculated by multiplying dampedFactor with a light color and the reflectivity
	}
	
	//ambient lighting
	totalDiffuse = max(totalDiffuse, ambientLightIntensity); //diffuse never below ambientLightIntensity -> apply Ambient lighting to ensure that every part of a model gets a little bit of light

	vec3 finalFogColor = mix(fogColor1, fogColor2, blendFactor); //adjust fogColor to match daytime/nightime cycle   
	
	//original pixel color is created by applying diffuse lighting on the texture color and adding the reflection from specular lighting
	outColor = vec4(totalDiffuse, 1.0) * totalColor + vec4(totalSpecular, 1.0) ; 
	//apply fog: create mixture of skyColor and actual vertex color -> 0 visibility: completely foggy = skyColor, 1 visibility:  original pixel color
	outColor = mix(vec4(finalFogColor, 1.0), outColor, visibility);	// DISABLED FOG FOR DEBUG PURPOSES
}
