package componentsSystems;

import componentNew.*;

public class NetoworkSystem {

	public static final int UPDATEINTEVAL = 0;

	public static void update(int id) {
		getEntityStatefromNetwork(id);
		brodcastEntityStateToNetowrk(id);
	}

	public static void getEntityStatefromNetwork(int id) {
		Network networkComponent = (Network) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.NetworkID);
		if (networkComponent == null)
			return;

		Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.PositionID);
		if (positionComponent != null) {
			float t = (System.currentTimeMillis() - networkComponent.interpolation_start)
					/ (float) networkComponent.ping;

			//this if is to avoid jitters while standing still
		//	if (positionComponent.x != networkComponent.new_packet.x
		//			&& positionComponent.y != networkComponent.new_packet.y) {
			Position.setPosition(id,
						networkComponent.last_packet.x * (1.0f - t) + networkComponent.new_packet.x * t,
						networkComponent.last_packet.y * (1.0f - t) + networkComponent.new_packet.y * t);
			//Position.setMapPosition();
		//	}
		}

		Health healthComponent = (Health) EntityManager.getInstance().getEntityComponent(id, EntityManager.HealthID);
		if (healthComponent != null)
			healthComponent.health = networkComponent.last_packet.health;

		Animation AnimationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.AnimationID);
		if (AnimationComponent != null) {
			if (networkComponent.last_packet.currentAction != AnimationComponent.currentPlayingAction) {
				AnimationComponent.setAnimation(networkComponent.last_packet.currentAction);
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

		//	Session.getInstance().SendWorldPacket(broadcastComponent.packet);
		}

	}
}
