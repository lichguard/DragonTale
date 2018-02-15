package game;

import java.awt.Point;
import Entity.ENTITY;
import Entity.Spawner;
import PACKET.CommandPacket;
import PACKET.NetworkSpawner;
import PACKET.WorldPacket;
import TileMap.TileMap;
import servers.ThreadPooledServer;

public class Gameplay implements Runnable {

	public static final int WIDTH = 320; // 320
	public static final int HEIGHT = 268; // 268

	public static final int SCALE = 900;
	public static final boolean DEBUG = true;
	public static final boolean DISABLEESOUND = true;
	private Thread thread;
	public boolean running;
	private int target_FPS = 60;
	public static long FPS = 0;
	private long targetTime = 1000 / target_FPS;
	public World world;
	public TileMap tileMap;
	public ThreadPooledServer server;
	public long lastbroadcast = 0;

	public Gameplay() {
	}

	private void init() {
		running = true;
		tileMap = new TileMap(30);
		tileMap.loadTiles("/TileSets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07);
		server = new ThreadPooledServer(9000);
		world = new World(tileMap);
		(new Thread(server)).start();


		Point[] points = new Point[] { new Point(353, 140) };

		for (Point p : points) {
			world.spawn_entity(Spawner.SLUGGER, p.x, p.y, true, false);
		}
	
	}

	public void addNotify() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {

		init();
		long start;
		long elapsed;
		long wait;
		while (running) {
			start = System.nanoTime();
			update();

			elapsed = System.nanoTime() - start;
			FPS = elapsed / 1000000;
			wait = Math.max(targetTime - FPS, 5);
			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		server.stop();

	}

	public void update() {
		// updates world
		// disptach commands
		synchronized (server.commandsPackets) {
			while (!server.commandsPackets.isEmpty()) {
				CommandPacket packet = server.commandsPackets.poll();
				switch (packet.packet_code) {
				case CommandPacket.REQUEST_HANDLE:
					int handle = world.spawn_entity((NetworkSpawner) packet.data);
					server.SendCommand(packet.session_id, new CommandPacket(CommandPacket.HANDLE, handle));
					server.setsessionpedhandle(packet.session_id,handle);
					for (ENTITY entity : world.entities.values()) {
						server.SendCommand(packet.session_id,
								new CommandPacket(CommandPacket.SPAWN, entity.getNetowrkSpawner()));
					}
					break;
				case CommandPacket.SPAWN:
					world.spawn_entity((NetworkSpawner) packet.data);
					break;
				case CommandPacket.SPEECH:
					server.BroadcastCommand(packet);
					break;
				case CommandPacket.DESPAWN:
					world.despawn_entity((int) packet.data);
					break;
					/*
				case CommandPacket.ADD_SESSION:
					server.addSession(packet.session_id, (Session) packet.data );
					break;
				case CommandPacket.REMOVE_SESSION:
					server.removSession(packet.session_id);
					world.despawn_entity((int) packet.data);
					break;
					*/
				default:
					System.out.println("Unknown CommandPacket code: " + packet.packet_code);
					break;
				}
			}
		}

		// dispatch worldpackets
		synchronized (server.worldPackets) {
			while (!server.worldPackets.isEmpty()) {
				WorldPacket packet = server.worldPackets.pop();
				if (world.entities.containsKey(packet.handle)) {
					world.entities.get(packet.handle).updatePacket(packet, world);
				}
			}
		}

		// send spawns
		for (Spawner spawner : world.entities_to_spawn) {
			server.BroadcastCommand(new CommandPacket(CommandPacket.SPAWN, spawner.castNetworkSpawner()));
		}
		// send despawns
		
		for (int handle : world.entities_to_remove) {
			server.BroadcastCommand(new CommandPacket(CommandPacket.DESPAWN, handle));
		}
		
		if (System.currentTimeMillis() - lastbroadcast > 100) {
			// send worldpackets
			for (ENTITY entity : world.entities.values()) {
				WorldPacket p = entity.getEntityPacket();
				//System.out.println("handle: " +p.handle + " x: " + p.x + "y: " + p.y);
				server.BroadcastWorldPacket(p);
				
				//System.out.println("h: " + p.handle + " x: " + p.x + " y: " + p.y);
			}
			lastbroadcast = System.currentTimeMillis();
		}
		
		world.update();
		
	}


}
