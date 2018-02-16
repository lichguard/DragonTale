package component;

import Entity.ENTITY;
import Network.Session;
import main.World;

public class AnimationComponent implements  IComponent {

	World world;
	ENTITY entity;
	Session session;
	
	public AnimationComponent(World world,ENTITY entity,Session session)
	{
		this.world = world;
		this.entity = entity;
		this.session = session;
	}
	@Override
	public void update() {
		
		if (entity.currentAction == ENTITY.ATTACK1) {
			if (entity.animation.hasPlayedOnce())
				entity.firing = false;
		}
		if (entity.currentAction == ENTITY.ATTACK2) {
			if (entity.animation.hasPlayedOnce())
				entity.scratching = false;
		}

		// check done flinching
		//if (entity.flinching) {
		//	if ((System.currentTimeMillis() - entity.flinchTimer) > 700)
		//		entity.flinching = false;
		//}

		// set animation
			if (entity.scratching) {
				if (entity.currentAction != ENTITY.ATTACK2) {
					//entity.scratchAttack(world);
					entity.currentAction = ENTITY.ATTACK2;
					entity.SetActionAnimation(ENTITY.ATTACK2);
				}
			} else if (entity.firing) {
				if (entity.currentAction !=ENTITY. ATTACK1) {
					//world.spawn_entity(Spawner.FIREBALL, x, y, facingRight, false);
					// new FireBall(tileMap, facingRight, x, y, 20);
					entity.currentAction = ENTITY.ATTACK1;
					entity.SetActionAnimation(ENTITY.ATTACK1);
				}
			} else if (entity.dy > 0) {
				if (entity.gliding) {
					if (entity.currentAction != ENTITY.GLIDING) {
						entity.currentAction = ENTITY.GLIDING;
						entity.SetActionAnimation(ENTITY.GLIDING);
					}
				} else if (entity.currentAction != ENTITY.FALLING) {
					entity.currentAction = ENTITY.FALLING;
					entity.SetActionAnimation(ENTITY.FALLING);
				}
			} else if (entity.dy < 0) {
				if (entity.currentAction != ENTITY.JUMPING) {
					entity.currentAction = ENTITY.JUMPING;
					entity.SetActionAnimation(ENTITY.JUMPING);
				}
			} else if (entity.left || entity.right) {
				if (entity.currentAction != ENTITY.WALKING) {
					entity.currentAction = ENTITY.WALKING;
					entity.SetActionAnimation(ENTITY.WALKING);
				}
			} else {
				if (entity.currentAction != ENTITY.IDLE) {
					entity.currentAction = ENTITY.IDLE;
					entity.SetActionAnimation(ENTITY.IDLE);
				}
			}
		
		entity.animation.update();
		
	}

}
