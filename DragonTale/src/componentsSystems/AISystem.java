package componentsSystems;

import componentNew.*;

public class AISystem implements IComponentSystem {
	
	
	public static void update(int id) {
		
		Movement movementComponent=(Movement) EntityManager.getInstance().getEntityComponent(id, EntityManager.MovementID);
		if (movementComponent == null)
			return;
		
		AI aiComponent=(AI) EntityManager.getInstance().getEntityComponent(id, EntityManager.AIID);
		if (aiComponent == null)
			return;
		
		movementComponent.right = (false);
		movementComponent.left = (false);
		movementComponent.setJumping(id,true);
		movementComponent.gliding = false;
		//EntityManager.getInstance().setattack(id);
		//EntityManager.getInstance().setattack2(id);
	
		
	}


}
