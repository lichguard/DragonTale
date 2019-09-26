package componentNew;

import PACKET.MovementData;

public class Network implements componentNew.IComponent {
	public MovementData entity_packet = new MovementData();
	public MovementData last_packet = new MovementData();
	public MovementData new_packet =  new MovementData();
	public long ping = 200;
	public long interpolation_start = 0;
	
	public static void setNewPacket(int handle, MovementData data) {
		Network nc = (Network) EntityManager.getInstance().getEntityComponent(handle, EntityManager.NetworkID);
		nc.new_packet =data;
	}
	
}
