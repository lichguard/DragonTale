package PACKET;

import java.io.Serializable;

public class WorldPacket implements Serializable {

	private static final long serialVersionUID = 1L;

	public int handle = 0;
	public float x = 0;
	public float y = 0;
	public int currentAction = 0;
	public boolean facingRight = true;
	public long timeframe = 0;
	public int health = 0;

	public WorldPacket()
	{
		
	}
	
	public WorldPacket(int handle, float x, float y, boolean facing)
	{
		this.handle = handle;
		this.x = x;
		this.y =y ;
		this.facingRight = facing;
		this.timeframe = System.currentTimeMillis();
	}
	
	public WorldPacket(WorldPacket packet)
	{
		clone(packet);
	}
	
	public void clone(WorldPacket packet) {
		this.handle = packet.handle;
		this.x = packet.x;
		this.y = packet.y;
		this.currentAction = packet.currentAction;
		this.facingRight = packet.facingRight;
		this.timeframe = packet.timeframe;
		this.health = packet.health;
	}

	// Overriding equals() to compare two Complex objects
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WorldPacket)) {
            return false;
        }
    	WorldPacket packet = (WorldPacket)o;
		return(
		this.handle == packet.handle &&
		this.x == packet.x &&
		this.y == packet.y &&
		this.currentAction ==packet.currentAction &&
		this.facingRight == packet.facingRight &&
		this.health == packet.health);
			
	}
}
