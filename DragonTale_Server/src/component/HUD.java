package component;

import java.awt.Font;

public class HUD implements component.IComponent {
	
	public static final int componentID = EntityManager.HUDID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public Font font =new Font("Arial", Font.PLAIN,11);
}
