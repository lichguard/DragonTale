package Entity;

import Network.Session;
import PACKET.NetworkSpawner;
import PACKET.WorldPacket;
import main.World;

public class Spawner {

	public static final int entities_count = 6;
	
	public static final int PLAYERPED = 0;
	public static final int ARACHNIK = 1;
	public static final int SLUGGER = 2;
	public static final int FIREBALL = 3;
	public static final int COIN = 4;
	public static final int EXPLOSION = 5;

	
	public int type;
	public float x;
	public float y;
	public boolean facing;
	public boolean network;
	public int handle;
	public World world;
	public Session session;
	public boolean local_player;

	public Spawner(World world, Session session,boolean local_player ,int handle, int type,float x,float y,boolean facing,boolean network)
	{
		this.world = world;
		this.session = session;
		this.local_player = local_player;
		this.type = type;
		this.x = x;
		this.y =y ;
		this.facing = facing;
		this.network = network;
		this.handle = handle;
	}
	
	public NetworkSpawner castNetworkSpawner()
	{
		return new NetworkSpawner(handle, type, x, y, facing, true);
	}
	
	public void spawn()
	{
		if (world.entities.containsKey(handle))
		{
			System.out.println("Spawn failed because handle " + handle + " exists!");
			return;
		}
		
		ENTITY entity = new ENTITY(type,handle,world,session,local_player,network,x,y,facing);
		
		entity.setPosition(x, y);
		entity.setMapPosition();
		entity.setRight(facing);
		entity.setLeft(!facing);
		entity.setFacingright(facing);
		world.entities.put(handle, entity);
	}
}
