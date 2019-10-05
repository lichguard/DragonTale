package component;

import java.awt.Font;

public class Chat implements component.IComponent {
	
	public static final int componentID = EntityManager.ChatID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public String txt = "";
	public Font font = new Font("Arial", Font.PLAIN,11);
	

	
}
