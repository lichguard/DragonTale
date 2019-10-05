package component;

public class Attribute implements component.IComponent {
	public String name = "Unknown";
	public boolean inWorld = true;
	public boolean isDead = false;
	
	public Attribute(String name) {
		this.name = name;
	}
}
