package componentsSystems;

import component.Animation;
import component.Attribute;
import component.EntityManager;
import component.Health;
import component.Size;
import component.Velocity;
import game.World;

public class AttackSystem {

	
	public static void meleeattack(int handle) {
		
		Attribute att = (Attribute) EntityManager.getInstance().getEntityComponent(handle, Attribute.componentID);
		if (System.currentTimeMillis() - att.last_attack_timestamp < 2000) {
			return;
		}
		
		
		
		Animation ac = (Animation) EntityManager.getInstance().getEntityComponent(handle, Animation.componentID);
		Size sc = (Size) EntityManager.getInstance().getEntityComponent(handle, Size.componentID);
		
		
		int angle = 0;
		if (!ac.facingRight) {
			angle = 180;
		}	
		for (Integer close_entity : World.getNearEntities(handle,sc.width + 10,angle,angle + 180)) {
		
			Health.decreaseHealth(close_entity,15);
			Velocity.hit(close_entity,angle);
			att.last_attack_timestamp = System.currentTimeMillis();
		}
		
	}

}
