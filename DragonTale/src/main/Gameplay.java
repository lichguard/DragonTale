package main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import gamestate.GameStateManager;
import main.LOGGER;
import network.Session;

public class Gameplay extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private Thread thread;
	private boolean running;
	private Controls controls;
	
	private long targetFPS = 120;
	private static long FPS = 1;
	private long targetFPSTime = 1000 / targetFPS;

	private long targetUPS = 60;
	private long UPS = 1;
	private long targetUPSTime = 1000 / targetUPS;
	
	public Gameplay() {
		super();
		setPreferredSize(new Dimension(GameConstants.WIDTH, GameConstants.HEIGHT));
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.controls = new Controls();
		addKeyListener(controls);
		addMouseListener(controls);
	}

	public synchronized void start() {
		if (running) {
			return;
		}
		LOGGER.info("Starting up Gameplay @" + targetFPS + " fps", this);
		LOGGER.info("Starting up Gameplay @" + targetUPS + " ups", this);
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
		Session.getInstance().disconnect("exiting the game");
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
		GameStateManager.getInstance().draw(g);
		bs.show();
	}

	public void update() {
		GameStateManager.getInstance().update();
		Controls.update();
	}

	public static long getFPS() {
		return (1000 / FPS);
	}
	
	public long getUPS() {
		return this.UPS;
	}
}
 