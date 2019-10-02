package componentNew;

import main.GameConstants;
import main.World;

public class Position implements componentNew.IComponent {
	public float x;
	public float y;
	public float xmap;
	public float ymap;
	
	public Position(float x, float y) {
		setPosition(x,y);
		setMapPosition();
	}
	
	public void setPosition(float x, float y) {

		this.x = x;
		this.y = y;
	}
	
	public void setMapPosition() {
		xmap = World.getInstance().tm.getx();
		ymap =  World.getInstance().tm.gety();
	}
	
	public boolean notonScreen() {

		return Math.abs(x - (-xmap + GameConstants.WIDTH / 2)) > GameConstants.WIDTH / 0.75f
				|| Math.abs(y - (-ymap + GameConstants.HEIGHT / 2)) > GameConstants.HEIGHT / 0.75f;
	}

}
