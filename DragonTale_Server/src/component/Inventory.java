package component;

public class Inventory implements component.IComponent {
	
	public static final int componentID = EntityManager.InventoryID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public int coins = 0;
	
	public static void addItem(Integer id, int i) {
		
		Inventory inventoryComponent = (Inventory) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.InventoryID);

		if (i == 0) {
			inventoryComponent.coins += 1;
		}
	}
}
