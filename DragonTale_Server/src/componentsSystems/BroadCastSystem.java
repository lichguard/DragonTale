package componentsSystems;

import java.util.Iterator;

import componentNew.Broadcast;
import componentNew.BroadcastData;
import componentNew.EntityManager;

public class BroadCastSystem {

	
	public void ProcessQueue(int handle, int num_packets) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(handle, EntityManager.BroadCastID);
		
		if (bc.m_queue.isEmpty())
			return;

		synchronized (bc.m_queue) {
			while (!bc.m_queue.isEmpty()) {
				BroadcastData data = bc.m_queue.pop();
				if (data.sendToSelf && data.handle == handle)
					Broadcast.SendPacket(handle, data.packet);

				for (Iterator<Integer> iterator = bc.listeners.iterator(); iterator.hasNext();) {
					Broadcast.SendPacket((Integer)iterator.next(),data.packet);
				}

			}
		}
	}
	
}
