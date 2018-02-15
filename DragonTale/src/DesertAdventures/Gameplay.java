package DesertAdventures;

import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;

import javax.swing.JPanel;
import GameState.GameStateManager;
import DesertAdventures.LOGGER;

public class Gameplay extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 320; // 320
	public static final int HEIGHT = 268; // 268

	public static final int SCALE = 900;
	public static final boolean DEBUG = true;
	public static final boolean DISABLEESOUND = true;
	public GameStateManager gsm;
	private Thread thread;
	private boolean running;
	private int target_FPS = 60;
	public static long FPS = 0;
	private long targetTime = 1000 / target_FPS;
	private BufferedImage image;
	private Graphics2D g;
	public Gameplay() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}

	private void init() {
		gsm = new GameStateManager();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
		g = (Graphics2D) image.getGraphics();
		running = true;
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		LOGGER.log(Level.INFO, "Starting up Gameplay", this);
		init();
		long start;
		long elapsed;
		long wait;
		while (running) {
			start = System.nanoTime();
			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;
			FPS = elapsed / 1000000;
			wait = Math.max(targetTime - FPS,5);
			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void update() {
		gsm.update();
		CONTROLS.update();
	}

	public void draw() {
		gsm.draw(g);
	}

	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	@Override
	public void keyPressed(KeyEvent key) {
		CONTROLS.keySet(key, key.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent key) {
		CONTROLS.keySet(key, key.getKeyCode(), false);

	}

	@Override
	public void keyTyped(KeyEvent key) {
		
	}


}
