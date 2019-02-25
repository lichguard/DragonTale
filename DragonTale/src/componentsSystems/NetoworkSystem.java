package componentsSystems;

import componentNew.*;
import network.Session;

public class NetoworkSystem {

	public static final int UPDATEINTEVAL = 0;
	
	public static void update(int id) {
		
		
		
		Network networkComponent = (Network) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.NetworkID);
		
		Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.PositionID);
		if (networkComponent != null && positionComponent != null) {
			
			//	if (entity.new_packet.timeframe - entity.last_packet.timeframe > entity.ping) {
			//		entity.setPosition(entity.new_packet.x, entity.new_packet.y);
			//	} else {
					float t = (System.currentTimeMillis() - networkComponent.interpolation_start) / (float) networkComponent.ping;
					//if (t >= 0 && t <= 1)
					positionComponent.setPosition(networkComponent.last_packet.x * (1.0f - t) + networkComponent.new_packet.x * t,
								networkComponent.last_packet.y * (1.0f - t) + networkComponent.new_packet.y * t);
			//	}
				positionComponent.setMapPosition();
				//entity.animation.update();
				
		}
		
		
		Broadcast broadcastComponent = (Broadcast) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.BroadCastID);
		if (broadcastComponent != null) {

			if (System.currentTimeMillis() - broadcastComponent.lastbroadcast > UPDATEINTEVAL) {
				/*
				Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
						EntityManager.PositionID);
				*/
				if (positionComponent == null) {
					return;
				}
				
				Animation animationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id,
						EntityManager.AnimationID);
				if (animationComponent != null) {
					broadcastComponent.packet.facingRight = animationComponent.facingRight;
				}
				
				broadcastComponent.packet.handle = id;
				broadcastComponent.packet.timeframe = System.currentTimeMillis();
				broadcastComponent.packet.x = positionComponent.x;
				broadcastComponent.packet.y = positionComponent.y;
				Session.getInstance().SendWorldPacket(broadcastComponent.packet);
				broadcastComponent.lastbroadcast = System.currentTimeMillis();
			}
			
		}

	}
}
