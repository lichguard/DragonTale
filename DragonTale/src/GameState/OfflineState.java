package GameState;

import TileMap.*;
import main.World;

import java.awt.Graphics2D;
import Audio.AudioPlayer;


public class OfflineState extends GameState {

	private TileMap tileMap;
	private Background bg;
	private AudioPlayer bgmusic;
	//private Quadtree qt;
	public World world;
	public OfflineState(GameStateManager gsm) {
		super(gsm);
	}

	private void populateMap() {
		/*
		//new Point(200, 50)
		Point[] points = new Point[] { new Point(860, 195), new Point(1525, 195),
				new Point(1680, 195), new Point(1800, 195),new Point(250, 50),new Point(1205,65),new Point(2353,190) };

		//for (Point p : points) {
			//world.request_spawn(Spawner.SLUGGER, p.x,p.y,true,false);
		//}

		points = new Point[] { new Point(1020, 200), new Point(3000, 200), new Point(2800, 200), new Point(2700, 200),
				new Point(3080, 200), new Point(3100, 200),new Point(943,165),new Point(1889,105) ,new Point(2313,195) };

		//for (Point p : points) {
			//world.spawn_entity(Spawner.ARACHNIK,p.x,p.y,true,false);
		//}
		 */

	}

	@Override
	public void init() {
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		tileMap = new TileMap(30);
		tileMap.loadTiles("/TileSets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07f);
		world = new World(tileMap);
		populateMap();
		bgmusic = new AudioPlayer("/Music/level1-1.mp3");
		bgmusic.play();
	}

	@Override
	public void update() {
		handleInput();
		world.update();
		bg.setPosition(tileMap.getx(), tileMap.gety());
	}


	@Override
	public void draw(Graphics2D g) {

		bg.draw(g);
		tileMap.draw(g);
		world.draw(g);
		
		//if (player.getPlayerPed().isDead()) {
		//	g.setColor(Color.yellow);
		//	g.setFont(new Font("Arial", Font.BOLD, 30));
		//	g.drawString("GAME OVER", Gameplay.WIDTH / 2 - 80, Gameplay.HEIGHT / 2);
		//}
		//player.draw(g);
		// draw hud should be last
	}


	protected void finalize() {
		bgmusic.close();
		// System.out.println("Destroyed");
	}

	@Override
	public void handleInput() {

			gsm.requestState(GameStateManager.MAINMENUSTATE);
		}

}
