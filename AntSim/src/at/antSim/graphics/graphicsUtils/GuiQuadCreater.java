package at.antSim.graphics.graphicsUtils;

import org.newdawn.slick.opengl.Texture;

import at.antSim.graphics.models.RawModel;

public class GuiQuadCreater {
	
	static float[] positions = { -1, 1, -1, -1, 1, 1, 1, 1, -1, -1, 1, -1 }; //gui quad positions for images
	
	public static RawModel createGuiQuad(Texture texture, Loader loader) {
		
		float[] textureCoords = new float[12]; //gui texture coords for images
		
		textureCoords[0] = 0;
		textureCoords[1] = 0;
		textureCoords[2] = 0;
		textureCoords[3] = 1;
		textureCoords[4] = 1;
		textureCoords[5] = 0;
		textureCoords[6] = 1;
		textureCoords[7] = 0;
		textureCoords[8] = 0;
		textureCoords[9] = 1;
		textureCoords[10] = 1;
		textureCoords[11] = 1;
				
		return loader.loadToVAO(positions, textureCoords, 2, false);
	}	
}
