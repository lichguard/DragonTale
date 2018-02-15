package Entity;




import TileMap.TileMap;
import game.World;

public class Explosion extends MAPOBJECT {


	//private int width;
	//private int height;
	
	//private Animation animation;
	//private BufferedImage[] sprites;

	public Explosion(TileMap tm)
	{
		super(tm);
		width = 30;
		height = 30;
		
	}

	
	public void update(World world)
	{
		setMapPosition();
		
		//if (animation.hasPlayedOnce())
		//	world.despawn_entity(this.handle);
	}
}

	
