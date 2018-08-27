package dt.gamestate;

import TileMap.*;
import dt.audio.AudioPlayer;
import main.CONTROLS;
import main.LOGGER;
import main.World;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import PACKET.MovementData;
import PACKET.NetworkSpawner;
import PACKET.SpeechPacket;
import PACKET.WorldPacket;

public class OnlineState extends GameState {

	private AudioPlayer bgmusic;
	private TileMap tileMap;
	private Background bg;
	private int playerhandle = -1;
	public Map<Integer, Integer> requested_spawns_from_server = new HashMap<Integer,Integer>();

	public OnlineState(GameStateManager gsm) {
		super(gsm);
	}

	private void populateMap() {

		/*
		 * //new Point(200, 50) Point[] points = new Point[] { new Point(860, 195), new
		 * Point(1525, 195), new Point(1680, 195), new Point(1800, 195),new Point(430,
		 * 50),new Point(1205,65),new Point(2353,190) };
		 * 
		 * for (Point p : points) { world.spawn_entity(Spawner.SLUGGER,
		 * p.x,p.y,true,false); }
		 * 
		 * points = new Point[] { new Point(1020, 200), new Point(3000, 200), new
		 * Point(2800, 200), new Point(2700, 200), new Point(3080, 200), new Point(3100,
		 * 200),new Point(943,165),new Point(1889,105) ,new Point(2313,195) };
		 * 
		 * for (Point p : points) {
		 * world.spawn_entity(Spawner.ARACHNIK,p.x,p.y,true,false); }
		 */

	}

	@Override
	public void init() {
		super.init();
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		tileMap = new TileMap(30);
		tileMap.loadTiles("/TileSets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		//tileMap.setPosition(0, 0);
		tileMap.setTween(0.07f);
		World.getInstance().start(tileMap);
		populateMap();
		bgmusic = new AudioPlayer("/Music/level1-1.mp3");
		bgmusic.play();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gsm.session.SendCommand(new WorldPacket(WorldPacket.LOGIN,null));
				
		//gsm.session.SendCommand(new WorldPacket(WorldPacket.REQUEST_HANDLE,
		//		new NetworkSpawner(0, Spawner.PLAYERPED, 200, 200, true, true)));
	}

	@Override
	public void update() {
		super.update();
		//dispatchCommands();
		dispatchWorldPackets();
		World.getInstance().update();
		handleInput();
		bg.setPosition(tileMap.getx(), tileMap.gety());
	}

	public void dispatchCommands() {
		synchronized (gsm.session.commandsPackets) {
			while (!gsm.session.commandsPackets.isEmpty()) {
				WorldPacket packet = gsm.session.commandsPackets.poll();
				switch (packet.packet_code) {
				case WorldPacket.HANDLE:
					playerhandle = (int) packet.data;
					break;
				case WorldPacket.SPAWN:
				
					NetworkSpawner sp = (NetworkSpawner) packet.data;
					if (requested_spawns_from_server.containsKey(sp.handle))
					{
						requested_spawns_from_server.remove(sp.handle);
					}
					if (playerhandle == sp.handle)
					{
						World.getInstance().request_spawn(gsm.session, true, sp.handle, sp.type, sp.x, sp.y, sp.facing, false);
					}
						else
							World.getInstance().request_spawn(gsm.session, false, sp.handle, sp.type, sp.x, sp.y, sp.facing, sp.network);

					break;
				case WorldPacket.SPEECH:
					SpeechPacket data = (SpeechPacket) packet.data;
					World.getInstance().entities.get(data.handle).say(data.text);
					break;
				case WorldPacket.DESPAWN:
					World.getInstance().request_despawn((int) packet.data);
					break;
				case WorldPacket.UDP_PORT:
					gsm.session.startUDP((int) packet.data);
					break;
				default:
					LOGGER.log(Level.WARNING,"Unknown CommandPacket code: " + packet.packet_code, this);
					break;
				}
			}
		}
	}

	public void dispatchWorldPackets() {
		// dispatch worldpackets
		synchronized (gsm.session.worldPackets) {
			while (!gsm.session.worldPackets.isEmpty()) {
				MovementData packet = gsm.session.worldPackets.pop();
				if (World.getInstance().entities.containsKey(packet.handle)) {
					LOGGER.debug( packet.handle + " x: " + packet.x, this);
						World.getInstance().entities.get(packet.handle).updatePacket(packet, World.getInstance());
					
				} else {
					if (!requested_spawns_from_server.containsKey(packet.handle)) {
						//gsm.session.SendCommand(new WorldPacket(WorldPacket.REQUEST_SPAWN, packet.handle));
						//requested_spawns_from_server.put(packet.handle,packet.handle);
						//LOGGER.log(Level.INFO,"requesting handle: " + packet.handle, this);
					}
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		tileMap.draw(g);
		World.getInstance().draw(g);

		// if (player.getPlayerPed().isDead()) {
		// g.setColor(Color.yellow);
		// g.setFont(new Font("Arial", Font.BOLD, 30));
		// g.drawString("GAME OVER", Gameplay.WIDTH / 2 - 80, Gameplay.HEIGHT / 2);
		// }
		// player.draw(g);
		// draw hud should be last
	}

	protected void finalize() {
		bgmusic.close();
		LOGGER.log(Level.INFO,"closing onlinestate", this);
		gsm.session.disconnect();
	}

	@Override
	public void handleInput() {
		super.handleInput();
		if (CONTROLS.isPressed(CONTROLS.ESCAPE)) {
			gsm.requestState(GameStateManager.LOGINSTATE);
			gsm.session.disconnect();

		}
	}
}
