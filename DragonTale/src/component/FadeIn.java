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
		entity.inWorld = true;
		/*
		//remove fadeout component if exists
				IComponent fadeout = null;
				for (IComponent component  : this.entity.components) {
					if (component instanceof FadeOut)
						fadeout = component;
				}
				if (fadeout != null)
				this.entity.components.remove(fadeout);
*/
	}

	@Override
	public boolean update() {
		
		for (IComponent component  : this.entity.components) {
			if (component instanceof FadeOut)
				return false;
		}
		entity.alpha += 0.02;
		
		if (entity.alpha >= 1.0f) {
			entity.alpha = 1.0f;
			return false;
		}
		return true;	
	}

}
