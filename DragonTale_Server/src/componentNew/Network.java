package componentNew;

import PACKET.MovementData;

public class Network implements componentNew.IComponent {
	public MovementData packet = new MovementData();
	public long ping = 200;

	public static void setNewPacket(int handle, MovementData data) {
		Network nc = (Network) EntityManager.getInstance().getEntityComponent(handle, EntityManager.NetworkID);
		nc.packet = data;
	}

}
