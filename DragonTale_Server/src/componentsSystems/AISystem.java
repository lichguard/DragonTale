package componentsSystems;

import component.*;


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
		
		
		switch (aiComponent.type) {
		case 0:
			break;
		case 4:
			movementComponent.right = (false);
			movementComponent.left = (false);
			movementComponent.setJumping(id,false);
			movementComponent.gliding = false;
			break;
		default:
			/*
			Collision collisionComponent = (Collision) EntityManager.getInstance().getEntityComponent(id, EntityManager.CollisionID);
			if (collisionComponent == null)
				return;
			
			System.out.println("===============================================" );
			System.out.println(" collisionComponent.topLeft : " +  collisionComponent.topLeft );
			System.out.println(" collisionComponent.topRight : " +  collisionComponent.topRight );
			System.out.println(" collisionComponent.bottomLeft : " +  collisionComponent.bottomLeft );
			System.out.println(" collisionComponent.bottomRight : " +  collisionComponent.bottomRight );
			
			if ( collisionComponent.topLeft || collisionComponent.topRight) {
				animationComponent.facingRight = !animationComponent.facingRight;
			}
			*/
			if (velocityComponenet.dx == 0) {
				animationComponent.facingRight  = !animationComponent.facingRight;
			}
			
			movementComponent.right = animationComponent.facingRight;
			movementComponent.left = !animationComponent.facingRight;
			movementComponent.setJumping(id,false);
			movementComponent.gliding = false;
			break;
		}
		

		//EntityManager.getInstance().setattack(id);
		//EntityManager.getInstance().setattack2(id);
	
		
	}


}
