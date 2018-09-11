package component;

import entity.Entity;
import main.World;
import network.Session;

public class FadeIn implements IComponent {

	World world;
	Entity entity;
	Session session;
	long lastbroadcast = 0;

	public FadeIn(World world, Entity entity, Session session) {
		this.world = world;
		this.entity = entity;
		this.session = session;
		this.entity.alpha = 0;
	}

	@Override
	public boolean update() {
		
		entity.alpha += 0.02;
		
		if (entity.alpha >= 1.0f) {
			entity.alpha = 1.0f;
			return false;
		}
		return true;	
	}

}
