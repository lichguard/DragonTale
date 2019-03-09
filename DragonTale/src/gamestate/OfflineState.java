package gamestate;
import java.awt.Graphics;
import TileMap.*;
import audio.AudioPlayer;
import entity.Spawner;
import main.Controls;
import main.GameConstants;
import main.LOGGER;
import main.World;

import java.awt.Graphics2D;
import java.util.logging.Level;

public class OfflineState extends GameState {

	private TileMap tileMap;
	private Background bg;
	private AudioPlayer bgmusic;
	// private Quadtree qt;



	private void populateMap() {
		/*
		 * //new Point(200, 50) Point[] points = new Point[] { new Point(860, 195), new
		 * Point(1525, 195), new Point(1680, 195), new Point(1800, 195),new Point(250,
		 * 50),new Point(1205,65),new Point(2353,190) };
		 * 
		 * //for (Point p : points) { //world.request_spawn(Spawner.SLUGGER,
		 * p.x,p.y,true,false); //}
		 * 
		 * points = new Point[] { new Point(1020, 200), new Point(3000, 200), new
		 * Point(2800, 200), new Point(2700, 200), new Point(3080, 200), new Point(3100,
		 * 200),new Point(943,165),new Point(1889,105) ,new Point(2313,195) };
		 * 
		 * //for (Point p : points) {
		 * //world.spawn_entity(Spawner.ARACHNIK,p.x,p.y,true,false); //}
		 */

	}

	@Override
	public void init(String requestedStateInitMessage) {
		super.init(requestedStateInitMessage);
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		tileMap = new TileMap(GameConstants.TILESIZE);
		tileMap.loadTiles("/TileSets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setCameraFocusSpeed(0.07f);
		World.getInstance().start(tileMap);
		populateMap();
		bgmusic = new AudioPlayer("/Music/level1-1.mp3");
		bgmusic.play();
		World.getInstance().request_spawn("0", true, 0, Spawner.PLAYERPED, 200, 200, true, 0);

	}

	@Override
	public void update() {
		handleInput();
		World.getInstance().update();
		bg.setPosition(tileMap.getx(), tileMap.gety());
	}

	@Override
	public void draw(Graphics g) {

		bg.draw(g);
		tileMap.draw(g);
		World.getInstance().draw(g);
	}

	protected void finalize() {
		bgmusic.close();
		
		LOGGER.log(Level.INFO,"Destroyed", this);
	}

	@Override
	public void handleInput() {

		if (Controls.isPressed(Controls.ESCAPE)) {
			GameStateManager.getInstance().requestState(GameStateManager.MAINMENUSTATE,"");

		}
	}

}
