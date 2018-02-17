package component;

import Network.Session;
import TileMap.Tile;
import entity.Entity;
import main.World;

public class PhysicsComponent implements IComponent {

	public static int id = 0;
	
	World world;
	Entity entity;
	Session session;


	public PhysicsComponent(World world, Entity entity, Session session) {
		this.world = world;
		this.entity = entity;
		this.session = session;
	}

	@Override
	public void update() {

		// if (entity.facingRight && entity.left)
		// entity.facingRight = false;
		// else if (!entity.facingRight && entity.right)
		// entity.facingRight = true;
		if (entity.left)
		{
			entity.setFacingright(false);
		}
		else if (entity.right)
		{
			entity.setFacingright(true);
		}
		else if (entity.dx > 0.0f)
		{
			entity.setFacingright(true);
		}
		else if (entity.dx < 0.0f)
		{
			entity.setFacingright(false);
		}

		
		if (entity.left) {
			entity.dx -= entity.moveSpeed;
			if (entity.dx < -entity.maxSpeed)
				entity.dx = -entity.maxSpeed;
		} else if (entity.right) {
			entity.dx += entity.moveSpeed;
			if (entity.dx > entity.maxSpeed)
				entity.dx = entity.maxSpeed;
		} else {
			if (entity.dx > 0) {
				entity.dx -= entity.stopSpeed;
				if (entity.dx < 0)
					entity.dx = 0;
			} else if (entity.dx < 0) {
				entity.dx += entity.stopSpeed;
				if (entity.dx > 0)
					entity.dx = 0;
			}
		}

		if (entity.jumping && (!entity.falling)) {
			entity.dy = entity.jumpStart;
			entity.falling = true;
		}

		if (entity.falling) {
			if (entity.dy > 0 && entity.gliding) {

				if (entity.dy < entity.maxGlideSpeed)
					entity.dy += entity.fallSpeed * 0.1;
				else
					entity.dy -= entity.fallSpeed;
			} else
				entity.dy += entity.fallSpeed;

			if (entity.dy > 0)
				entity.jumping = false;

			if (entity.dy < 0 && !entity.jumping)
				entity.dy += entity.stopJumpSpeed;

			if (entity.dy > entity.maxFallSpeed)
				entity.dy = entity.maxFallSpeed;
		} else if (entity.doublejump != 0)
			entity.doublejump = 0;

		checkTileMapCollision();
		entity.setPosition(entity.xtemp, entity.ytemp);
		entity.setMapPosition();
	}

	public void checkTileMapCollision() {
		entity.currCol = (int) entity.x / world.tm.getTileSize();
		entity.currRow = (int) entity.y / world.tm.getTileSize();

		entity.xdest = entity.x + entity.dx;
		entity.ydest = entity.y + entity.dy;

		entity.xtemp = entity.x + entity.dx;
		entity.ytemp = entity.y + entity.dy;
		calculateCorners(entity.x, entity.ydest);

		if (entity.dy < 0 && (entity.topLeft || entity.topRight)) {
			entity.dy = 0;
			entity.ytemp = entity.currRow * world.tm.getTileSize() + entity.cheight / 2;
		}

		if (entity.dy > 0 && (entity.bottomLeft || entity.bottomRight)) {
			entity.dy = 0;
			entity.falling = false;
			entity.ytemp = (entity.currRow + 1) * world.tm.getTileSize() - entity.cheight / 2;
		}

		calculateCorners(entity.xdest, entity.y);

		if (entity.dx < 0 && (entity.topLeft || entity.bottomLeft)) {
			entity.dx = 0;
			entity.xtemp = entity.currCol * world.tm.getTileSize() + entity.cwidth / 2;

		}

		if (entity.dx > 0 && (entity.topRight || entity.bottomRight)) {
			entity.dx = 0;
			entity.xtemp = entity.currCol * world.tm.getTileSize() + entity.cwidth / 2;
			// xtemp = currCol * tileSize - cwidth / 2;
		}

		if (!entity.falling) {
			calculateCorners(entity.x, entity.ydest + 1);
			entity.falling = (!entity.bottomLeft && !entity.bottomRight);
		}

	}

	public void calculateCorners(float x, float y) {
		int leftTile = (int) (x - entity.cwidth / 2) / world.tm.getTileSize();
		int rightTile = (int) (x + entity.cwidth / 2 - 1) / world.tm.getTileSize();
		int topTile = (int) (y - entity.cheight / 2) / world.tm.getTileSize();
		int bottomTile = (int) (y + entity.cheight / 2 - 1) / world.tm.getTileSize();

		if (topTile < 0 || bottomTile >= world.tm.getNumRows() || leftTile < 0 || rightTile >= world.tm.getNumCols()) {
			entity.topLeft = entity.topRight = entity.bottomLeft = entity.bottomRight = false;
			return;
		}

		int tl = world.tm.getType(topTile, leftTile);
		int tr = world.tm.getType(topTile, rightTile);
		int bl = world.tm.getType(bottomTile, leftTile);
		int br = world.tm.getType(bottomTile, rightTile);
		entity.topLeft = tl == Tile.BLOCKED;
		entity.topRight = tr == Tile.BLOCKED;
		entity.bottomLeft = bl == Tile.BLOCKED;
		entity.bottomRight = br == Tile.BLOCKED;

	}
}
