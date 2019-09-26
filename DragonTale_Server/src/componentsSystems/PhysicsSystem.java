package componentsSystems;


import componentNew.*;
import game.World;
import vmaps.Tile;

public class PhysicsSystem implements IComponentSystem {

	public static void update(int id) {

		Position positionComponent=(Position) EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID);
		if (positionComponent == null)
			return;
		
		Velocity velocityComponent=(Velocity) EntityManager.getInstance().getEntityComponent(id, EntityManager.VelocityID);
		if (velocityComponent == null)
			return;
		
		Movement movementComponent=(Movement) EntityManager.getInstance().getEntityComponent(id, EntityManager.MovementID);
		if (movementComponent == null)
			return;
		

		
		
		Animation animationComponent=(Animation) EntityManager.getInstance().getEntityComponent(id, EntityManager.AnimationID);
		if (animationComponent != null) {
			if (movementComponent.left) {
				animationComponent.facingRight = false;
			} else if (movementComponent.right) {
				animationComponent.facingRight = true;
			} else if (velocityComponent.dx > 0.0f) {
				animationComponent.facingRight = true;
			} else if (velocityComponent.dx < 0.0f) {
				animationComponent.facingRight = false;
			}
		}

		if (movementComponent.left) {
			velocityComponent.dx -= movementComponent.moveSpeed;
			if (velocityComponent.dx < -movementComponent.maxSpeed)
				velocityComponent.dx = -movementComponent.maxSpeed;
		} else if (movementComponent.right) {
			velocityComponent.dx += movementComponent.moveSpeed;
			if (velocityComponent.dx > movementComponent.maxSpeed)
				velocityComponent.dx = movementComponent.maxSpeed;
		} else {
			if (velocityComponent.dx > 0) {
				velocityComponent.dx -= movementComponent.stopSpeed;
				if (velocityComponent.dx < 0)
					velocityComponent.dx = 0;
			} else if (velocityComponent.dx < 0) {
				velocityComponent.dx += movementComponent.stopSpeed;
				if (velocityComponent.dx > 0)
					velocityComponent.dx = 0;
			}
		}

		if (movementComponent.jumping && (!movementComponent.falling)) {
			velocityComponent.dy = movementComponent.jumpStart;
			movementComponent.falling = true;
		}

		if (movementComponent.falling) {
			if (velocityComponent.dy > 0 && movementComponent.gliding) {

				if (velocityComponent.dy < movementComponent.maxGlideSpeed)
					velocityComponent.dy += movementComponent.fallSpeed * 0.1;
				else
					velocityComponent.dy -= movementComponent.fallSpeed;
			} else
				velocityComponent.dy += movementComponent.fallSpeed;

			if (velocityComponent.dy > 0)
				movementComponent.jumping = false;

			if (velocityComponent.dy < 0 && !movementComponent.jumping)
				velocityComponent.dy += movementComponent.stopJumpSpeed;

			if (velocityComponent.dy > movementComponent.maxFallSpeed)
				velocityComponent.dy = movementComponent.maxFallSpeed;
		} else if (movementComponent.doublejump != 0)
			movementComponent.doublejump = 0;

		Collision collisionComponent=(Collision) EntityManager.getInstance().getEntityComponent(id, EntityManager.CollisionID);
		if (collisionComponent != null)
			checkTileMapCollision(positionComponent, movementComponent, velocityComponent,collisionComponent);
		
		Position.setPosition(id,movementComponent.xtemp, movementComponent.ytemp);
		
		///positionComponent.setMapPosition();
		
	}

	private static void checkTileMapCollision(Position positionComponent, Movement movementComponent, Velocity velocityComponent,Collision collisionComponent) {
		
		int tileSize = World.getInstance().tm.getTileSize();
		collisionComponent.currCol = (int) positionComponent.x /tileSize;
		collisionComponent.currRow = (int) positionComponent.y /tileSize;

		collisionComponent.xdest = positionComponent.x + velocityComponent.dx;
		collisionComponent.ydest = positionComponent.y + velocityComponent.dy;

		movementComponent.xtemp = positionComponent.x + velocityComponent.dx;
		movementComponent.ytemp = positionComponent.y + velocityComponent.dy;
		calculateCorners(collisionComponent,  movementComponent,positionComponent.x, collisionComponent.ydest);

		if (velocityComponent.dy < 0 && (collisionComponent.topLeft || collisionComponent.topRight)) {
			velocityComponent.dy = 0;
			movementComponent.ytemp = collisionComponent.currRow *tileSize + collisionComponent.cheight / 2;
		}

		if (velocityComponent.dy > 0 && (collisionComponent.bottomLeft || collisionComponent.bottomRight)) {
			velocityComponent.dy = 0;
			movementComponent.falling = false;
			movementComponent.ytemp = (collisionComponent.currRow + 1) *tileSize - collisionComponent.cheight / 2;
		}

		calculateCorners(collisionComponent,  movementComponent, collisionComponent.xdest, positionComponent.y);

		if (velocityComponent.dx < 0 && (collisionComponent.topLeft || collisionComponent.bottomLeft)) {
			velocityComponent.dx = 0;
			movementComponent.xtemp = collisionComponent.currCol *tileSize + collisionComponent.cwidth / 2;

		}

		if (velocityComponent.dx > 0 && (collisionComponent.topRight || collisionComponent.bottomRight)) {
			velocityComponent.dx = 0;
			movementComponent.xtemp = collisionComponent.currCol *tileSize + collisionComponent.cwidth / 2;
			// xtemp = currCol * tileSize - cwidth / 2;
		}

		if (!movementComponent.falling) {
			calculateCorners(collisionComponent,  movementComponent,positionComponent.x, collisionComponent.ydest + 1);
			movementComponent.falling = (!collisionComponent.bottomLeft && !collisionComponent.bottomRight);
		}

	}

	private static void calculateCorners(Collision collisionComponent, Movement movementComponent, float x, float y) {
		int tileSize = World.getInstance().tm.getTileSize();
		int leftTile = (int) (x - collisionComponent.cwidth / 2) /tileSize;
		int rightTile = (int) (x + collisionComponent.cwidth / 2 - 1) /tileSize;
		int topTile = (int) (y - collisionComponent.cheight / 2) /tileSize;
		int bottomTile = (int) (y + collisionComponent.cheight / 2 - 1) /tileSize;

		if (topTile < 0 || bottomTile >= World.getInstance().tm.getNumRows() || leftTile < 0 || rightTile >= World.getInstance().tm.getNumCols()) {
			collisionComponent.topLeft = collisionComponent.topRight = collisionComponent.bottomLeft = collisionComponent.bottomRight = false;
			return;
		}

		int tl = World.getInstance().tm.getType(topTile, leftTile);
		int tr = World.getInstance().tm.getType(topTile, rightTile);
		int bl = World.getInstance().tm.getType(bottomTile, leftTile);
		int br = World.getInstance().tm.getType(bottomTile, rightTile);
		collisionComponent.topLeft = tl == Tile.BLOCKED;
		collisionComponent.topRight = tr == Tile.BLOCKED;
		collisionComponent.bottomLeft = bl == Tile.BLOCKED;
		collisionComponent.bottomRight = br == Tile.BLOCKED;

	}

}
