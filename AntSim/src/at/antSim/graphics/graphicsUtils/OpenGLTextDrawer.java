package at.antSim.graphics.graphicsUtils;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.Globals;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.textures.GuiTexture;
import at.antSim.guiWrapper.GuiTextData;

/**Used to draw bitmap font text on a 3d screen with gui Renderer.
 * 
 * @author Flo
 *
 */
public class OpenGLTextDrawer {
	
	Loader loader;
	GuiTexture texture;

	//2d texture is supposed to be a square with NUM_SIDE columns and rows of characters in a texture atlas
	final int NUM_SIDE = 16;
	final int MAX_CHARS = NUM_SIDE * NUM_SIDE;
	final int CHAR_RES = 32; //res in pixel of one char
	
	/* stores texture coordinates of characters in 2d texture atlas
	 * order corresponds the our triangle strip quad drawing order in gui renderer, since we'll use it for rendering our text: 
	 * 		top left, bottom left, top right, bottom right
	 */
	Vector2f[] textureCoords = new Vector2f[MAX_CHARS * 4];
	
	/**Creates a new {@link OpenGLTextDrawer}.
	 * 
	 */
	public OpenGLTextDrawer(Loader loader, GuiTexture texture) {
		this.loader = loader;
		this.texture = texture;
		initCoords();
	}

	/**Calculates texture coordinates for all characters of the texture atlas, according to their ASCII code.
	 * 
	 */
	private void initCoords() {
		
		float charSize = (float) 1/NUM_SIDE; //size of 1 char in screen coordinates
		
		for (int i = 0; i < MAX_CHARS; i++) {
			textureCoords[i*4] = new Vector2f((float) i % NUM_SIDE * charSize, i / NUM_SIDE * charSize);
			textureCoords[i*4 + 1] = new Vector2f((float) i % NUM_SIDE * charSize, (i / NUM_SIDE + 1) * charSize);
			textureCoords[i*4 + 2] = new Vector2f((float) (i % NUM_SIDE + 1) * charSize, i / NUM_SIDE * charSize);
			textureCoords[i*4 + 3] = new Vector2f((float) (i % NUM_SIDE + 1) * charSize, (i / NUM_SIDE + 1) * charSize);
		}		
	}
	
	/**Creates one quad per line in the text, consisting of 1 sub-quad per character, with the appropriate texture coordinates for the passed text.
	 * 
	 * @param text - text to be rendered
	 * @return - a {@link GuiTextData}
	 */
	public GuiTextData createTextQuad(String text) {
		
		LinkedList<Vector2f> quadPositions = new LinkedList<>();
		LinkedList<Vector2f> quadTextCoords = new LinkedList<>();
		
		//number of rows = number of linebreaks in text
		String[] lines = text.split("\\r?\\n");
		int rows = lines.length;
		
		//number of cols: max length line
		int cols = 0;
		for (String line : lines) {
			if (line.length() > cols) {
				cols = line.length();
			}
		}
		
		//row height = char height in object coords
		float height = (float) 2f / rows;
		float width = (float) 2f / cols;
		
		//write positions and texture coordinates of text quad
		for (int i = 0; i < lines.length; i++) {
			for (int j = 0; j < lines[i].length(); j++) {
				
				int character = lines[i].charAt(j);
				
//				System.out.println(text.charAt(i) + ": " + character);
				
//				System.out.println(" top left: " + textureCoords[character * 4].x + ", " + textureCoords[character * 4].y);
//				System.out.println(" bottom left: " + textureCoords[character * 4 + 1].x + ", " + textureCoords[character * 4 + 1].y);
//				System.out.println(" top right: " + textureCoords[character * 4 + 2].x + ", " + textureCoords[character * 4 + 2].y);
//				System.out.println(" bottom right: " + textureCoords[character * 4 + 3].x + ", " + textureCoords[character * 4 + 3].y);
				
				//calculate positions of vertices
				float top = 1 - i * height;
				float bottom = top - height;
				float left = -1 + j * width;
				float right = left + width;
				
//				System.out.println(" top: " + top);
//				System.out.println(" bottom: " + bottom);
//				System.out.println(" left: " + left);
//				System.out.println(" right: " + right);
				
				//add vertices and texture coords in the following order: T1: left top, left bottom, right top, T2: right top, left bottom, right bottom
				quadPositions.add(new Vector2f(left, top));
				quadPositions.add(new Vector2f(left, bottom));
				quadPositions.add(new Vector2f(right, top));
				quadPositions.add(new Vector2f(right, top));
				quadPositions.add(new Vector2f(left, bottom));
				quadPositions.add(new Vector2f(right, bottom));
				quadTextCoords.add(textureCoords[character * 4]);
				quadTextCoords.add(textureCoords[character * 4 + 1]);
				quadTextCoords.add(textureCoords[character * 4 + 2]);
				quadTextCoords.add(textureCoords[character * 4 + 2]);
				quadTextCoords.add(textureCoords[character * 4 + 1]);
				quadTextCoords.add(textureCoords[character * 4 + 3]);
			}
		}
		
		float[] quadPosArray = convertListToArray(quadPositions);
		float[] quadTextCoordArray = convertListToArray(quadTextCoords);
		
//		System.out.println("Positions: ");
//		for (float f: quadPosArray) {
//			System.out.println(" " + f);
//		}
//		
//		System.out.println("TextureCoords: ");
//		for (float f: quadTextCoordArray) {
//			System.out.println(" " + f);
//		}
		
		
		GuiTextData textData = new GuiTextData(loader.loadToVAO(convertListToArray(quadPositions), convertListToArray(quadTextCoords), 2), rows, cols, texture.getWidth()/Globals.fontCols, texture.getTextureId());
		
		return textData; 
	}
	
	/**Converts a list of Vector2fs to an array of floats.
	 * 
	 * @param floatList - list of Vector2fs
	 * @return - a float array
	 */
	private float[] convertListToArray(List<Vector2f> floatList) {
		
		float[] floatArray = new float[floatList.size() * 2];
		
		int idx = 0;
		for (Vector2f vector : floatList) {
			floatArray[idx] = vector.x;
			idx++;
			floatArray[idx] = vector.y;
			idx++;
		}
		
		return floatArray;
	}
}
