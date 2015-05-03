package at.antSim.graphics.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.graphicsUtils.DisplayManager;
import at.antSim.graphics.terrains.Terrain;

/**A virtual camera, pretending camera movement by moving the whole world in the opposite direction.
 * 
 * @author Flo
 *
 */
public class Camera {
	
	//Reference Point Camera
	private float distanceFromReferencePoint = 100;
	
	//determine how fast to rotate camera around x,y,z axis
	private static final float ZOOM_FACTOR = 0.1f;
	private static final float PITCH_FACTOR = 0.1f;
	private static final float YAW_FACTOR = 0.3f;
	
	private static final float MOVE_SPEED = 50; //units per second
	private static final float TURN_SPEED = 150; //degrees per second
	
	//determine how fast camera is currently moving
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	
	//camera's position and rotation
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 30; //how high or low the camera is aiming
	private float yaw; //how much left or right the camera is aiming
	private float roll; //the camera's tilt - at 180� it is upside down
	
	//reference point's position and rotation
	private Vector3f refPointPosition;
	private float rotY; //rotation of the reference point
	
	/**Creates a new reference point camera, passing the reference point's initial position.
	 * 
	 * @param refPointPosition
	 */
	public Camera(Vector3f refPointPosition) {
		this.refPointPosition = refPointPosition;
	}
	
	
	/**Moves the camera.
	 * 
	 * @param terrain - the {@link Terrain} the {@link Camera} moves on
	 */
	public void move(Terrain terrain) {
		
		//move the reference point of movement keys have been triggered
		checkKeyInputs();
		
		//takes into account the actual time passed, making movement independent from frame rate
		increaseKeyRotation(currentTurnSpeed * DisplayManager.getFrameTimeSeconds()); 
		increaseMouseRotation();
		
		/*
		 * distance is our referncePoints movement triangle's hypotenuse.
		 * The rotY angle defines the angle between the movement triangle's hypotenuse and the z-axis.
		 * The z-axis distance from our referencePoint to its previous position is the movement triangle's adjacent.
		 * The x-axis distance from our referencePoint to its previous position is the movement triangle's opposite.
		 * 
		 * Since we know that adjacent/hypotenuse = cos(rotY) and that opposite/hypotenuse = sin(rotY),
		 * we can calculate the adjacent's length as hypotenuse * cos(rotY) and the opposite's length as hypotenuse * sin(rotY).
		 * 
		 */
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(rotY)));
		float dz = (float) (distance * Math.cos(Math.toRadians(rotY)));
		increasePosition(dx, 0, dz);
		refPointPosition.y = terrain.getHeightOfTerrain(refPointPosition.x, refPointPosition.z);
		
		calculateZoom();
		calculatePitch();
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
	                
	}
	
	/**Checks if the camera movement keys have been pressed on the keyboard and sets movement variables accordingly.
	 * 
	 */
	private void checkKeyInputs(){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			this.currentSpeed = MOVE_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			this.currentSpeed = -MOVE_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
	}
	
	/**Moves the referencePoint in the world.
	 * 
	 * @param dx - how far to move the Entity on the x-Axis
	 * @param dy - how far to move the Entity on the y-Axis
	 * @param dz - how far to move the Entity on the z-Axis
	 */
	private void increasePosition(float dx, float dy, float dz) {
		refPointPosition.x += dx;
		refPointPosition.y += dy;
		refPointPosition.z += dz;
	}
	
	/**Rotates the referencePoint around the world's y-Axis when the left/right buttons are pressed.
	 * 
	 * @param dy - how far to rotate the Entity around the y-Axis
	 */
	private void increaseKeyRotation(float dy) {
		rotY += dy;
	}
	
	/**Rotates the reference Point around the world's y-Axis when the mouse is moved to the left or the right (inverted).
	 * 
	 */
	private void increaseMouseRotation() {
		
		//since camera is supposed to always stay directly behind the reference point (at 0 degree angle), need to update rotY when mouse rotate around y-axis
		if (Mouse.isButtonDown(1)) { //right mouse button pressed
			float angleChange = Mouse.getDX() * YAW_FACTOR; //calculate how far to rotate camera left and right, determined by the mouse's movement along the x-Axis
			rotY += angleChange; //update reference point rotation
		}
	}
	
	/**Calculates the position of a 3rd person camera in world space.
	 * 
	 * @param horizontalDistance - the horizontal distance between the player and the 3rd person camera
	 * @param verticalDistance - the vertical distance between the player and the 3rd person camera
	 */
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		
		/*
		 * horizontalDistance is our camera triangle's hypotenuse.
		 * The rotY angle defines the angle between the camera triangle's hypotenuse and the z-axis.
		 * The z-axis distance from our camera to its referencePoint is the camera triangle's adjacent.
		 * The x-axis distance from our camera to its referencePoint is the camera triangle's opposite.
		 * 
		 * Since we know that adjacent/hypotenuse = cos(rotY) and that opposite/hypotenuse = sin(rotY),
		 * we can calculate the adjacent's length as hypotenuse * cos(rotY) and the opposite's length as hypotenuse * sin(rotY).
		 */
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(rotY)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(rotY)));
		position.x = refPointPosition.x - offsetX;
		position.y = refPointPosition.y + verticalDistance;
		position.z = refPointPosition.z - offsetZ; 
		this.yaw = 180 - rotY; //add 180 degrees to make camera look along the negative z-axis (forwards) instead of the positive z-axis 
	}
	
	/*
	 * distanceFromReferencePoint is our camera triangle's hypotenuse.
	 * The pitch angle defines the angle between the camera triangle's hypotenuse and the horizontal axis(z).
	 * The horizontal distance from our camera to our referencePoint is the camera triangle's adjacent.
	 * The vertical distance from our camera to our referencePoint is the camera triangle's opposite.
	 * 
	 * Since we know that adjacent/hypotenuse = cos(pitch) and that opposite/hypotenuse = sin(pitch),
	 * we can calculate the adjacent's length as hypotenuse * cos(pitch) and the opposite's length as hypotenuse * sin(pitch).
	 * 
	 */		
	
	/**
	 * @return - the horizontal distance between the reference point and the camera
	 */
	private float calculateHorizontalDistance() {
		return (float) (distanceFromReferencePoint * Math.cos(Math.toRadians(pitch)));
	}
	
	/**
	 * @return - the vertical distance between the reference point and the camera
	 */
	private float calculateVerticalDistance() {
		return (float) (distanceFromReferencePoint * Math.sin(Math.toRadians(pitch)));
	}
	
	/**Zooms camera out when moving mousewheel down and in when moving mousewheel up.
	 * 
	 */
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * ZOOM_FACTOR; //calculate how for to zoom in and out, determined by mousewheel movement
		distanceFromReferencePoint -= zoomLevel; //move in when moving mousewheel up (Mouse.getDWheel() returns > 0)
	}
	
	/**Changes camera pitch -> rotates Camera downwards when moving mouse upwards and rotates Camera upwards when moving mouse downwards.
	 * 
	 */
	private void calculatePitch() {
		if (Mouse.isButtonDown(1)) { //right mouse button pressed
			float pitchChange = Mouse.getDY() * PITCH_FACTOR; //calculate how far to rotate camera up and down, determined by the mouse's movement along the y-Axis
			pitch -= pitchChange; //rotate camera downwards when moving mouse upwards the y-Axis
		}
	}
	
	/**
	 * @return - the Camera's position as Vector3f
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @return - the Camera's pitch (how high or low the camera is aiming)
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * @return - the Camera's yaw (how much left or right the camera is aiming)
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * @return - the Camera's tilt (in degrees)
	 */
	public float getRoll() {
		return roll;
	}
}
