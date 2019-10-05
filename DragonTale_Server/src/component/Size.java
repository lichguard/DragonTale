package component;

public class Size implements component.IComponent {
	
	public static final int componentID = EntityManager.SizeID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public float sizeMultiplier = 1.0f;
	public int width = 30;
	public int height = 30;
}
