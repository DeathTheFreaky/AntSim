#version 400 core

//VERTEX SHADER

in vec3 position; //takes x,y,z positions as input
in vec2 textureCoords; //takes u,v positions for texture coordinates
in vec3 normal; //tells us which direction vertices are facing at -> for light calculation

out vec2 pass_textureCoords; //outputs linearly-interpolated textureCoords
out vec3 surfaceNormal; //direction at which surfaces "between" vertices are facing to
out vec3 toLightVector; //vector pointing towards the light source
out vec3 toCameraVector; //vector point from vertex towards the camera

//uniform variables in which values from java code can be passed in
uniform mat4 transformationMatrix; 
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

void main(void) { //main is run for every vertex which undergoes vertexShader

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0); //calculate position of vertex in the world by performing a transformation on it

	//calculate the final position of vertices onscreen by multiplying its initial position with a projection and a transformation matrix
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0); 
	pass_textureCoords = textureCoords; //pass on taxture coordinates to the fragment shader

	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz; //save xyz components of resulting 4d vector as a 3d vector in surfaceNormal
	toLightVector = lightPosition - worldPosition.xyz; //difference between lightPosition and position of vertex in the world
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz; //viewMatrix contains negative inversion of camera position - 
		// convert this to a 4d vector -> result is camera position; get vector from vertex to camera position by subtracting vertex position from camera position
}