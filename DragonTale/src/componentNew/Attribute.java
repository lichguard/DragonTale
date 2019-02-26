package componentNew;

public class Attribute implements componentNew.IComponent {
	public String name = "Unknown";
	public boolean inWorld = true;
	public boolean isDead = false;
	
	public Attribute(String name) {
		this.name = name;
	}
}
