package objects;


import java.util.logging.Level;

import PACKET.NetworkSpawner;
import game.World;
import main.LOGGER;
import network.WorldSocket;


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
	public int network;
	public int handle;
	public WorldSocket socketCallback;
	public String name;
	public Spawner(String name, int handle, int type,double x,double y,boolean facing,int network,WorldSocket socketCallback)
	{
		this.type = type;
		this.x = (float)x;
		this.y = (float)y ;
		this.facing = facing;
		this.network = network;
		this.handle = handle;
		this.socketCallback = socketCallback;
		this.name = name;
	}
	
	public NetworkSpawner castNetworkSpawner()
	{
		return new NetworkSpawner(name, handle, type, x, y, facing, network);
	}
	
	public void create_entity()
	{

		GameObject entity = null;
		switch (type) {
		case PLAYERPED :
			entity = new Player(socketCallback);
			if (socketCallback != null) {
				LOGGER.info("socketCallback worked for entity", this);
				entity.broadcaster.setSocket(socketCallback);
				socketCallback.m_session._player = (Player)entity;
			}
			break;
		case ARACHNIK :
			entity = new Arachnik();
			break;
		case SLUGGER :
			entity = new Slugger();
			break;
		case FIREBALL :
			entity = new FireBall(facing,x,y,3);
			break;
		case COIN :
			entity = new Coin();
			break;
		case EXPLOSION :
			entity = new Explosion();
			break;
		}
		entity.SetMap(World.getInstance().tm);
		entity.type = type;
		entity.isNetowrkEntity = network;
		entity.setHandle(handle);
		entity.setPosition(x, y);
		entity.setMapPosition();
		entity.setRight(facing);
		entity.setLeft(!facing);
		entity.facingRight = facing;
		entity.name = this.name;
		LOGGER.log(Level.INFO, "new handle: " + handle, this);
		World.getInstance().m_gameObjectsMap.put(handle, entity);
	}
}
