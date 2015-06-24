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
import at.antSim.utils.Maths;

/**For pheromones...
 * 
 * @author Flo
 *
 */
public class Pheromone extends Entity {
	
	private LinkedList<DirectionEntry> directions = new LinkedList<>();
	private int idCtr;

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

	public int getLifetime() {
		int maxLifetime = 0;
		for (DirectionEntry entry : directions) {
			if (entry.lifetime > maxLifetime) {
				maxLifetime = entry.lifetime;
			}
		}
		return maxLifetime;
	}
	
	public void increaseLifetime(Vector3f direction) {
		
		if (direction != null) {
			direction.x = direction.x * -1;
			direction.z = direction.z * -1;
			direction.y = 0;
			direction.normalize();
			direction.x = Maths.round(direction.x, 2);
			direction.z = Maths.round(direction.z, 2);
			
			Vector3f similarDirection = null;
			for (DirectionEntry entry : directions) {
				Vector3f change = new Vector3f();
				change.sub(entry.direction, direction);
				change.y = 0;
				
				if (change.length() < 0.01f) {
					entry.lifetime = Globals.maxPheromoneLifetime;
				}
			}
			
			if (similarDirection == null) {
				directions.add(new DirectionEntry(direction, Globals.maxPheromoneLifetime));
			}
			
		} else {
			System.err.println("Direciton of pheromone to be added was null ");
		}
	}

	public void decreaseLifetime() {
		LinkedList<DirectionEntry> deleteables = new LinkedList<>();
		for (DirectionEntry entry : directions) {
			entry.lifetime -= 1;
			if (entry.lifetime <= 0) {
				deleteables.add(entry);
			}
		}
		for (DirectionEntry d : deleteables) {
			directions.remove(d);
		}
		deleteables.clear();
		if (directions.size() == 0) {
			Hive.getInstance().removePheromone(this);
		}
	}

	/**
	 * @return - "strongest" direction -> visited the most
	 */
	public Vector3f getDirection() {
		Vector3f returnDir = null;
		int maxLifetime = 0;
		for (DirectionEntry entry : directions) {
			if (entry.lifetime > maxLifetime) {
				maxLifetime = entry.lifetime;
				returnDir = entry.direction;
			}
		}
		return returnDir;
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
