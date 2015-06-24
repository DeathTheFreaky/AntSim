package at.antSim.objectsKI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.print.attribute.HashAttributeSet;
import javax.vecmath.Vector3f;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;

/**For pheromones...
 * 
 * @author Flo
 *
 */
public class Pheromone extends Entity {
	
	private HashMap<ReadOnlyPhysicsObject, Integer> sources = new HashMap<>();
	Vector3f previousTargetPosition;

	public Pheromone(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.PHEROMONE);
		Entity.pheromones.add(this);
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
		if (dynamicPhysicsObject.getType().equals("ant")) {
//			System.out.println("an ant ran into a pheromone");
		}
	}

	@Override
	public void react(GhostPhysicsObject ghostPhysicsObject) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void react(TerrainPhysicsObject terrainPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteSpecific() {
		if (deleteAllowed) {
			Entity.pheromones.remove(this);
		}
	}
	
	public void increaseLifetime(ReadOnlyPhysicsObject po) {
		sources.put(po, Globals.maxPheromoneLifetime);
	}

	public void decreaseLifetime() {
		LinkedList<ReadOnlyPhysicsObject> deleteables = new LinkedList<>();
		for (Entry<ReadOnlyPhysicsObject, Integer> entry : sources.entrySet()) {
			entry.setValue(entry.getValue() - 1);
			if (entry.getValue() <= 0) {
				deleteables.add(entry.getKey());
			}
		}
		for (ReadOnlyPhysicsObject d : deleteables) {
			sources.remove(d);
		}
		deleteables.clear();
		if (sources.size() == 0) {
			Hive.getInstance().removePheromone(this);
		}
	}

	/**
	 * @param - position: position of an ant 
	 * @return - "strongest" direction -> visited the most
	 */
	public Vector3f getDirection(Vector3f position) {
		Vector3f returnDirection = null;
		Vector3f targetPos = null;
		int maxLifetime = 0;
		for (Entry<ReadOnlyPhysicsObject, Integer> entry : sources.entrySet()) {
			if (entry.getValue() > maxLifetime) {
				maxLifetime = entry.getValue();
				if (entry.getKey() != null) {
					targetPos = entry.getKey().getPosition();
				} 
			}
		}
		if (targetPos != null) {
			previousTargetPosition = targetPos;
		} 
		returnDirection = new Vector3f(previousTargetPosition.x - position.x, 0, previousTargetPosition.z - position.z);
		return returnDirection;
	}
	
	class DirectionEntry {
		
		Vector3f direction;
		int lifetime;
		
		public DirectionEntry(Vector3f direction, int lifetime) {
			this.direction = direction;
			this.lifetime = lifetime;
		}
	}
}
