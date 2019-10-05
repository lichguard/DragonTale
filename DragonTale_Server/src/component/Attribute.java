package component;

public class Attribute implements component.IComponent {
	
	public static final int componentID = EntityManager.AttributeID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	
	public String name = "Unknown";
	public boolean inWorld = true;
	public boolean isDead = false;
	
	public Attribute(String name) {
		this.name = name;
	}
}
