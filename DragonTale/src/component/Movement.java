package component;

public class Movement implements component.IComponent {
	public boolean left = false;
	public boolean right = false;
	public boolean up = false;
	public boolean down = false;
	public boolean falling = false;
	
	public float moveSpeed = 0.8f;
	public float maxSpeed  = 1.9f;
	public float stopSpeed = 0.03f;
	public float fallSpeed = 0.009f;
	public float maxFallSpeed = 4.0f;
	public float swimSpeed = 0.1f; 
	public float maxSwimSpeed = 4.0f;
	
	public float xtemp = 0; 
	public float ytemp = 0;
	
	public boolean flinching = false;
	public long filinchTimer;
	public float maxGlideSpeed = 1.2f;
	public boolean gliding = false;
	public boolean jumping = false;
	public int doublejump = 2;
	public float jumpStart = -3.5f;
	public float stopJumpSpeed = 0.02f;
	public boolean firing = false;
	public boolean attack = false;
	public boolean scratching = false;
	public boolean sprint = false;
	
	public long jumpstarttime = 0;

	
	public void setJumping(int id,boolean b) {

		if (!b && doublejump == 0)
			doublejump = 1;

		if (b && doublejump == 1) {
			doublejump = 2;
			falling = false;
			
			Velocity positionComponent = (Velocity) EntityManager.getInstance().getEntityComponent(id,
					EntityManager.VelocityID);
	
			positionComponent.dy = 0;
		}

		jumping = b;
	}
	
}
