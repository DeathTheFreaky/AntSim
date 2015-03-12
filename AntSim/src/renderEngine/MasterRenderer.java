package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

/**MasterRenderer handles all of the rendering code in the application.
 * 
 * @author Flo
 *
 */
public class MasterRenderer {
	
	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

	/**Renders a frame to the screen.
	 * 
	 * @param lightsource
	 * @param camera - for creating a viewMatrix
	 */
	public void render(Light lightsource, Camera camera) {
		renderer.prepare();
		shader.start();
		shader.loadLight(lightsource);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear(); //entities needs to be clear every frame, otherwise the entities we build up and up every frame
	}
	
	/**Sort entity in the entities Hashmap by their correct {@link TexturedModel} for more efficient rendering.
	 * 
	 * @param entity - the {@link Entity} to be sorted in
	 */
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null) {
			batch.add(entity);
		}
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	/**Call cleanUp in MasterRenderer before closing the game to free resources.
	 * 
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
}
