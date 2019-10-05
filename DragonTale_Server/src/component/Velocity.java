package component;

public class Velocity implements component.IComponent {
	
	
	public static final int componentID = EntityManager.VelocityID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public float dx = 0;
	public float dy = 0;
	
	public Velocity() {
	}
	
	public Velocity(float dx,float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public static void hit(Integer close_entity) {
		Velocity vc = (Velocity) EntityManager.getInstance().getEntityComponent(close_entity, Velocity.componentID);
		vc.dy = -5.0f;
	}
}
