package textures;

import java.nio.ByteBuffer;

/**TextureData holds the decoded byte data, height and width of an image.
 * 
 * @author Flo
 *
 */
public class TextureData {
     
    private int width;
    private int height;
    private ByteBuffer buffer;
     
    /**Creates a new {@link TextureData} object.
     * 
     * @param buffer - a texture's decoded byte data
     * @param width - the width of a texture
     * @param height - the hight of a texture
     */
    public TextureData(ByteBuffer buffer, int width, int height){
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }
     
    public int getWidth(){
        return width;
    }
     
    public int getHeight(){
        return height;
    }
     
    public ByteBuffer getBuffer(){
        return buffer;
    }
 
}
