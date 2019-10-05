package component;

public class Inventory implements component.IComponent {
	
	public static final int componentID = EntityManager.InventoryID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public int coins = 0;
}
