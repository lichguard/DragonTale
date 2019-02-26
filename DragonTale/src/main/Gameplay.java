package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import gamestate.GameStateManager;
import main.LOGGER;

public class Gameplay extends JPanel implements Runnable, KeyListener,MouseListener {

	private static final long serialVersionUID = 1L;
	private Thread thread;
	private boolean running;
	private int target_FPS = 60;
	public static long FPS = 1;
	private long targetTime = 1000 / target_FPS;
	private BufferedImage image;
	private Graphics2D g;
	public Gameplay() {
		super();
		setPreferredSize(new Dimension(GameConstants.WIDTH * GameConstants.SCALE, GameConstants.HEIGHT * GameConstants.SCALE));
		setFocusable(true);
		requestFocus();
		setFocusTraversalKeysEnabled(false); 
	}

	private void init() {
		LOGGER.info("Starting up Gameplay @" + target_FPS + " fps", this);
		image = new BufferedImage(GameConstants.WIDTH, GameConstants.HEIGHT, BufferedImage.TYPE_INT_BGR);
		g = (Graphics2D) image.getGraphics();
		running = true;
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		
		init();
		long start;
		long elapsed;
		while (running) {
			start = System.nanoTime();
			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;
			//FPS is how long it takes to calculate a frame in milliseconds
			FPS = elapsed / 1000000;
			try {
				Thread.sleep(Math.max(targetTime - FPS,0));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void update() {
		GameStateManager.getInstance().update();// gsm.update();
		CONTROLS.update();  
	}

	public void draw() {
		GameStateManager.getInstance().draw(g); //gsm.draw(g);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		CONTROLS.Mouseset(e,true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		CONTROLS.Mouseset(e,false);
	}


}
