package componentsSystems;

import component.*;
import network.Session;

public class NetoworkSystem {

	public static final int UPDATEINTEVAL = 0;

	public static void update(int id, long timeDelta) {

		getEntityStatefromNetwork(id);
		brodcastEntityStateToNetowrk(id);
	}

	public static void getEntityStatefromNetwork(int id) {
		Network networkComponent = (Network) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.NetworkID);

		if (networkComponent == null)
			return;

		Health healthComponent = (Health) EntityManager.getInstance().getEntityComponent(id, EntityManager.HealthID);
		if (healthComponent != null)
			healthComponent.health = networkComponent.last_packet.health;

		if (id != Session.getInstance().handle) {
			
			Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
					EntityManager.PositionID);
			//networkComponent.last_packet.x != 0 is while witing for the first packet to arrive
			
		
			
			if (positionComponent != null && networkComponent.interpolation_start != 0 &&
					networkComponent.last_packet.x != 0) {
				
		
				
				float t = (System.currentTimeMillis() - networkComponent.interpolation_start)
						/ (float) networkComponent.ping;

				// this if is to avoid jitters while standing still
				// if (positionComponent.x != networkComponent.new_packet.x
				// && positionComponent.y != networkComponent.new_packet.y) {
				positionComponent.setPosition(
						networkComponent.last_packet.x * (1.0f - t) + networkComponent.new_packet.x * t,
						networkComponent.last_packet.y * (1.0f - t) + networkComponent.new_packet.y * t);
				positionComponent.setMapPosition();
				// }
/*
				if (id == 1)
				{
					System.out.println("last_packet: " + networkComponent.last_packet.x);
					System.out.println("new_packet: " + networkComponent.new_packet.x);
					System.out.println("t:" + t);
					System.out.println("networkComponent.interpolation_start:" + networkComponent.interpolation_start);
					
					System.out.println(positionComponent.x + ", " + positionComponent.y);
					System.out.println(" ");
				}
				*/

			}
			Animation AnimationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id,
					EntityManager.AnimationID);
			if (AnimationComponent != null) {
				if (networkComponent.last_packet.currentAction != AnimationComponent.currentPlayingAction) {
					AnimationComponent.setAnimation(networkComponent.last_packet.currentAction);
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

			// Health healthComponent = (Health)
			// EntityManager.getInstance().getEntityComponent(id,
			// EntityManager.HealthID);
			// if (healthComponent != null)
			// broadcastComponent.packet.health = healthComponent.health;

			broadcastComponent.packet.timeframe = System.currentTimeMillis();

			Session.getInstance().SendWorldPacket(broadcastComponent.packet);
		}

	}
}
