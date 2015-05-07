#version 400

//VERTEX SHADER

in vec2 position; //vertex's x,y positions as inputs (0 to 1, objectSpace)

out vec2 textureCoords;

//uniform variables used to pass parameters from javacode which stay the same for all vertices of an object
uniform mat4 transformationMatrix; 

void main(void){

	/*For more info about gl_Position see: https://www.opengl.org/sdk/docs/man/html/gl_Position.xhtml*/
	//write position of current vertex into gl_Position, to be used for clipping, culling and other fixed functionality operations	
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0); 
	//texture Coordinates match the quad's positions, but quad's positions are given from -1 to 1 and texture coordinates from 0 to 1, so we need to adapt them accordingly
	//attenttion: texture coordinates use a different coordinate system them geometry coordinates, starting with 0 at the top and 1 at the bottom, where geometry coordinates
	// start with 1 at the top and -1 at the bottom, so we need to "invert" our scaled y-coordinates acoordingly
	textureCoords = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
}
