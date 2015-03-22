#version 400 core

//FRAGMENT SHADER

in vec2 pass_textureCoords; //input to fragment shader is output from vertexShader -> 2-dim texture coords
in vec3 surfaceNormal;
in vec3 toLightVector[4]; //vector from vertex to light sources in world space
in vec3 toCameraVector;
in float visibility;

out vec4 outColor; //outputs color of pixel which the shader is currently processing -> 4d because of RGBA

uniform sampler2D backgroundTexture; //grass texture - the default value is 0, so we do not need to pass the texture id as uniform variable for 1 texture only
uniform vec3 fogColor1; //mix terrain with fog color in the distance
uniform vec3 fogColor2; //mix terrain with fog color in the distance
uniform float blendFactor; //0: just the first texture, 1: just the second texture

//Textures are not passed to a shader. They need to be bound (one or multiple textures) to the GL state, and they stay bound until a different texture is bound.
//Then the fragment shader samples (i.e. "texture fetch") the texture. The fragment shader uses sampler2D uniforms to determine which texture unit to sample from.


//three other terrain textures
uniform sampler2D rTexture; 
uniform sampler2D gTexture;
uniform sampler2D bTexture;

uniform sampler2D blendMap; //blendMap determines how much of each of the different terrain textures (meshed together) to use on certain positions of the terrain

uniform vec3 lightColor[4]; //r,g,b values for lights
uniform vec3 attenuation[4]; //attunations to be used for the light sources -> pointed lighting - light gets weaker if distance increases
uniform float shineDamper;
uniform float reflectivity;

void main(void) {

	vec4 blendMapColor = texture(blendMap, pass_textureCoords); //how much of each texture shall be rendered on this particular fragment of the terrain
	vec3 unitVectorToCamera = normalize(toCameraVector); //normalize sets size of toCameraVector to 1 but keeps its direction

	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b); //how much of backGroundTexture to render -> 100% when blendMap color is black (r = 0, g = 0, b = 0)
	vec2 tiledCoords = pass_textureCoords * 40.0; //results in values bigger than 1 -> gpu will scale texture down and draw it repeatedly
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoords) * backTextureAmount; //actual backgroundTexture's color (0% - 100% of original backgroundTExture color) 
	vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColor.r; //how much of the texture corresponding to red on the blendMap shall be drawn on the terrain
	vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColor.g; //how much of the texture corresponding to green on the blendMap shall be drawn on the terrain
	vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColor.b; //how much of the texture corresponding to blue on the blendMap shall be drawn on the terrain
	vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor; //final terrain color = mix of all texture colors

	vec3 unitNormal = normalize(surfaceNormal); //normalizing makes size of vector equal to 1 while direction of the vector stays the same
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	//light calculations
	for (int i = 0; i < 4; i++) {
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].y * distance * distance); 
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDot1 = dot(unitNormal, unitLightVector); //dot product is used to calculate strength of light reflection on an object: 1 -> vectors are parallel, 0 -> vectors are perpenticular (highest reflection)
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
	
	vec3 finalFogColor = mix(fogColor1, fogColor2, blendFactor); //adjust fogColor to match daytime    
	
	outColor = vec4(totalDiffuse, 1.0) * totalColor + vec4(totalSpecular, 1.0) ; //returns color of the pixel on the texture at given coordinates 
		//by multiplying the total texture color with the light color and adding the specularLighting value
	outColor = mix(vec4(finalFogColor, 1.0), outColor, visibility); //create mixture of skyColor and actual vertex color -> 0 visibility: completely foggy = skyColor, 1 visibility: Out_color (original object color)
}
