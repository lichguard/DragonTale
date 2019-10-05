package component;

public class Audio implements component.IComponent {
	public static final int componentID = EntityManager.AudioID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	//public HashMap<String, AudioPlayer> sfx;
}
