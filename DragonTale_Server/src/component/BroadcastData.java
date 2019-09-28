package component;

import PACKET.WorldPacket;

public class BroadcastData {
	public WorldPacket packet;
	public boolean sendToSelf;
	public int handle;

	public BroadcastData(WorldPacket p, boolean self, int handle) {
		this.packet = p;
		this.sendToSelf = self;
		this.handle = handle;
	}
}
