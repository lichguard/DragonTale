package componentsSystems;

import componentNew.*;


public class AISystem implements IComponentSystem {
	
	
	public static void update(int id) {
		
		Movement movementComponent= (Movement) EntityManager.getInstance().getEntityComponent(id, EntityManager.MovementID);
		if (movementComponent == null)
			return;
		
		Animation animationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id, EntityManager.AnimationID);
		if (animationComponent == null)
			return;


		AI aiComponent=(AI) EntityManager.getInstance().getEntityComponent(id, EntityManager.AIID);
		if (aiComponent == null)
			return;
		
		
		switch (aiComponent.type) {
		case AI.coin:
			movementComponent.right = (false);
			movementComponent.left = (false);
			movementComponent.setJumping(id,false);
			movementComponent.gliding = false;
			break;
		case AI.fireball:
			movementComponent.right = animationComponent.facingRight;
			movementComponent.left = !animationComponent.facingRight;
			movementComponent.setJumping(id,false);
			movementComponent.gliding = false;
			break;
		default:
			break;
		}
		

		//EntityManager.getInstance().setattack(id);
		//EntityManager.getInstance().setattack2(id);
	
		
	}


}
