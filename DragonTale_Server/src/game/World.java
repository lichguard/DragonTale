package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

import PACKET.NetworkSpawner;
import PACKET.WorldPacket;
import main.LOGGER;
import network.WorldSession;
import network.WorldSocket;
import objects.GameObject;
import objects.Spawner;
import vmaps.GameMap;

public class World {
	public static final int MAX_ENTITIES = 100;

	// session in world
	public Map<UUID, WorldSession> SessionMap = new HashMap<UUID, WorldSession>();
	// session waiting to be added to world
	ConcurrentLinkedDeque<WorldSession> m_sessionAddQueue = new ConcurrentLinkedDeque<WorldSession>();
	public int handle_generator = 1;
	public Map<Integer, GameObject> m_gameObjectsMap = new HashMap<Integer, GameObject>();
	public Stack<Spawner> m_objectSpawnQueue = new Stack<Spawner>();
	public Stack<Integer> m_objectRemoveQueue = new Stack<Integer>();
	private static World instance = null;
	public GameMap tm = null;
	public long lastbroadcast = 0;
	
	//find session in world
	public WorldSession FindSession(UUID id) {
		return SessionMap.get(id);
	}

	//add session to world queue
	public void AddSession(WorldSession s) {
		synchronized (m_sessionAddQueue) {
			m_sessionAddQueue.push(s);
		}
	}


	public static World getInstance() {
		if (instance == null) {
			instance = new World();
		}
		return instance;
	}

	public void shutdown() {
		LOGGER.info("Shuting down world...", this);
		for (WorldSession s : m_sessionAddQueue) {
			s.worldsocket.disconnect();
		}
		for (WorldSession s : SessionMap.values()) {
			s.worldsocket.disconnect();
		}
	}
	private World() {}

	//start the world
	public void startWorld(GameMap tm) {
		this.tm = tm;
	}

	public int getObjectCount() {
		return m_gameObjectsMap.size();
	}

	public int requestObjectSpawn(NetworkSpawner sp) {
		return requestObjectSpawn(sp.type,sp.x,sp.y,sp.facing,sp.network,null);
	}

	public int requestObjectSpawn(int type, double x, double y, boolean facing, boolean network,WorldSocket socketcallback) {
		if (objects.Spawner.entities_count <= type || type < 0)
			return -1;

		int handle = handle_generator++;
		m_objectSpawnQueue.push(new Spawner(handle, type, x, y, facing, network,socketcallback));

		return handle;
	}

	public void requestObjectDespawn(int handle) {
		m_objectRemoveQueue.push(handle);
	}


	public ArrayList<GameObject> getCollisions(GameObject entity) {

		ArrayList<GameObject> returnObjects = new ArrayList<GameObject>();
		for (GameObject entry : m_gameObjectsMap.values()) {
			GameObject collied_ent = entry;
			if (collied_ent == entity)
				continue;

			if (entity.intersects(collied_ent))
				returnObjects.add(collied_ent);
		}
		return returnObjects;
	}

	public ArrayList<GameObject> getNearEntities(int handle, int range, int direction_start, int direction_end) {

		ArrayList<GameObject> returnObjects = new ArrayList<GameObject>();
		if (!m_gameObjectsMap.containsKey(handle))
			return returnObjects;

		GameObject entity = m_gameObjectsMap.get(handle);
		for (GameObject close_entity : m_gameObjectsMap.values()) {
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

	public static int getHeading(int x, int y) {
		return (int) ((90.0 - (180.0 / Math.PI) * Math.atan2((double) y, (double) x)) + 360.0) % 360;
	}

	public void BroadcastToAll(WorldPacket packet) {
		for (WorldSession session : SessionMap.values()) {
			session.SendWorldPacket(packet);
		}
	}

	public void update() {
		
		//add sessions in queue and process / remove closed sessions
		/// - Add new sessions
		synchronized (m_sessionAddQueue) {
			for (WorldSession s : m_sessionAddQueue) {
				LOGGER.info("Adding session from m_sessionAddQueue",this);
				SessionMap.put(s.id, s);
			}
			m_sessionAddQueue.clear();
		}

		// update world session packets
		for (Iterator<WorldSession> iterator = SessionMap.values().iterator(); iterator.hasNext();) {
			WorldSession s = (WorldSession) iterator.next();
			if (!s.Update())
				iterator.remove();
			
		}

		//add objects
		while (!m_objectSpawnQueue.isEmpty()) 
			m_objectSpawnQueue.pop().create_entity();

		//remove objects
		while (!m_objectRemoveQueue.isEmpty()) {
			int handle = m_objectRemoveQueue.pop();
			m_gameObjectsMap.get(handle).broadcaster.ClearListeners();
			m_gameObjectsMap.remove(handle);
		}

	/*
		if (System.currentTimeMillis() - lastbroadcast > game.GameConstants.POSITION_UPDATE_SEND_FREQUENCY) {
			
			//update all locations
			for (GameObject entity : m_gameObjectsMap.values()) {
					entity.broadcaster.QueuePacket(new WorldPacket(WorldPacket.MOVEMENT_DATA, entity.getEntityPacket()), false, entity.gethandle());
			}
			
			//check who to spawn
			for (WorldSession s : SessionMap.values()) {
				if (s._player == null) {continue;}
				ArrayList<GameObject> entities_close = getNearEntities(s._player.gethandle(),Gameplay.WIDTH / 2 + Gameplay.HEIGHT / 2, 0, 360);

				for (GameObject near : entities_close) {
					near.broadcaster.AddListener(s._player);
				}
				
			}
			
			lastbroadcast = System.currentTimeMillis();
		}
		*/
		// update world session packets
		for (Iterator<WorldSession> iterator = SessionMap.values().iterator(); iterator.hasNext();) {
			WorldSession s = (WorldSession) iterator.next();
			if (s._player != null) {
				s._player.cell.update();
			}
		}

		// update all entities in world
		//for (GameObject entity : m_gameObjectsMap.values()) {
		//	entity.update(this);
		//}
	}

}
