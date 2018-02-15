package component;

import DesertAdventures.World;
import Entity.ENTITY;
import Network.Session;

public class NetworkComponent implements IComponent {
	
	World world;
	ENTITY entity;
	Session session;
	
	public NetworkComponent(World world,ENTITY entity,Session session)
	{
		this.world = world;
		this.entity = entity;
		this.session = session;
	}
	
	@Override
	public void update() {
		//System.out.println("ping: " + entity.ping);
		float t = (System.currentTimeMillis() - entity.interpolation_start) / (float) entity.ping;
		//System.out.println("x: " + entity.last_packet.x + "  t: " + t);
		entity.setPosition(entity.last_packet.x * (1.0f - t) + entity.new_packet.x * t, entity.last_packet.y * (1.0f - t) + entity.new_packet.y * t);
		//System.out.println("handle: " +entity.handle + " x: " + entity.x + "y: " + entity.y);
		
		entity.setMapPosition();
		
		entity.animation.update();
	}

}
