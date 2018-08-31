package objects;

import game.World;
import vmaps.GameMap;

public abstract class WorldObject extends GameObject {

	// constructor
	public WorldObject() {
		super();
	}

	private void getNextPosition() {

		
		if (dx > 0) {
			dx -= stopSpeed;
			if (dx < 0)
				dx = 0;
		} else if (dx < 0) {
			dx += stopSpeed;
			if (dx > 0)
				dx = 0;
		}

		if (falling) {

			dy += fallSpeed;

			if (dy > maxFallSpeed)
				dy = maxFallSpeed;

		}

	}

	public void update() {
		super.update();
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		//setMapPosition();
	}

}
