package component;

import dt.entity.Entity;
import dt.network.Session;
import main.World;

public class BroadCastEntityComponent implements IComponent {

	World world;
	Entity entity;
	Session session;
	long lastbroadcast = 0;

	public BroadCastEntityComponent(World world, Entity entity, Session session) {
		this.world = world;
		this.entity = entity;
		this.session = session;
	}

	@Override
	public void update() {
		if (System.currentTimeMillis() - lastbroadcast > 100) {
			session.SendWorldPacket(entity.getEntityPacket());
			lastbroadcast = System.currentTimeMillis();
		}
	}
}
