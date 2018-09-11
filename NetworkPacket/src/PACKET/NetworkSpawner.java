package PACKET;

import java.io.Serializable;

public class NetworkSpawner implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type;
	public float x;
	public float y;
	public boolean facing;
	public boolean network;
	public int handle;
	public String name = "";
	public NetworkSpawner(String name, int handle, int type,float x,float y,boolean facing,boolean network)
	{
		this.type = type;
		this.x = x;
		this.y =y ;
		this.facing = facing;
		this.network = network;
		this.handle = handle;
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return "NetworkSpawner type:" + type + " handle: " + handle;
	}
}
