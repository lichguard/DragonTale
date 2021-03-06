package component;

import PACKET.MovementData;

public class Network implements component.IComponent {
	
	public static final int componentID = EntityManager.NetworkID;
	@Override
	public int getComponentID() {
		return componentID;
	}
	
	public MovementData packet = new MovementData();
	public long ping = 200;

	public static void setNewPacket(int handle, MovementData data) {
		Network nc = (Network) EntityManager.getInstance().getEntityComponent(handle, EntityManager.NetworkID);
		if (nc == null || data == null ) {
			System.out.println("component Network: SOMETHING HERE IS NULL! handle: " +handle);
		} else 
			nc.packet = data;
	}

}
