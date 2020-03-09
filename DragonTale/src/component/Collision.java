package component;

import main.GameConstants;

public class Collision implements component.IComponent {
	public int cwidth = 30 * GameConstants.ENTITYSCALE;
	public int cheight = 30 * GameConstants.ENTITYSCALE;
	public int currRow = 0;
	public int currCol = 0;
	public float xdest = 0;
	public float ydest = 0;

	public boolean topLeft = false;
	public boolean topRight = false;
	public boolean bottomLeft = false;
	public boolean bottomRight = false;
	
	public boolean swimming = false;
	/*
	public static void setSwim(int id, boolean val) {

		Collision collisionComponent = (Collision) EntityManager.getInstance().getEntityComponent(id,EntityManager.CollisionID);

		//Velocity velocityComponent = (Velocity) EntityManager.getInstance().getEntityComponent(id,EntityManager.VelocityID);
		//if (velocityComponent != null && val && !collisionComponent.swimming) {
		//	velocityComponent.dy = velocityComponent.dy * 0.2f;
		//}

		collisionComponent.swimming = val;

	}
	*/
}
