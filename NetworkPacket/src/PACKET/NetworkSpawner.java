package PACKET;

import java.io.Serializable;

public class NetworkSpawner implements Serializable {

	private static final long serialVersionUID = 3L;
	public int type;
	public float x;
	public float y;
	public boolean facing;
	public boolean network;
	public int handle;
	public NetworkSpawner(int handle, int type,float x,float y,boolean facing,boolean network)
	{
		this.type = type;
		this.x = x;
		this.y =y ;
		this.facing = facing;
		this.network = network;
		this.handle = handle;
	}
	
}
