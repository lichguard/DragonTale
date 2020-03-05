package component;

import java.util.ArrayList;
import java.util.List;

public class AI implements component.IComponent {
	
	public static final int componentID = EntityManager.AIID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public int type;
	public List<AITask> tasks = new ArrayList<AITask>();
	
	public AI(int type) {
		this.type = type;
	}
}
