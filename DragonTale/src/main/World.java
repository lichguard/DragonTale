package main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import Network.Session;
import TileMap.TileMap;
import entity.Entity;
import entity.Spawner;

public class World {
	public Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	public Stack<Integer> despawn_requests = new Stack<Integer>();
	public Stack<Spawner> spawn_requests = new Stack<Spawner>();
	public TileMap tm;

	public World(TileMap tm) {
		this.tm = tm;
	}

	public int getEntitityCount() {
		return entities.size();
	}

	public int request_spawn(Session session, boolean local_player, int handle, int type, float x, float y,
			boolean facing, boolean network) {
		System.out.println("Requesting Entity type: " + type + " , handle: " + handle);
		spawn_requests.push(new Spawner(this, session, local_player, handle, type, x, y, facing, network));
		return handle;
	}

	public void request_despawn(int handle) {
		System.out.println("Requesting despawn of handle: " + handle);
		despawn_requests.push(handle);
	}

	public void update() {
		while (!despawn_requests.isEmpty())
			entities.remove(despawn_requests.pop());

		while (!spawn_requests.isEmpty())
			spawn_requests.pop().spawn();

		for (Entity entity : entities.values())
			entity.update();
	}

	public void draw(Graphics2D g) {
		for (Entity entity : entities.values())
			entity.draw(g);
	}

	public ArrayList<Entity> getCollisions(Entity entity) {
		ArrayList<Entity> returnObjects = new ArrayList<Entity>();
		for (Entity _entity : entities.values()) {
			if (entity.intersects(_entity) && _entity != entity)
				returnObjects.add(_entity);
		}
		return returnObjects;
	}

	public static int getHeading(int x, int y) {
		return (int) ((90.0 - (180.0 / Math.PI) * Math.atan2((double) y, (double) x)) + 360.0) % 360;
	}

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
}
