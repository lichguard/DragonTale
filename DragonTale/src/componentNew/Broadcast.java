package componentNew;

import PACKET.MovementData;

public class Broadcast implements componentNew.IComponent {

	public long lastbroadcast = 0;
	public MovementData packet = new MovementData();
}
