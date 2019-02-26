package componentsSystems;
import componentNew.*;

public class AnimationSystem implements IComponentSystem {

	public static void update(int id) {

		// check if requiredComponent Exists
		Animation animationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.AnimationID);

		if (animationComponent != null) {
			updateAnimationAction(id,animationComponent);
			updateAnimationFrame(id,animationComponent);
		
		}
		
	
		
		

	}

	public static void updateAnimationFrame(int id,Animation animationComponent) {
		// Continue execution
		if (animationComponent.delay == -1l)
			return;

		long elapsed = (System.nanoTime() - animationComponent.startTime) / 1000000l;
		if (elapsed > animationComponent.delay * animationComponent.animationSpeed) {

			if (animationComponent.currentFrame + 1 >= animationComponent.numofframes) {
				animationComponent.currentFrame = 0;
				animationComponent.playedonce = true;
			} else {
				animationComponent.currentFrame++;
			}

			animationComponent.startTime = System.nanoTime();
		}
	}
	
	public static void updateAnimationAction(int id,Animation animationComponent) {

		Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.PositionID);
		
		if (positionComponent == null)
			return;
		Movement movementComponent = (Movement) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.MovementID);
		
		if (movementComponent == null)
			return;
		
		Velocity velocityComponent = (Velocity) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.VelocityID);
		
		if (velocityComponent == null)
			return;
		
		Attribute attrributeComponent = (Attribute) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.AttributeID);
		
		if (attrributeComponent == null)
			return;
		
		
		if (animationComponent.getCurrenPlayingAnimation() == Animation.ATTACK1) {
			if (animationComponent.hasPlayedOnce())
				movementComponent.scratching = false;
		}
		if (animationComponent.getCurrenPlayingAnimation() == Animation.ATTACK2) {
			if (animationComponent.hasPlayedOnce())
				movementComponent.firing = false;
		}

		//check done flinching
		/*
		if (entity.flinching) {
			if ((System.currentTimeMillis() - entity.flinchTimer) > 700)
				entity.flinching = false;
		}
*/
		
		// set animation
		if (attrributeComponent.isDead) {
			EntityManager.getInstance().setCurrentAction(id, Animation.DEAD);
		}
		else if (movementComponent.scratching) {
			EntityManager.getInstance().setCurrentAction(id, Animation.ATTACK1);
		} else if (movementComponent.firing) {
			EntityManager.getInstance().setCurrentAction(id, Animation.ATTACK2);
		} else if (velocityComponent.dy > 0) {
			if (movementComponent.gliding) {
				EntityManager.getInstance().setCurrentAction(id, Animation.GLIDING);
			} else {
				EntityManager.getInstance().setCurrentAction(id, Animation.FALLING);
			}
		} else if (velocityComponent.dy < 0) {
			EntityManager.getInstance().setCurrentAction(id, Animation.JUMPING);
		} else if (movementComponent.left || movementComponent.right) {
			EntityManager.getInstance().setCurrentAction(id, Animation.WALKING);
		} else {
			EntityManager.getInstance().setCurrentAction(id, Animation.IDLE);
		}
	}
}
