package main;

import java.util.logging.Level;

import TileMap.TileMap;
import component.EntityManager;

import java.awt.Graphics;

public class World {
	public TileMap tm;
	private static World instance = null;

	public static World getInstance() {
		if (instance == null) {
			instance = new World();
		}
		return instance;
	}

	public void restart() {
		tm = null;
	}

	private World() {
	}

	public void start(TileMap tm) {
		this.tm = tm;
	}

	public int getEntitityCount() {
		return EntityManager.getInstance().entityCount;
	}

	public void request_spawn(String name, boolean local_player, int handle, int type, float x, float y, boolean facing,
			int network) {
		
		LOGGER.log(Level.INFO, "Requesting Entity type: " + type + " , handle: " + handle, this);
		
		try {
			EntityManager.getInstance().addEntity(handle, name, local_player, handle, type, x, y, facing, network);
		} catch (Exception e) {
			LOGGER.error("Failed to spawn a new entity", this);
			e.printStackTrace();
		}
		
	}

	public void request_despawn(int handle) {
		
		try {
			LOGGER.info("despawning despawn of handle: " + handle, this);
			EntityManager.getInstance().removeEntity(handle);
		} catch (Exception e) {
			LOGGER.error("Failed to despawn an entity", this);
			e.printStackTrace();
		}
		
	}

	public void update(long timeDelta) {
		EntityManager.getInstance().update(timeDelta);
	}

	public void draw(Graphics g) {
		EntityManager.getInstance().draw(g);
	}

	public void destroy() throws Exception {
		tm.destroy();
		EntityManager.getInstance().destroy();
	}
	
	/*
	
	public ArrayList<Entity> getCollisions(Entity entity) {
		ArrayList<Entity> returnObjects = new ArrayList<Entity>();
		for (Entity _entity : entities.values()) {
			if (entity.intersects(_entity) && _entity != entity)
				returnObjects.add(_entity);
		}
		return returnObjects;
	}
*/
	public static int getHeading(int x, int y) {
		return (int) ((90.0 - (180.0 / Math.PI) * Math.atan2((double) y, (double) x)) + 360.0) % 360;
	}
/*
	public ArrayList<Entity> getNearEntities(Entity entity, int range, int direction_start, int direction_end) {
		ArrayList<Entity> returnObjects = new ArrayList<Entity>();
		for (Entity _entity : entities.values()) {
			if (_entity == entity)
				continue;
			int dx = _entity.getx() - entity.getx();
			int dy = _entity.gety() - entity.gety();
			if (Math.abs(dx) + Math.abs(dy) <= range) {
				double angle = getHeading(dx, dy);
				if (direction_start < angle && angle < direction_end)
					returnObjects.add(_entity);
			}
		}
		return returnObjects;
	}
	*/
}
