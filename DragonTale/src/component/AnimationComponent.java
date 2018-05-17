package component;

import dt.entity.Animation;
import dt.entity.Entity;
import dt.network.Session;
import main.World;

public class AnimationComponent implements  IComponent {

	World world;
	Entity entity;
	Session session;
	
	public AnimationComponent(World world,Entity entity,Session session)
	{
		this.world = world;
		this.entity = entity;
		this.session = session;
	}
	@Override
	public void update() {
		
		if (entity.currentAction == Animation.ATTACK1) {
			if (entity.animation.hasPlayedOnce())
				entity.firing = false;
		}
		if (entity.currentAction == Animation.ATTACK2) {
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
				if (entity.currentAction != Animation.ATTACK2) {
					//entity.scratchAttack(world);
					entity.currentAction = Animation.ATTACK2;
					entity.SetActionAnimation(Animation.ATTACK2);
				}
			} else if (entity.firing) {
				if (entity.currentAction !=Animation. ATTACK1) {
					//world.spawn_entity(Spawner.FIREBALL, x, y, facingRight, false);
					// new FireBall(tileMap, facingRight, x, y, 20);
					entity.currentAction = Animation.ATTACK1;
					entity.SetActionAnimation(Animation.ATTACK1);
				}
			} else if (entity.dy > 0) {
				if (entity.gliding) {
					if (entity.currentAction != Animation.GLIDING) {
						entity.currentAction = Animation.GLIDING;
						entity.SetActionAnimation(Animation.GLIDING);
					}
				} else if (entity.currentAction != Animation.FALLING) {
					entity.currentAction = Animation.FALLING;
					entity.SetActionAnimation(Animation.FALLING);
				}
			} else if (entity.dy < 0) {
				if (entity.currentAction != Animation.JUMPING) {
					entity.currentAction = Animation.JUMPING;
					entity.SetActionAnimation(Animation.JUMPING);
				}
			} else if (entity.left || entity.right) {
				if (entity.currentAction != Animation.WALKING) {
					entity.currentAction = Animation.WALKING;
					entity.SetActionAnimation(Animation.WALKING);
				}
			} else {
				if (entity.currentAction != Animation.IDLE) {
					entity.currentAction = Animation.IDLE;
					entity.SetActionAnimation(Animation.IDLE);
				}
			}
		
		entity.animation.update();
		
	}

}
