package objects;

import game.World;
import vmaps.GameMap;

public class Enemy extends Unit {

	protected int static_damage;

	public Enemy(GameMap tm) {
		super(tm);
		width = 30;
		height = 30;
		cwidth = 30;
		cheight = 20;

		moveSpeed = 0.3;
		maxSpeed = 0.3;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		facingRight = true;
		right = true;
		health = maxHealth = 100;
		static_damage = 1;

	}

	public int getDamager() {
		return static_damage;
	}

	public void hit(int damage) {
		health -= damage;
		if (health < 0)
			health = 0;
	}

	public void update(World world) {
		super.update(world);
		if (health <= 0) {
			//world.spawn_entity(Spawner.EXPLOSION, getx(), gety(), facingRight, false);
			int x = (int) (Math.random() * 6.0);
			for (int i = 0; i < 1 + x; i++)
				world.requestObjectSpawn(Spawner.COIN, getx(), gety(), facingRight, false,null);

			world.requestObjectDespawn(this.handle);
		}

		if (gety() > gameMap.getHeight()) {
			world.requestObjectDespawn(this.handle);
		}

		// if hits a wall go pother direciton
		if (right && (dx == 0 || !bottomRight)) {
			right = false;
			left = true;
			facingRight = false;
		} else if (left && (dx == 0 || !bottomLeft)) {
			right = true;
			left = false;
			facingRight = true;
		}

	}

}
