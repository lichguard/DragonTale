package component;

import Entity.ENTITY;
import Network.Session;
import main.World;

public class NetworkComponent implements IComponent {

	World world;
	ENTITY entity;
	Session session;

	public NetworkComponent(World world, ENTITY entity, Session session) {
		this.world = world;
		this.entity = entity;
		this.session = session;
	}

	@Override
	public void update() {

	//	if (entity.new_packet.timeframe - entity.last_packet.timeframe > entity.ping) {
	//		entity.setPosition(entity.new_packet.x, entity.new_packet.y);
	//	} else {
			float t = (System.currentTimeMillis() - entity.interpolation_start) / (float) entity.ping;
			//if (t >= 0 && t <= 1)
				entity.setPosition(entity.last_packet.x * (1.0f - t) + entity.new_packet.x * t,
						entity.last_packet.y * (1.0f - t) + entity.new_packet.y * t);
	//	}
		entity.setMapPosition();
		entity.animation.update();
	}

}
