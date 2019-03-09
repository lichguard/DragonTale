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
	private int target_FPS = 60;
	public static long FPS = 1;
	private long targetTime = 1000 / target_FPS;
	private Controls controls;

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
		LOGGER.info("Starting up Gameplay @" + target_FPS + " fps", this);
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

		long start;
		long elapsed;
		requestFocus();
		while (running) {
			start = System.nanoTime();
			update();
			render();
			elapsed = System.nanoTime() - start;
			
			// FPS is how long it takes to calculate a frame in milliseconds
			FPS = elapsed / 1_000_000l;
			try {
				Thread.sleep(Math.max(targetTime - FPS, 0));
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

}
