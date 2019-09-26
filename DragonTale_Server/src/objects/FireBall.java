package objects;

/*
import java.util.ArrayList;

import  game.World;
*/
public class FireBall extends WorldObject{
/*
	//private boolean hit;
	private int damage;
	//private BufferedImage[] sprites;
	//private BufferedImage[] hitSprites;

	public FireBall(boolean right,double x, double y,int damage) {
		super();
		
		this.x = x;
		this.y = y;
		moveSpeed = 3.8;
		this.damage = damage;
		facingRight = right;
		dx = right ? moveSpeed : -moveSpeed;

		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		
		
	
	}




	public void update() {
		
		ArrayList<GameObject> entities = World.getInstance().getCollisions(this);
		//check for collision with an enemy
		for (GameObject entity : entities)
		{
			if (entity instanceof Enemy)
			{
				((Enemy)entity).hit(damage);
				//break
			}
		}

		//check for collision with tile
		if (dx == 0) {
			Despawn();
			//World.getInstance().requestObjectDespawn(this.handle); //setHit();
		}
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		//setMapPosition();
		
	}
*/

}
