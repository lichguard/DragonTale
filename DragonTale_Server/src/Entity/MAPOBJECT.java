package Entity;

import TileMap.TileMap;
import game.World;

public abstract class MAPOBJECT extends ENTITY {

	// constructor
	public MAPOBJECT(TileMap tm) {
		super(tm);
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

	public void update(World world) {
		super.update(world);
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		//setMapPosition();
	}

}
