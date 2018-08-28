package objects;

import java.util.ArrayList;
import java.util.Random;

import game.World;
import vmaps.TileMap;

public class Coin extends WorldObject {

	// private BufferedImage[] sprites;

	public Coin(TileMap tm) {
		super(tm);

		width = 20;
		height = 30;
		cwidth = 20;
		cheight = 20;
		stopSpeed = 0.05;

		Random rand = new Random();
		dy = -3.0 * rand.nextDouble() - 1;
		dx = 3.0 * rand.nextDouble();

		if (rand.nextInt(2) == 0)
			dx *= -1.0;

		falling = true;
	}

	public void update(World world) {

		if (!isNetowrkEntity) {
			ArrayList<GameObject> entities = world.getCollisions(this);
			for (GameObject entity : entities) {
				if (entity instanceof Player) {
					world.requestObjectDespawn(handle);
					((Player) entity).addCoins(1);
				}
			}
		}
		super.update(world);
	}

}
