package game;

import java.awt.Point;

import network.Listener;
import objects.Spawner;
import vmaps.TileMap;

public class Gameplay implements Runnable {

	public static final int WIDTH = 320; // 320
	public static final int HEIGHT = 268; // 268

	public static final int SCALE = 900;
	public static final boolean DEBUG = true;
	public static final boolean DISABLEESOUND = true;
	public Thread thread;
	public boolean running;
	private int target_FPS = 60;
	public static long FPS = 0;
	private long targetTime = 1000 / target_FPS;
	public TileMap tileMap;
	public long lastbroadcast = 0;

	public Gameplay() {
	}

	private void init() {
		running = true;
		thread = Thread.currentThread();
		tileMap = new TileMap(30);
		tileMap.loadTiles("/TileSets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07);
		World.getInstance().startWorld(tileMap);
		Listener.getInstance().start();

		Point[] points = new Point[] { new Point(100, 20) };

		for (Point p : points) {
			World.getInstance().requestObjectSpawn(Spawner.SLUGGER, p.x, p.y, true, false, null);
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
		try {
			while (running && !Thread.currentThread().isInterrupted()) {
				start = System.nanoTime();
				update();

				elapsed = System.nanoTime() - start;
				FPS = elapsed / 1000000;
				wait = Math.max(targetTime - FPS, 5);
				Thread.sleep(wait);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Listener.getInstance().shutdown();
		World.getInstance().shutdown();

	}

	public void update() {
		World.getInstance().update();
	}

}
