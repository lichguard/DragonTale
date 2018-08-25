package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import Entity.GameObject;
import Entity.Spawner;

import PACKET.NetworkSpawner;
import PACKET.WorldPacket;
import TileMap.TileMap;
import network.WorldSession;
import network.WorldSocket;

public class World {
	public static final int MAX_ENTITIES = 100;

	// session in world
	Map<UUID, WorldSession> SessionMap = new HashMap<UUID, WorldSession>();

	// session waiting to be added to world
	ConcurrentLinkedDeque<WorldSession> m_sessionAddQueue = new ConcurrentLinkedDeque<WorldSession>();

	public WorldSession FindSession(int id) {
		return null;
	}

	public void AddSession(WorldSession s) {
		m_sessionAddQueue.push(s);
	}

	public boolean RemoveSession(int id) {
		return false;
	}

	public int handle_generator = 1;
	public Map<Integer, GameObject> entities = new HashMap<Integer, GameObject>();
	public Stack<Integer> entities_to_remove = new Stack<Integer>();
	public Stack<Spawner> entities_to_spawn = new Stack<Spawner>();
	private static World instance = null;
	public TileMap tm = null;
	public long lastbroadcast = 0;

	public static World getInstance() {
		if (instance == null) {
			instance = new World();
		}
		return instance;
	}

	private World() {
	}

	public void start(TileMap tm) {
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
		entities_to_spawn.push(new Spawner(handle, sp.type, sp.x, sp.y, sp.facing, sp.network,null));
		return handle;
	}

	public int spawn_entity(int type, double x, double y, boolean facing, boolean network,WorldSocket socketcallback) {
		if (Entity.Spawner.entities_count <= type || type < 0)
			return 0;

		// System.out.println("creating entity of type: " + type);
		int handle = handle_generator++;
		entities_to_spawn.push(new Spawner(handle, type, x, y, facing, network,socketcallback));

		return handle;
	}

	public void despawn_entity(int handle) {
		entities_to_remove.push(handle);
	}

	public void UpdateSession() {
		/// - Add new sessions
		synchronized (m_sessionAddQueue) {
			for (WorldSession s : m_sessionAddQueue) {
				SessionMap.put(s.id, s);
				s.worldsocket.SendWorldPacket(new WorldPacket(WorldPacket.HAND_SHAKE, "accepted"));
			}
			m_sessionAddQueue.clear();
		}

		// update world session packets
		for (Iterator<WorldSession> iterator = SessionMap.values().iterator(); iterator.hasNext();) {
			WorldSession s = (WorldSession) iterator.next();
			if (!s.Update())
				iterator.remove();
		}

	}

	public ArrayList<GameObject> getCollisions(GameObject entity) {

		ArrayList<GameObject> returnObjects = new ArrayList<GameObject>();
		for (GameObject entry : entities.values()) {
			GameObject collied_ent = entry;
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

	public ArrayList<GameObject> getNearEntities(int handle, int range, int direction_start, int direction_end) {

		ArrayList<GameObject> returnObjects = new ArrayList<GameObject>();
		if (!entities.containsKey(handle))
			return returnObjects;

		GameObject entity = entities.get(handle);
		for (GameObject close_entity : entities.values()) {
			if (close_entity == entity)
				continue;
			int dx = close_entity.getx() - entity.getx();
			int dy = close_entity.gety() - entity.gety();
			if (Math.abs(dx) + Math.abs(dy) <= range) {
				double angle = getHeading(dx, dy);

				if (direction_start <= angle && angle <= direction_end) {
					returnObjects.add(close_entity);
				}
			}
		}
		return returnObjects;
	}

	public void BroadcastToAll(WorldPacket packet) {
		for (WorldSession session : SessionMap.values()) {
			session.SendWorldPacket(packet);
		}
	}

	public void update() {
		UpdateSession();

		// removes entities that are destined to be removed
		while (!entities_to_spawn.isEmpty()) {
			Spawner s = entities_to_spawn.pop();
			s.create_entity(this);
		}

		while (!entities_to_remove.isEmpty()) {
			entities.remove(entities_to_remove.pop());
		}

		// update all entities in world
		for (GameObject entity : entities.values()) {
			entity.update(this);
			entity.broadcaster.ProcessQueue(0);
		}

		if (System.currentTimeMillis() - lastbroadcast > 100) {
			for (WorldSession s : SessionMap.values()) {
				if (s._player == null) {
					continue;
				}
				ArrayList<GameObject> entities_close = getNearEntities(s._player.gethandle(),
						Gameplay.WIDTH / 2 + Gameplay.HEIGHT / 2, 0, 360);

				for (GameObject near : entities_close) {
					near.broadcaster.AddListener(s._player);
				}
				s._player.broadcaster.QueuePacket(new WorldPacket(WorldPacket.MOVEMENT_DATA, s._player.getEntityPacket()), false, s._player.gethandle());
				
			}
			
			lastbroadcast = System.currentTimeMillis();
		}

	}

}
