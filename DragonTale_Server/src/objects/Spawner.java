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
	public boolean network;
	public int handle;
	public WorldSocket socketCallback;
	public Spawner(int handle, int type,double x,double y,boolean facing,boolean network,WorldSocket socketCallback)
	{
		this.type = type;
		this.x = (float)x;
		this.y = (float)y ;
		this.facing = facing;
		this.network = network;
		this.handle = handle;
		this.socketCallback = socketCallback;
	}
	
	public NetworkSpawner castNetworkSpawner()
	{
		return new NetworkSpawner(handle, type, x, y, facing, true);
	}
	
	public void create_entity()
	{

		GameObject entity = null;
		switch (type) {
		case PLAYERPED :
			entity = new Player(World.getInstance().tm);
			if (socketCallback != null) {
				LOGGER.info("socketCallback worked for entity", this);
				entity.broadcaster.setSocket(socketCallback);
				socketCallback.m_session._player = (Player)entity;
			}
			break;
		case ARACHNIK :
			entity = new Arachnik(World.getInstance().tm);
			break;
		case SLUGGER :
			entity = new Slugger(World.getInstance().tm);
			break;
		case FIREBALL :
			entity = new FireBall(World.getInstance().tm,facing,x,y,3);
			break;
		case COIN :
			entity = new Coin(World.getInstance().tm);
			break;
		case EXPLOSION :
			entity = new Explosion(World.getInstance().tm);
			break;
		}
		entity.type =type;
		entity.isNetowrkEntity = network;
		entity.setHandle(handle);
		entity.setPosition(x, y);
		entity.setMapPosition();
		entity.setRight(facing);
		entity.setLeft(!facing);
		entity.facingRight = facing;
		
		LOGGER.log(Level.INFO, "new handle: " + handle, this);
		World.getInstance().m_gameObjectsMap.put(handle, entity);
	}
}
