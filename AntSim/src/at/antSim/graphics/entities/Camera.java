package at.antSim.graphics.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**A Camera is used for moving around the world.
 * 
 * @author Flo
 *
 */
public class Camera {
	
	//3rd person Camera
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	

	//private Vector3f position = new Vector3f(0,10,-400); //world camera
	private Vector3f position = new Vector3f(0,0,0); //3rd person camera
	private float pitch = 20; //how high or low the camera is aiming
	private float yaw; //how much left or right the camera is aiming
	private float roll; //the camera's tilt - at 180° it is upside down
	
	private Player player;
	
	public Camera(Player player){
		this.player = player;
	};
	
	/**Moves the camera by the following key bindings:<br>
	 * <ul>
	 * 		<li>W - zoom in</li>
	 * 		<li>D - move right</li>
	 * 		<li>A - move left</li>
	 * </ul>
	 */
	/*public void move() {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) { //zoom out
			position.z -= 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) { //zoom in
			position.z += 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) { //move right
			position.x += 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) { //move left
			position.x -= 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) { //move up
			position.y += 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) { //move down
			position.y -= 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_A)) { //rotate left
			position.x -= 1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_D)) { //rotate right
			position.x -= 1f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_S)) { //rotate down
			pitch = (pitch + 2)%360;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_W)) { //rotate up
			pitch = (pitch - 2)%360;
		}
	}*/
	
	/**Moves the camera.
	 * 
	 */
	public void move() {
		
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
	                
	           /* float arg_yaw = Mouse.getDX() ;
	            //System.out.println(arg_yaw) ;
	            yaw += arg_yaw/10 ;
	            float arg_roll = Mouse.getDY() ;
	            pitch += -(arg_roll/10) ;
	            Mouse.setGrabbed(true);
	            
	                if (Keyboard.isKeyDown(Keyboard.KEY_W)) 
	                {
	                    float toZ = ((float)Math.sin( Math.toRadians(yaw+90))) ;
	                    float toX = ((float)Math.cos( Math.toRadians(yaw+90))) ;
	                    position.x -= toX;
	                    position.z -= toZ;
	                        
	                }
	                if (Keyboard.isKeyDown(Keyboard.KEY_S)) 
	                {
	                    float toZ = ((float)Math.sin( Math.toRadians(yaw+90))) ;
	                    float toX = ((float)Math.cos( Math.toRadians(yaw+90))) ;
	                    position.x += toX;
	                    position.z += toZ;
	                }

	                if (Keyboard.isKeyDown(Keyboard.KEY_D)) 
	                {
	                    float toZ = ((float)Math.sin( Math.toRadians(yaw))) ;
	                    float toX = ((float)Math.cos( Math.toRadians(yaw))) ;
	                    position.x += toX;
	                    position.z += toZ;
	                }

	                if (Keyboard.isKeyDown(Keyboard.KEY_A)) 
	                {
	                    float toZ = ((float)Math.sin( Math.toRadians(yaw))) ;
	                    float toX = ((float)Math.cos( Math.toRadians(yaw))) ;
	                    position.x -= toX;
	                    position.z -= toZ;
	                }
	                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) 
	                {
	                    position.y += 0.2f;
	                }
	                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) 
	                {
	                    position.y -= 0.2f;
	                }*/
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
	
	/**Calculates the position of a 3rd person camera in world space.
	 * 
	 * @param horizDistance - the horizontal distance between the player and the 3rd person camera
	 * @param verticDistance - the vertical distance between the player and the 3rd person camera
	 */
	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		
		//an explanation of what's going on here: http://www.youtube.com/watch?v=PoxDDZmctnU
		
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + verticDistance;
		position.z = player.getPosition().z - offsetZ; 
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	/**
	 * @return - the horizontal distance between the player and the 3rd person camera
	 */
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	/**
	 * @return - the vertical distance between the player and the 3rd person camera
	 */
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	/**Zooms 3rd person camera out when moving mousewheel down and in when moving mousewheel up.
	 * 
	 */
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	/**Changes 3rd person camera pitch -> rotate Camera up when moving mouse up rotate Camera down when moving mouse down.
	 * 
	 */
	private void calculatePitch() {
		if (Mouse.isButtonDown(1)) { //right mouse button
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	/**Changes 3rd person camera angle around the player - rotate camera left when moving mouse to the left, move camera righ twhen moving mouse to the right.
	 * 
	 */
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(1)) { //left mouse button
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
