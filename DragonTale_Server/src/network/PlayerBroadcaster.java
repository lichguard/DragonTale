package network;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import PACKET.WorldPacket;
import main.LOGGER;
import objects.GameObject;
import objects.Player;

public class PlayerBroadcaster {

	class BroadcastData {
		WorldPacket packet;
		boolean sendToSelf;
		int objectHandle;

		BroadcastData(WorldPacket p, boolean self, int handle) {
			this.packet = p;
			this.sendToSelf = self;
			this.objectHandle = handle;
		}
	};

	public WorldSocket m_socket;
	GameObject object;
	Map<Integer, Player> listeners = new HashMap<Integer, Player>();
	Deque<BroadcastData> m_queue = new ArrayDeque<BroadcastData>();

	static int num_bcaster_created = 0;
	static int num_bcaster_deleted = 0;
	public PlayerBroadcaster(GameObject object) {
		this.object = object;
	}
	
	public void setSocket(WorldSocket m_socket) {
		this.m_socket = m_socket;
	}
	
	public void ClearListeners() {
		synchronized (listeners) {
		while (!listeners.isEmpty()) {
			RemoveListener(listeners.values().iterator().next());
		}
		}
	}

	public void AddListener(Player p) {
		//if (m_socket == null)
		//	return;
		if (m_socket != null && m_socket.m_session._player == p) {
			return;
		}
		
		synchronized (listeners ) {
			if (!listeners.containsKey(p.gethandle())) {
			listeners.put(p.gethandle(), p);
			LOGGER.debug("NEW spawn sent!", this);
			p.broadcaster.SendPacket(new WorldPacket(WorldPacket.SPAWN, object.getNetowrkSpawner()));
			if (m_socket != null)
			p.broadcaster.startListening(m_socket.m_session._player);
			}
		}

	}

	public void RemoveListener(Player p) {
		
		if (m_socket != null && m_socket.m_session._player == p) {
			return;
		}
		synchronized (listeners) {
			if (listeners.containsKey(p.gethandle())) {
				
				//stop that player from listening to my broadcaster
				listeners.remove(p.gethandle());
				p.broadcaster.SendPacket(new WorldPacket(WorldPacket.DESPAWN, object.gethandle()));
				
				//tells the listener to stop listening to me if he is
				if (m_socket != null)
				p.broadcaster.stoplistening(m_socket.m_session._player);
			}
		}
	}
	
	private void startListening(Player p) {
		synchronized (listeners ) {
			if (!listeners.containsKey(p.gethandle())  && m_socket.m_session._player != p) {
			listeners.put(p.gethandle(), p);
			LOGGER.debug("NEW spawn sent!", this);
			p.broadcaster.SendPacket(new WorldPacket(WorldPacket.SPAWN, object.getNetowrkSpawner()));
			}
		}
	}
	
	private void stoplistening(Player p) {
		synchronized (listeners) {
			if (listeners.containsKey(p.gethandle())) {
				listeners.remove(p.gethandle());
				p.broadcaster.SendPacket(new WorldPacket(WorldPacket.DESPAWN, object.gethandle()));
			}
		}
	}

	public void QueuePacket(WorldPacket p, boolean self, int objectHandle) {
		m_queue.add(new BroadcastData(p, self, objectHandle));
	}

	public void ProcessQueue(int num_packets) {
		if (m_queue.isEmpty())
			return;

		synchronized (listeners) {
			while (!m_queue.isEmpty()) {
				BroadcastData data = m_queue.pop();
				if (data.sendToSelf && data.objectHandle != getselfHandle())
					SendPacket(data.packet);

				for (Iterator<Player> iterator = listeners.values().iterator(); iterator.hasNext();) {
					Player p = (Player) iterator.next();
					if(!p.broadcaster.SendPacket(data.packet))
						iterator.remove();
					
				}

			}
		}
	}

	public boolean SendPacket(WorldPacket pct) {
		if (m_socket != null)
			return m_socket.SendWorldPacket(pct);
		
		return false;
	}
	public int getselfHandle() {
		if (m_socket != null)
			return this.m_socket.m_session._player.gethandle();
		
		return -1;
	}
}
