package at.antSim;

import at.antSim.GTPMapper.GTPMapper;
import at.antSim.GTPMapper.GTPObject;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.objectsKI.Ant;
import at.antSim.objectsKI.Enemy;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EnvironmentObject;
import at.antSim.objectsKI.Food;
import at.antSim.objectsKI.Pheronome;
import at.antSim.objectsPhysic.PhysicsFactorys.PhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;

import com.bulletphysics.linearmath.Transform;

public interface EntityBuilder {
	
	
//
//	Entity entity;
//	PhysicsObject physicsObject = null;
//	GraphicsEntity graphicsEntity = new GraphicsEntity(ModelLoader.texturedModels.get(modelId), textureIndex, scale);
//	GTPObject gtpObject = GTPMapper.getObject(graphicsEntity, scale, graphicsEntity.getModel().getPrimitiveType());
//	
//	PhysicsObjectFactory factory;
//	
//	if (mass < 0) {
//		mass = graphicsEntity.getModel().getMass();
//	} else if (mass == 0) { //do all objects need mass? if no, delete this
//		mass = ModelLoader.massDummie;
//	}
//	
//	switch(factoryType) {
//	case STATIC:
//		factory = staticFactory;
//		physicsObject = staticFactory.createPrimitive(gtpObject, mass, new Transform(Maths.createTransformationMatrix(position, rx, ry, rz)));
//		break;
//	case DYNAMIC:
//		physicsObject = dynamicFactory.createPrimitive(gtpObject, mass, new Transform(Maths.createTransformationMatrix(position, rx, ry, rz)));
//		break;
//	}
//	
//	switch (graphicsEntity.getModel().getObjectType()) {
//	case ANT:
//		return new Ant(graphicsEntity, physicsObject);
//	case ENEMY:
//		return new Enemy(graphicsEntity, physicsObject);
//	case ENVIRONMENT:
//		return new EnvironmentObject(graphicsEntity, physicsObject);
//	case FOOD:
//		return new Food(graphicsEntity, physicsObject);
//	case PHEROMONE:
//		return new Pheronome(graphicsEntity, physicsObject);
//	}
}
