package objects;


public class Coin extends WorldObject {
/*
	// private BufferedImage[] sprites;

	public Coin() {
		super();

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

	public void update() {

		if (isNetowrkEntity == 1) {
			ArrayList<GameObject> entities = World.getInstance().getCollisions(this);
			for (GameObject entity : entities) {
				if (entity instanceof Player) {
					Despawn();
					//World.getInstance().requestObjectDespawn(handle);
					((Player) entity).addCoins(1);
				}
			}
		}
		super.update();
	}
*/
}
