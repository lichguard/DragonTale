package Entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import game.World;
import network.PlayerBroadcaster;
import PACKET.MovementData;
import PACKET.NetworkSpawner;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class GameObject {
	
		protected World world;
		protected TileMap tileMap;
		protected int tileSize;
		protected double xmap;
		protected double ymap;
		protected int handle;
		public boolean isNetowrkEntity = false;
		
		// position and vector
		protected double x;
		protected double y;
		protected double dx;
		protected double dy;

		public int type;
		// dimensions
		protected int width;
		protected int height;

		// collision voz
		protected int cwidth;
		protected int cheight;

		// collision

		protected int currRow;
		protected int currCol;
		protected double xdest;
		protected double ydest;
		protected double xtemp;
		protected double ytemp;
		protected boolean topLeft;
		protected boolean topRight;
		protected boolean bottomLeft;
		protected boolean bottomRight;

		// animation
		//protected Animation animation;
		protected int currentAction;
		protected int previousAction;
		protected boolean facingRight;

		// movmeent
		protected boolean left;
		protected boolean right;
		protected boolean up;
		protected boolean down;
		
		protected boolean falling;

		// movment attributes
		protected double moveSpeed;
		protected double maxSpeed;
		protected double stopSpeed;
		protected double fallSpeed;
		protected double maxFallSpeed;
		protected boolean removeEntity;
		protected int affiliation;
		protected MovementData entity_packet = new MovementData();
		protected long ping = 200;
		protected MovementData last_packet = new MovementData();
		protected MovementData new_packet = new MovementData();
		protected long interpolation_start = 0;
		public String name = "dragon";
		protected int health;
		public static final int IDLE = 0;
		public static final int WALKING = 1;
		public static final int JUMPING = 2;
		public static final int FALLING = 3;
		public static final int GLIDING = 4;
		public static final int ATTACK1 = 5;
		public static final int ATTACK2 = 6;
		public PlayerBroadcaster broadcaster =null;
		public void setMaxSpeed(double value)
		{
			this.maxSpeed = value;
		}
		
		public void setCurrentAction(int newaction)
		{
			this.currentAction = newaction;
		}
		public void setPreviousAction(int oldaction)
		{
			this.previousAction = oldaction;
		}
		public int getCurrentAction()
		{
			return this.currentAction;
		}
		public int getPreviousAction()
		{
			return  this.previousAction;
		}
		
		// constructor
		public GameObject(TileMap tm) {
			
			broadcaster = new PlayerBroadcaster(this);
			
			tileMap = tm;
			tileSize = tm.getTileSize();
			removeEntity = false;
			width = 30;
			height = 30;
			cwidth = 20;
			cheight = 20;

			moveSpeed = 0.3;
			maxSpeed = 1.6;
			stopSpeed = 0.4;
			fallSpeed = 0.15;
			maxFallSpeed = 4.0;
		
			facingRight = true;
			right = true;
		}


		public void setHandle(int handle)
		{
			this.handle = handle;
		}
		
		public boolean intersects(GameObject e) {
			return this.getRecangle().intersects(e.getRecangle());
		}

		public Rectangle getRecangle() {
			return new Rectangle((int) x - cwidth, (int) y - cheight, cwidth, cheight);
		}

		public void calculateCorners(double x, double y) {
			int leftTile = (int) (x - cwidth / 2) / tileSize;
			int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
			int topTile = (int) (y - cheight / 2) / tileSize;
			int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;

			if (topTile < 0 || bottomTile >= tileMap.getNumRows() || leftTile < 0 || rightTile >= tileMap.getNumCols()) {
				topLeft = topRight = bottomLeft = bottomRight = false;
				return;
			}

			int tl = tileMap.getType(topTile, leftTile);
			int tr = tileMap.getType(topTile, rightTile);
			int bl = tileMap.getType(bottomTile, leftTile);
			int br = tileMap.getType(bottomTile, rightTile);
			topLeft = tl == Tile.BLOCKED;
			topRight = tr == Tile.BLOCKED;
			bottomLeft = bl == Tile.BLOCKED;
			bottomRight = br == Tile.BLOCKED;

		}

		public void checkTileMapCollision() {
			currCol = (int) x / tileSize;
			currRow = (int) y / tileSize;

			xdest = x + dx;
			ydest = y + dy;

			xtemp = x + dx;
			ytemp = y + dy;
			calculateCorners(x, ydest);

			if (dy < 0 && (topLeft || topRight)) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}

			if (dy > 0 && (bottomLeft || bottomRight)) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}

			calculateCorners(xdest, y);

			if (dx < 0 && (topLeft || bottomLeft)) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
				
			}

			if (dx > 0 && (topRight || bottomRight)) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth/2;
				// xtemp = currCol * tileSize - cwidth / 2;
			}

			if (!falling) {
				calculateCorners(x, ydest + 1);
				falling = (!bottomLeft && !bottomRight);
			}


		}

		public int getx() {
			return (int) x;
		}

		public int gety() {
			return (int) y;
		}

		public double getdx() {
			return dx;
		}

		public double getdy() {
			return dy;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public int getCWidth() {
			return cwidth;
		}

		public int getCHeight() {
			return cheight;
		}

		public void setPosition(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public void setVector(double dx, double dy) {
			this.dx = dx;
			this.dy = dy;
		}


		public void setMapPosition() {
			xmap = tileMap.getx();
			ymap = tileMap.gety();
		}

		public void setLeft(boolean b) {
			left = b;
		}

		public void setRight(boolean b) {
			right = b;
		}

		public void setUp(boolean b) {
			up = b;
		}

		public void setDown(boolean b) {
			down = b;
		}

		public void update(World world)
		{	
			if (isNetowrkEntity)
			{
				double t = (System.currentTimeMillis() - interpolation_start) / ping;
				setPosition(last_packet.x * (1 - t) + new_packet.x * (t),
						last_packet.y * (1 - t) + new_packet.y * t);
			}
			
			if (gety() > tileMap.getHeight()) {
				world.despawn_entity(this.handle);
			}
			setMapPosition();
			broadcaster.ProcessQueue(0);
			
		}
		
		public int gethandle()
		{
			return handle;
		}
		
		public void updatePacket(MovementData p)
		{

			if (new_packet.timeframe < p.timeframe) {
				last_packet.clone(new_packet);
				new_packet.clone(p);
				ping = (new_packet.timeframe - last_packet.timeframe  + ping ) / 2;
				if (ping <= 0 || ping > 1000)
					ping = 500;
				interpolation_start = System.currentTimeMillis();
				facingRight = last_packet.facingRight;
				if (last_packet.currentAction != this.currentAction) {
					currentAction = last_packet.currentAction;

					if (last_packet.currentAction == 5) {
						//setattack();
						World.getInstance().spawn_entity(Spawner.FIREBALL, x, y, facingRight, false,null);
					} else if (last_packet.currentAction == 6) {
						setmeleeattack(World.getInstance());
						//scratchAttack(world);

					}
				}
			}
		}

		public void setmeleeattack(World world)
		{
			int direction_start= 0;
			int direction_end = 180;
			if (!facingRight) 
			{
				direction_start= 180;
				direction_end = 360;
			}
		 ArrayList<GameObject> entities =	world.getNearEntities(handle, 40, direction_start, direction_end);
		 for (Object entity : entities)
			{
				if (entity instanceof Enemy)
				{
					((Enemy)entity).hit(20);
					//break
				}
			}

		}
		public void say(String s) {
		
		}

		public MovementData getEntityPacket()
		{
			//if (isNetowrkEntity)
			//	return new_packet;
			
			entity_packet.handle = handle;
			entity_packet.timeframe = System.currentTimeMillis();
			entity_packet.x = getx();
			entity_packet.y = gety();
			entity_packet.currentAction = getCurrentAction();
			entity_packet.facingRight = facingRight;
			entity_packet.health = health;
			//TODO entity_packet.HEALTH
			return entity_packet;
		}
		
		public NetworkSpawner getNetowrkSpawner()
		{
			return new NetworkSpawner(handle, type, (float)x, (float)y, facingRight, true);
		}
		
		
}

