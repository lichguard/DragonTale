package componentsSystems;

import java.util.Iterator;

import PACKET.WorldPacket;
import componentNew.Broadcast;
import componentNew.Broadcast.BroadcastData;
import componentNew.EntityManager;
import objects.Player;

public class BroadCastSystem {

	//move to systems
	public void QueuePacket(int id, WorldPacket p, boolean self, int objectHandle) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(id, EntityManager.BroadCastID);
		
		bc.m_queue.add(new BroadcastData(p, self, objectHandle));
	}


	public void ProcessQueue(int id, int num_packets) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(id, EntityManager.BroadCastID);
		
		if (bc.m_queue.isEmpty())
			return;

		synchronized (bc.listeners) {
			while (!bc.m_queue.isEmpty()) {
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
	
}
