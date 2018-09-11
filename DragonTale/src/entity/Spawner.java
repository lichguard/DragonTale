package entity;

import java.util.logging.Level;

import PACKET.NetworkSpawner;
import main.LOGGER;
import main.World;
import network.Session;

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
	public boolean local_player;
	public String name;
	
	public Spawner(String name, boolean local_player ,int handle, int type,float x,float y,boolean facing,boolean network)
	{
		this.local_player = local_player;
		this.type = type;
		this.x = x;
		this.y =y ;
		this.facing = facing;
		this.network = network;
		this.handle = handle;
		this.name = name;
	}
	
	public NetworkSpawner castNetworkSpawner()
	{
		return new NetworkSpawner(name,handle, type, x, y, facing, true);
	}
	
	public boolean spawn()
	{
		if (World.getInstance().entities.containsKey(handle))
		{
			LOGGER.log(Level.SEVERE,"Spawn failed because handle " + handle + " exists!", this);
			World.getInstance().entities.get(handle).fadeOut();
			return false;
		}
		
		Entity entity = new Entity(type,handle,World.getInstance(),Session.getInstance(),local_player,network,x,y,facing);
		
		entity.setPosition(x, y);
		entity.setMapPosition();
		entity.setRight(facing);
		entity.setLeft(!facing);
		entity.setFacingright(facing);
		entity.name = name;
		World.getInstance().entities.put(handle, entity);
		return true;
	}
}
