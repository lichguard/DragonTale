package Entity;


import java.util.ArrayList;




import TileMap.TileMap;
import  game.World;
public class FireBall extends WorldObject{

	//private boolean hit;
	private int damage;
	//private BufferedImage[] sprites;
	//private BufferedImage[] hitSprites;

	public FireBall(TileMap tm, boolean right,double x, double y,int damage) {
		super(tm);
		
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




	public void update(World world) {
		
		ArrayList<GameObject> entities = world.getCollisions(this);
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
		if (dx == 0)
			world.despawn_entity(this.handle); //setHit();
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		setMapPosition();
		
	}


}
