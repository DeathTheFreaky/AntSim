#version 400

in vec2 position; //get vertex positions from vao

out vec2 textureCoords;

uniform mat4 transformationMatrix; 

void main(void){

	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0); //render gui element at given positions
	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0); //calculate texture coordinates to send to the fragment shader (pos: -1 to 1, texture coord: 0 to 1)
}
