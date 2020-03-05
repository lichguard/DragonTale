package componentsSystems;

import component.*;
import game.World;


public class AISystem implements IComponentSystem {
	
	
	public static void update(int id) {
		
		Movement movementComponent= (Movement) EntityManager.getInstance().getEntityComponent(id, EntityManager.MovementID);
		if (movementComponent == null)
			return;
		
		Animation animationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id, EntityManager.AnimationID);
		if (animationComponent == null)
			return;

		
		Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID);
		if (positionComponent == null)
			return;
		
		Velocity velocityComponenet = (Velocity) EntityManager.getInstance().getEntityComponent(id, EntityManager.VelocityID);
		if (velocityComponenet == null)
			return;



		AI aiComponent=(AI) EntityManager.getInstance().getEntityComponent(id, EntityManager.AIID);
		if (aiComponent == null)
			return;

		
		//target:1021,314

		if (aiComponent.type == AItypes.slug.ordinal()) {
			if (velocityComponenet.dx == 0) {
				animationComponent.facingRight = !animationComponent.facingRight;
			}
			movementComponent.right = animationComponent.facingRight;
			movementComponent.left = !animationComponent.facingRight;
			movementComponent.setJumping(id, false);
			movementComponent.gliding = false;
		} else if (aiComponent.type == AItypes.zombie.ordinal()) {
		
		} else if (aiComponent.type == AItypes.coin.ordinal()) {
			//Size sc = (Size) EntityManager.getInstance().getEntityComponent(id, Size.componentID);
			
			for (Integer close_entity : World.getNearEntities(id,30,0,360)) {
				
				AI cai =(AI) EntityManager.getInstance().getEntityComponent(close_entity, EntityManager.AIID);
				if (aiComponent == null || cai.type != AItypes.playercontrolled.ordinal())
					continue;
				
				Inventory.addItem(close_entity,0);
				EntityManager.getInstance().deleteEntity(id);
				System.out.println("REMOVIGN ENTITY COIN PLAY ID: " + close_entity + " HAS COLLECTED");
			}
			
			//movementComponent.right = (false);
			//movementComponent.left = (false);
			//movementComponent.setJumping(id, false);
			//movementComponent.gliding = false;
		}

		//EntityManager.getInstance().setattack(id);
		//EntityManager.getInstance().setattack2(id);
	
		
	}


}
