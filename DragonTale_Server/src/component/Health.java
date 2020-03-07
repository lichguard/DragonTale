package component;

public class Health implements component.IComponent {
	
	public static final int componentID = EntityManager.HealthID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public int maxHealth = 100;
	public int health = 100;
	public int regenHealth = 0;
	
	public static void decreaseHealth(int handle, int health) {
		Health hc = (Health) EntityManager.getInstance().getEntityComponent(handle, Health.componentID);
		
		AI  ai = (AI) EntityManager.getInstance().getEntityComponent(handle, AI.componentID);
		 
		
		 if (hc.health <= 0) {
			 if (ai.type != AItypes.playercontrolled.ordinal())
				 EntityManager.getInstance().die(handle);
		 } else {
			 hc.health -= health;
		 }
	}
}
