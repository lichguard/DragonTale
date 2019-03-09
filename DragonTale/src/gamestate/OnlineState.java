package gamestate;

import TileMap.*;
import audio.AudioPlayer;
import componentNew.EntityManager;
import main.CONTROLS;
import main.GameConstants;
import main.LOGGER;
import main.World;
import network.Session;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.logging.Level;

import PACKET.MovementData;
import PACKET.NetworkSpawner;
import PACKET.SpeechPacket;
import PACKET.WorldPacket;
import java.awt.Graphics;
public class OnlineState extends GameState {

	private AudioPlayer bgmusic;
	private TileMap tileMap;
	private Background bg;
	
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
	public void init(String requestedStateInitMessage) {
		super.init(requestedStateInitMessage);
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		tileMap = new TileMap(GameConstants.TILESIZE);
		tileMap.loadTiles("/TileSets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		// tileMap.setPosition(0, 0);
		tileMap.setCameraFocusSpeed(0.07f);
		World.getInstance().start(tileMap);
		populateMap();
		bgmusic = new AudioPlayer("/Music/level1-1.mp3");
		bgmusic.play();

		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		Session.getInstance().SendCommand(new WorldPacket(WorldPacket.LOGIN, null));

		// gsm.session.SendCommand(new WorldPacket(WorldPacket.REQUEST_HANDLE,
		// new NetworkSpawner(0, Spawner.PLAYERPED, 200, 200, true, true)));
	}

	@Override
	public void update() {
		super.update();
		dispatchCommands();
		dispatchWorldPackets();
		World.getInstance().update();
		handleInput();
		bg.setPosition(tileMap.getx(), tileMap.gety());
	}

	public void dispatchCommands() {
		synchronized (Session.getInstance().commandsPackets) {
			while (!Session.getInstance().commandsPackets.isEmpty()) {
				WorldPacket packet = Session.getInstance().commandsPackets.poll();
				switch (packet.packet_code) {
				case WorldPacket.SPAWN:
					NetworkSpawner sp = (NetworkSpawner) packet.data;
					World.getInstance().request_spawn(sp.name, false, sp.handle, sp.type, sp.x, sp.y, sp.facing,
							sp.network);
					break;
				case WorldPacket.DESPAWN:
					World.getInstance().request_despawn((int) packet.data);
					break;
				case WorldPacket.SPEECH:
					SpeechPacket data = (SpeechPacket) packet.data;
					EntityManager.getInstance().say(data.handle, data.text);
					break;
				case WorldPacket.DIE:
					EntityManager.getInstance().die((int) packet.data);
					break;
				case WorldPacket.LOGIN:
					NetworkSpawner spwaner = (NetworkSpawner) packet.data;
					World.getInstance().request_spawn(spwaner.name, true, spwaner.handle, spwaner.type, spwaner.x,
							spwaner.y, spwaner.facing, spwaner.network);
					break;
				case WorldPacket.UDP_PORT:
					try {
						Session.getInstance().startUDP((int) packet.data);
					} catch (IOException e) {
						e.printStackTrace();
						Session.getInstance().disconnect("Error establishing connection");
					}
					break;
				default:
					LOGGER.log(Level.WARNING, "Unknown CommandPacket code: " + packet.packet_code, this);
					break;
				}
			}
		}
	}

	public void dispatchWorldPackets() {
		// dispatch worldpackets
		synchronized (Session.getInstance().worldPackets) {
			while (!Session.getInstance().worldPackets.isEmpty()) {
				MovementData packet = Session.getInstance().worldPackets.pop();
				// if (World.getInstance().entities.containsKey(packet.handle)) {
				// World.getInstance().entities.get(packet.handle).updatePacket(packet,
				// World.getInstance());

				// }
				EntityManager.getInstance().updatePacket(packet.handle, packet);
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		bg.draw(g);
		tileMap.draw(g);
		World.getInstance().draw(g);

		// if (player.getPlayerPed().isDead()) {
		// g.setColor(Color.yellow);
		// g.setFont(new Font("Arial", Font.BOLD, 30));
		// g.drawString("GAME OVER", GameConstants.WIDTH / 2 - 80, GameConstants.HEIGHT
		// / 2);
		// }
		// player.draw(g);
		// draw hud should be last
	}

	protected void finalize() {
		LOGGER.log(Level.INFO, "onlinestate was destroyed by java GC", this);
	}

	@Override
	public void handleInput() {
		super.handleInput();
		if (CONTROLS.isPressed(CONTROLS.ESCAPE)) {
			GameStateManager.getInstance().requestState(GameStateManager.LOGINSTATE, "");
		}
	}
	public void destroy() {
		super.destroy();
		Session.getInstance().disconnect("");
		World.getInstance().destroy();
		bgmusic.close();
		
	}
}
