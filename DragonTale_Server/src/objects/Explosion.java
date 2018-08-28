package objects;




import game.World;
import vmaps.GameMap;

public class Explosion extends WorldObject {


	//private int width;
	//private int height;
	
	//private Animation animation;
	//private BufferedImage[] sprites;

	public Explosion(GameMap tm)
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

	
