package component;

import entity.Entity;

public class FadeOut implements IComponent {
	Entity entity;

	long lastbroadcast = 0;

	public FadeOut(Entity entity) {
		this.entity = entity;
		
		/*
		//remove fadeout component if exists
		IComponent fadein = null;
		for (IComponent component  : this.entity.components) {
			if (component instanceof FadeIn)
				fadein = component;
		}
		if (fadein != null)
		this.entity.components.remove(fadein);
		*/
	}

	@Override
	public boolean update() {
		
		entity.alpha -= 0.02;
		
		
		if (entity.alpha <= 0) {
			entity.alpha = 0;
			entity.inWorld = false;
			return false;
		}
		return true;	

	}

}
