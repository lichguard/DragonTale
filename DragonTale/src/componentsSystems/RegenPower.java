package componentsSystems;

import component.*;

public class RegenPower {
	
	
	public static void update(int id, long timeDelta) {
		Health healthComponent=(Health) EntityManager.getInstance().getEntityComponent(id, EntityManager.HealthID);
		if (healthComponent != null) {
			if (healthComponent.health < healthComponent.maxHealth) {
				healthComponent.health += healthComponent.regenHealth;
			}
		}
			
	}
}
