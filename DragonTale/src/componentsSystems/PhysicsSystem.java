package componentsSystems;

import TileMap.Tile;
import component.*;
import main.World;

public class PhysicsSystem implements IComponentSystem {

	public static void update(int id, long timeDelta) {

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

		Collision collisionComponent=(Collision) EntityManager.getInstance().getEntityComponent(id, EntityManager.CollisionID);
	
		//falling
		//swimming
		//walking
		//gliding
		//

		//x axis
		if (collisionComponent.swimming) {
			velocityComponent.dx = calcNewXVelocity(movementComponent.left, movementComponent.right,
					velocityComponent.dx, movementComponent.swimSpeed, movementComponent.stopSpeed,
					movementComponent.maxSwimSpeed, timeDelta);
		} else {
			velocityComponent.dx = calcNewXVelocity(movementComponent.left, movementComponent.right,
					velocityComponent.dx, movementComponent.moveSpeed, movementComponent.stopSpeed,
					movementComponent.maxSpeed, timeDelta);
		}
		
		
		
		if (movementComponent.jumping && (!movementComponent.falling)) {
			velocityComponent.dy = movementComponent.jumpStart;
			movementComponent.falling = true;
			movementComponent.jumpstarttime = System.currentTimeMillis();
		}

		

		
		if (movementComponent.falling) {
			
			if (velocityComponent.dy > 0 && movementComponent.gliding) {

				if (velocityComponent.dy < movementComponent.maxGlideSpeed)
					velocityComponent.dy += movementComponent.fallSpeed * 0.1f * timeDelta;
				else
					velocityComponent.dy -= movementComponent.fallSpeed * timeDelta;
			} else {
				velocityComponent.dy += movementComponent.fallSpeed * timeDelta ;
			}
			
			if (velocityComponent.dy > 0)
				movementComponent.jumping = false;

			
			if (movementComponent.jumping && System.currentTimeMillis() - movementComponent.jumpstarttime < 100)
				velocityComponent.dy -= movementComponent.stopJumpSpeed * timeDelta;

			if (velocityComponent.dy > movementComponent.maxFallSpeed)
				velocityComponent.dy = movementComponent.maxFallSpeed;
			
		} else if (movementComponent.doublejump != 0)
			movementComponent.doublejump = 0;

		
		/*
		 if (movementComponent.falling && !collisionComponent.swimming) {
			
			if (velocityComponent.dy > 0 && movementComponent.gliding) {

				if (velocityComponent.dy < movementComponent.maxGlideSpeed)
					velocityComponent.dy += movementComponent.fallSpeed * 0.1f * timeDelta;
				else
					velocityComponent.dy -= movementComponent.fallSpeed * timeDelta;
			} else {
				velocityComponent.dy += movementComponent.fallSpeed * timeDelta ;
			}
			
			if (velocityComponent.dy > 0)
				movementComponent.jumping = false;

			if (velocityComponent.dy < 0 && !movementComponent.jumping)
				velocityComponent.dy += movementComponent.stopJumpSpeed * timeDelta;

			if (velocityComponent.dy > movementComponent.maxFallSpeed)
				velocityComponent.dy = movementComponent.maxFallSpeed;
			
		} else if (movementComponent.doublejump != 0)
			movementComponent.doublejump = 0;
		 */
		
		
		
		
		if (collisionComponent != null)
			checkTileMapCollision(id,positionComponent, movementComponent, velocityComponent,collisionComponent);
		
		positionComponent.setPosition(movementComponent.xtemp, movementComponent.ytemp);
		
		positionComponent.setMapPosition();
		
	}

	private static float calcNewXVelocity(Boolean minusDir, Boolean plusDir, float currentSpeed, float moveSpeed, float stopSpeed,
			float maxSpeed, long timeDelta) {

		if (minusDir) {
			currentSpeed = Math.max(currentSpeed - moveSpeed * timeDelta, -maxSpeed);
			// moving right
		} else if (plusDir) {

			currentSpeed = Math.min(currentSpeed + moveSpeed * timeDelta, maxSpeed);
		} else {
			// stopping
			if (currentSpeed > 0) {
				currentSpeed = Math.max(currentSpeed - stopSpeed * timeDelta, 0);
			} else if (currentSpeed < 0) {
				currentSpeed = Math.min(currentSpeed + stopSpeed * timeDelta, 0);
			}
		}
		return currentSpeed;
	}
	
	private static void checkTileMapCollision(int id, Position positionComponent, Movement movementComponent, Velocity velocityComponent,Collision collisionComponent) {
		
		int tileSize = World.getInstance().tm.getTileSize();
		collisionComponent.currCol = (int) positionComponent.x /tileSize;
		collisionComponent.currRow = (int) positionComponent.y /tileSize;

		collisionComponent.xdest = positionComponent.x + velocityComponent.dx;
		collisionComponent.ydest = positionComponent.y + velocityComponent.dy;

		movementComponent.xtemp = positionComponent.x + velocityComponent.dx;
		movementComponent.ytemp = positionComponent.y + velocityComponent.dy;
		calculateCorners(id,collisionComponent,  movementComponent,positionComponent.x, collisionComponent.ydest);

		if (velocityComponent.dy < 0 && (collisionComponent.topLeft || collisionComponent.topRight)) {
			velocityComponent.dy = 0;
			movementComponent.ytemp = collisionComponent.currRow *tileSize + collisionComponent.cheight / 2;
		}

		if (velocityComponent.dy > 0 && (collisionComponent.bottomLeft || collisionComponent.bottomRight)) {
			velocityComponent.dy = 0;
			movementComponent.falling = false;
			movementComponent.ytemp = (collisionComponent.currRow + 1) *tileSize - collisionComponent.cheight / 2;
		}

		calculateCorners(id,collisionComponent,  movementComponent, collisionComponent.xdest, positionComponent.y);

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
			calculateCorners(id,collisionComponent,  movementComponent,positionComponent.x, collisionComponent.ydest + 1);
			movementComponent.falling = (!collisionComponent.bottomLeft && !collisionComponent.bottomRight);
		}

	}

	private static void calculateCorners(int id,Collision collisionComponent, Movement movementComponent, float x, float y) {
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
	
		collisionComponent.swimming = (tl == Tile.WATER || tr == Tile.WATER ||  bl == Tile.WATER || bl == Tile.WATER);

	}

}
