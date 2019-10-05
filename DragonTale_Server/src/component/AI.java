package component;

public class AI implements component.IComponent {
	
	public static final int componentID = EntityManager.AIID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public int type;
	
	public AI(int type) {
		this.type = type;
	}
}
