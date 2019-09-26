package componentNew;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import PACKET.MovementData;
import PACKET.WorldPacket;
import network.WorldSocket;

public class Broadcast implements componentNew.IComponent {

	public long lastbroadcast = 0;
	public MovementData packet = new MovementData();

	public Broadcast(int id, WorldSocket worldSocket) {
		packet.handle = id;
		this.m_socket = worldSocket;
	}

	public WorldSocket m_socket;
	public Set<Integer> listeners = new HashSet<Integer>();
	public Deque<BroadcastData> m_queue = new ArrayDeque<BroadcastData>();

	static int num_bcaster_created = 0;
	static int num_bcaster_deleted = 0;

	public static void ClearListeners(int id) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(id, EntityManager.BroadCastID);

		synchronized (bc.listeners) {
			while (!bc.listeners.isEmpty()) {
				RemoveListenerfromID(id, bc.listeners.iterator().next());
			}
		}
	}

	public static void AddListener(int id, int target) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(id, EntityManager.BroadCastID);

		if (id == target) {
			return;
		}

		synchronized (bc.listeners) {
			bc.listeners.add(target);
			if (bc.m_socket != null) {
				bc.m_socket.SendWorldPacket(new WorldPacket(WorldPacket.SPAWN, null));
				startListening(target, id);
			}
		}
	}

	public static void RemoveListenerfromID(int id, int target) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(id, EntityManager.BroadCastID);
		Broadcast targetbc = (Broadcast) EntityManager.getInstance().getEntityComponent(target,
				EntityManager.BroadCastID);

		if (bc.m_socket != null && id == target) {
			return;
		}
		synchronized (bc.listeners) {
			if (bc.listeners.contains(target)) {
				bc.listeners.remove(target);

			}
			if (bc.listeners.contains(target)) {

				// stop that player from listening to my broadcaster
				targetbc.listeners.remove(id);
				if (bc.m_socket != null) {
					targetbc.m_socket.SendWorldPacket(new WorldPacket(WorldPacket.DESPAWN, id));
				}
				// tells the listener to stop listening to me if he is
				stoplistening(target, id);
			}
		}
	}

	private static void startListening(int id, int target) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(id, EntityManager.BroadCastID);

		synchronized (bc.listeners) {
			if (!bc.listeners.contains(target) && target != id) {
				bc.listeners.add(target);
				bc.m_socket.SendWorldPacket(new WorldPacket(WorldPacket.SPAWN, null));
			}
		}
	}

	private static void stoplistening(int id, int target) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(id, EntityManager.BroadCastID);
		Broadcast targetbc = (Broadcast) EntityManager.getInstance().getEntityComponent(target,
				EntityManager.BroadCastID);

		synchronized (bc.listeners) {
			if (bc.listeners.contains(target)) {
				bc.listeners.remove(target);
				targetbc.m_socket.SendWorldPacket(new WorldPacket(WorldPacket.DESPAWN, id));
			}
		}
	}
	
	public static void QueuePacket(int handle, WorldPacket p, boolean self) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(handle, EntityManager.BroadCastID);
		
		synchronized (bc.m_queue) {
			bc.m_queue.add(new BroadcastData(p, self, handle));
		}
	}

	public static void SendPacket(Integer handle, WorldPacket packet) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(handle, EntityManager.BroadCastID);
		if (bc.m_socket != null) {
			bc.m_socket.SendWorldPacket(packet);
		}
	}

}
