package componentNew;

import PACKET.MovementData;

public class Network implements componentNew.IComponent {
	public MovementData last_packet = new MovementData();
	public MovementData new_packet =  new MovementData();
	public long ping = 200;
	public long interpolation_start = 0;
}
