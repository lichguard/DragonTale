package componentsSystems;


import component.*;

public class NetoworkSystem {

	

	public static void update(int id) {
		getEntityStatefromNetwork(id);
	}

	public static void getEntityStatefromNetwork(int id) {
		Network networkComponent = (Network) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.NetworkID);
		if (networkComponent == null)
			return;

		Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.PositionID);
		if (positionComponent != null) {

			//this if is to avoid jitters while standing still
		//	if (positionComponent.x != networkComponent.new_packet.x
		//			&& positionComponent.y != networkComponent.new_packet.y) {
			Position.setPosition(id,networkComponent.packet.x,networkComponent.packet.y);
			//Position.setMapPosition();
		//	}
		}

		Health healthComponent = (Health) EntityManager.getInstance().getEntityComponent(id, EntityManager.HealthID);
		if (healthComponent != null)
			healthComponent.health = networkComponent.packet.health;

		Animation AnimationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.AnimationID);
		if (AnimationComponent != null) {
			if (networkComponent.packet.currentAction != AnimationComponent.currentPlayingAction) {
				AnimationComponent.setAnimation(networkComponent.packet.currentAction);
			}
		}

	}

}
