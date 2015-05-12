#version 400

//VERTEX SHADER

in vec2 position; //vertex's x,y positions as inputs (0 to 1, objectSpace)
in vec2 textureCoords; //vertex's u,v texture coordinates

out vec2 pass_textureCoords;

//uniform variables used to pass parameters from javacode which stay the same for all vertices of an object
uniform mat4 transformationMatrix; 

void main(void){

	/*For more info about gl_Position see: https://www.opengl.org/sdk/docs/man/html/gl_Position.xhtml*/
	//write position of current vertex into gl_Position, to be used for clipping, culling and other fixed functionality operations	
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0); 
	pass_textureCoords = textureCoords;
}
