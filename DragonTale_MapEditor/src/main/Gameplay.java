package main;

import java.awt.*;
import java.awt.image.BufferStrategy;

import TileMap.Background;
import TileMap.TileMap;


public class Gameplay extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private Thread thread;
	private boolean running;

	
	private long targetFPS = 120;
	private static long FPS = 1;
	private long targetFPSTime = 1000 / targetFPS;

	private long targetUPS = 60;
	private long UPS = 1;
	private long targetUPSTime = 1000 / targetUPS;
	private TileMap tileMap;
	private Background bg;
	private Controls controls;
	
	public Gameplay() {
		super();
		setPreferredSize(new Dimension(GameConstants.WIDTH, GameConstants.HEIGHT));
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.controls = new Controls();
		addKeyListener(controls);
		addMouseListener(controls);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		//bg.setVector(-0.1, 0);
		tileMap = new TileMap(GameConstants.TILESIZE);
		tileMap.loadTiles("/TileSets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		// tileMap.setPosition(0, 0);
		tileMap.setCameraFocusSpeed(0.1f);
	}

	public synchronized void start() {
		if (running) {
			return;
		}

		running = true;
		thread = new Thread(this);
		thread.start();

	}
	
	public synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("CLOSING");
	}

	@Override
	public void run() {
		long startFPS = System.nanoTime();
		long startUPS = System.nanoTime();
		requestFocus();
			
		while (running) {
			
			// FPS is how long it takes to calculate a frame in milliseconds
			UPS = (System.nanoTime() - startUPS) / 1_000_000l;
			FPS = (System.nanoTime() - startFPS) / 1_000_000l;
			
			if (UPS >= targetUPSTime) {
				update();
				startUPS = System.nanoTime();
			}
			if (FPS >= targetFPSTime) {
				render();
				startFPS = System.nanoTime();
			}	
			

			try {
				long wait = Math.min(targetUPSTime - UPS, targetFPSTime - FPS);
				if (wait > 0)
					Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void render() {

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		bg.draw(g);
		tileMap.draw(g);
		
		bs.show();
	}

	float ax = 0;
	float ay = 0;
	boolean releaseing = false;
	int blocktype = 21;
	public void update() {
		
		//tileMap.setPosition(-50, 100);

		if (Controls.isPressed(Controls.GLIDE)) {
			tileMap.exportMap();
		}
		
		if (Controls.isPressed(Controls.SCRATCH)) {
			Controls.mousey = MouseInfo.getPointerInfo().getLocation().y-this.getLocationOnScreen().y;
			Controls.mousex = MouseInfo.getPointerInfo().getLocation().x-this.getLocationOnScreen().x;
			blocktype = tileMap.getBlockType(Controls.mousex,Controls.mousey);
		}
		
		if (Controls.isPressed(Controls.RMB)) {
			int y = MouseInfo.getPointerInfo().getLocation().y-this.getLocationOnScreen().y;
			int x = MouseInfo.getPointerInfo().getLocation().x-this.getLocationOnScreen().x;
			
			tileMap.setPosition(tileMap.x + (- Controls.mousex + x)*5, tileMap.y + ( -Controls.mousey + y)*5);
			Controls.mousey = y;
			Controls.mousex = x;
			releaseing = true;
		
		} else {
			if (releaseing ) {
				releaseing = false;
				int y = MouseInfo.getPointerInfo().getLocation().y-this.getLocationOnScreen().y;
				int x = MouseInfo.getPointerInfo().getLocation().x-this.getLocationOnScreen().x;
				ax = (- Controls.mousex + x)*5;
				ay = (- Controls.mousey + y)*5;
			}
		}
		if  (ax != 0 && ay != 0) {
			tileMap.setPosition(tileMap.x + ax, tileMap.y + ay);
			if (ax > 0)
				ax -= ax * 0.05f; 
			else
				ax +=  ax * -0.05f; 
			
			if (ay > 0)
				ay -= ay * 0.05f; 
			else
				ay +=  ay * 0.05f; 
		}
		
		
		if (Controls.isPressed(Controls.LMB)) {
			Controls.mousey = MouseInfo.getPointerInfo().getLocation().y-this.getLocationOnScreen().y;
			Controls.mousex = MouseInfo.getPointerInfo().getLocation().x-this.getLocationOnScreen().x;
			//System.out.println((Controls.mousey / tileMap.getTileSize())  + "," + (Controls.mousex / tileMap.getTileSize()));
			//tileMap.map[Controls.mousey / tileMap.getTileSize()][Controls.mousex / tileMap.getTileSize()] = 22;
			tileMap.setTileTo(Controls.mousex,Controls.mousey,blocktype);
		
		}
			
		bg.update();
	}

	public static long getFPS() {
		return (1000 / FPS);
	}
	
	public long getUPS() {
		return this.UPS;
	}
}
 