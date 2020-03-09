package component;

import main.GameConstants;

public class Collision implements component.IComponent {
	
	public static final int componentID = EntityManager.CollisionID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
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
}
