package componentsSystems;

import java.util.Iterator;

import PACKET.WorldPacket;
import component.Animation;
import component.Broadcast;
import component.BroadcastData;
import component.EntityManager;
import component.Health;
import component.Position;

public class BroadCastSystem {

	public static final int UPDATEINTEVAL = 0;
	
	public static void ProcessQueue(int handle) {
		Broadcast bc = (Broadcast) EntityManager.getInstance().getEntityComponent(handle, EntityManager.BroadCastID);
		
		if (bc == null || bc.m_queue.isEmpty())
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


	public static void brodcastEntityStateToNetowrk(int id) {

		Broadcast broadcastComponent = (Broadcast) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.BroadCastID);
		if (broadcastComponent == null)
			return;

		if (System.currentTimeMillis() - broadcastComponent.packet.timeframe > UPDATEINTEVAL) {

			Animation animationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id,
					EntityManager.AnimationID);
			if (animationComponent != null) {
				broadcastComponent.packet.facingRight = animationComponent.facingRight;
				broadcastComponent.packet.currentAction = animationComponent.currentPlayingAction;
			}

			Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
					EntityManager.PositionID);
			if (positionComponent != null) {
				broadcastComponent.packet.x = positionComponent.x;
				broadcastComponent.packet.y = positionComponent.y;
			}

			Health healthComponent = (Health) EntityManager.getInstance().getEntityComponent(id,
					EntityManager.HealthID);
			if (healthComponent != null)
				broadcastComponent.packet.health = healthComponent.health;

			broadcastComponent.packet.timeframe = System.currentTimeMillis();

			Broadcast.QueuePacket(id, new WorldPacket(WorldPacket.MOVEMENT_DATA ,broadcastComponent.packet), true);
		//	Session.getInstance().SendWorldPacket(broadcastComponent.packet);
		}

	}
	
	public static void update(int id) {
		brodcastEntityStateToNetowrk(id);
		ProcessQueue(id);
	}
	
}
