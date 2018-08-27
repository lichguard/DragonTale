package Entity;
import TileMap.TileMap;
import game.World;


public class Unit extends WorldObject {

	protected boolean flinching;
	protected long filinchTimer;

	protected long flinchTimer;
	protected int maxHealth;
	protected double maxGlideSpeed;
	protected boolean gliding;
	protected boolean jumping;
	protected int doublejump = 2;
	protected double jumpStart;
	protected double stopJumpSpeed;
	protected boolean firing;
	protected boolean attack;
	protected boolean scratching;
	protected boolean sprint;
	protected String speech_bubble;
	protected long bubble_start;
	protected int coins = 0;

	public double ping = 200;
	public long interpolation_tick_start = 0;

	public Unit(TileMap tm) {
		super(tm);

		speech_bubble = "";
		right = true;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		doublejump = 0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		health = maxHealth = 5;
		maxGlideSpeed = 1.2;
		doublejump = 0;
		sprint = false;
		// weap1 = new Scratch();
		// weap2 = new FireBall();

	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getMaxFire() {
		return 0;
	}

	public int getCoinsCount() {
		return coins;
	}

	public void addCoins(int amount) {
		coins += amount;
	}

	public void removeCoins(int amount) {
		coins -= amount;
	}

	public void setattack() {
		firing = true;
	}

	public void setattack2() {
		scratching = true;
	}

	public void setGliding(boolean b) {
		gliding = b;
	}

	public void setFalling(boolean b) {
		falling = b;
	}

	public boolean isFalling() {
		return falling;
	}

	public boolean isGliding() {
		return gliding;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void gethit(int damage, Unit enemy) {
		if (flinching)
			return;
		health -= damage;
		if (health < 0)
			health = 0;

		// if (health == 0)
		// this.dead = true;

		flinching = true;
		flinchTimer = System.currentTimeMillis();

		if (x < enemy.getx())
			dx = -3.0;
		else
			dx = 3.0;

		dy = -2.5;
	}

	public void setJumping(boolean b) {

		if (!b && doublejump == 0)
			doublejump = 1;

		if (b && doublejump == 1) {
			doublejump = 2;
			falling = false;
			dy = 0;
		}

		jumping = b;
	}

	private void getNextPosition() {

		if (facingRight && left)
			facingRight = false;
		else if (!facingRight && right)
			facingRight = true;

		// DEBUG
		// if (dx == 0)
		// sprint = false;
		// else
		// maxSpeed = sprint ? 3.0:1.6;

		// if (CONTROLS.isdoublePressed(CONTROLS.RIGHT))
		// sprint = true;

		// movement

		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed)
				dx = -maxSpeed;
		} else if (right) {
			dx += moveSpeed;

			if (dx > maxSpeed)
				dx = maxSpeed;
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0)
					dx = 0;
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0)
					dx = 0;
			}
		}

		if (jumping && (!falling)) {
			dy = jumpStart;
			falling = true;
		}

		if (falling) {
			if (dy > 0 && gliding) {

				if (dy < maxGlideSpeed)
					dy += fallSpeed * 0.1;
				else
					dy -= fallSpeed;
			} else
				dy += fallSpeed;

			if (dy > 0)
				jumping = false;

			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed)
				dy = maxFallSpeed;
		} else if (doublejump != 0)
			doublejump = 0;

	}

	private void scratchAttack(World world) {
/*
		ArrayList<ENTITY> entities;
		if (facingRight)
			entities = world.getNearEntities((ENTITY) this, 45, 0, 180);
		else
			entities = world.getNearEntities((ENTITY) this, 45, 180, 360);

		for (ENTITY entity : entities) {
			if (entity instanceof Enemy) {

				Enemy enemy = (Enemy) entity;
				enemy.hit(60);
			}

		}
*/
	}

	@Override
	public void update(World world) {
		super.update(world);

		if (isNetowrkEntity)
			return;

		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		if (currentAction == ATTACK1) {
			// if (animation.hasPlayedOnce())
			// TODO: DELAY
			firing = false;
		}
		if (currentAction == ATTACK2) {
			// if (animation.hasPlayedOnce())
			// TODO:DELAY
			scratching = false;
		}

		if (flinching) {
			if ((System.currentTimeMillis() - flinchTimer) > 700)
				flinching = false;
		}

		// set animation
			if (scratching) {
				if (currentAction != ATTACK2) {
					scratchAttack(world);
					currentAction = ATTACK2;
				}
			} else if (firing) {
				if (currentAction != ATTACK1) {
					world.requestObjectSpawn(Spawner.FIREBALL, x, y, facingRight, false,null);
					currentAction = ATTACK1;

				}
			} else if (dy > 0) {
				if (gliding) {
					if (currentAction != GLIDING) {
						currentAction = GLIDING;

					}
				} else if (currentAction != FALLING) {
					currentAction = FALLING;

				}
			} else if (dy < 0) {
				if (currentAction != JUMPING) {
					currentAction = JUMPING;

				}
			} else if (left || right) {
				if (currentAction != WALKING) {
					currentAction = WALKING;

				}
			} else {
				if (currentAction != IDLE) {
					currentAction = IDLE;

				}
			}
		//setMapPosition();
	}

	public boolean isFacingright() {
		return facingRight;
	}

	public void setFacingright(boolean face) {
		facingRight = face;
	}

}
