package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import Entity.ENTITY;

import Entity.Spawner;
import PACKET.NetworkSpawner;
import TileMap.TileMap;

public class World {
	public static final int MAX_ENTITIES = 100;

	public int handle_generator = 1;
	public Map<Integer, ENTITY> entities = new HashMap<Integer, ENTITY>();
	public Stack<Integer> entities_to_remove = new Stack<Integer>();
	public Stack<Spawner> entities_to_spawn = new Stack<Spawner>();

	public TileMap tm;

	public World(TileMap tm) {
		this.tm = tm;
	}

	public int entitity_count() {
		return entities.size();
	}

	public int spawn_entity(NetworkSpawner sp) {
		if (Entity.Spawner.entities_count <= sp.type || sp.type < 0)
			return 0;

		// System.out.println("creating entity of type: " + type);
		int handle = handle_generator++;
		entities_to_spawn.push(new Spawner(handle, sp.type, sp.x, sp.y, sp.facing, sp.network));
		return handle;
	}

	public int spawn_entity(int type, double x, double y, boolean facing, boolean network) {
		if (Entity.Spawner.entities_count <= type || type < 0)
			return 0;

		// System.out.println("creating entity of type: " + type);
		int handle = handle_generator++;
		entities_to_spawn.push(new Spawner(handle, type, x, y, facing, network));

		return handle;
	}

	public void despawn_entity(int handle) {
		entities_to_remove.push(handle);
	}

	public void update() {

		// removes entities that are destined to be removed
		while (!entities_to_spawn.isEmpty()) {
			Spawner s = entities_to_spawn.pop();
			s.create_entity(this);
		}

		while (!entities_to_remove.isEmpty()) {
			entities.remove(entities_to_remove.pop());
		}

		// update all entities in world
		for (ENTITY entity : entities.values()) {
			entity.update(this);
		}
	}

	public ArrayList<ENTITY> getCollisions(ENTITY entity) {

		ArrayList<ENTITY> returnObjects = new ArrayList<ENTITY>();
		for (ENTITY entry : entities.values()) {
			ENTITY collied_ent = entry;
			if (collied_ent == entity)
				continue;

			if (entity.intersects(collied_ent))
				returnObjects.add(collied_ent);
		}
		return returnObjects;
	}

	public static int getHeading(int x, int y) {
		return (int) ((90.0 - (180.0 / Math.PI) * Math.atan2((double) y, (double) x)) + 360.0) % 360;
	}

	public ArrayList<ENTITY> getNearEntities(int handle, int range, int direction_start, int direction_end) {

		ArrayList<ENTITY> returnObjects = new ArrayList<ENTITY>();
		if (!entities.containsKey(handle))
			return returnObjects;

		ENTITY entity = entities.get(handle);
		for (ENTITY close_entity : entities.values()) {
			if (close_entity == entity)
				continue;
			int dx = close_entity.getx() - entity.getx();
			int dy = close_entity.gety() - entity.gety();
			if (Math.abs(dx) + Math.abs(dy) <= range) {
				double angle = getHeading(dx, dy);
				
				if (direction_start <= angle && angle <= direction_end)
				{
					returnObjects.add(close_entity);
				}
			}
		}
		return returnObjects;
	}
}
