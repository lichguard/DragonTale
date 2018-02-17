package entity;

import java.awt.image.BufferedImage;


public class Animation  {
	
	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int JUMPING = 2;
	public static final int FALLING = 3;
	public static final int GLIDING = 4;
	public static final int ATTACK1 = 5;
	public static final int ATTACK2 = 6;
	
	private BufferedImage[] frames;
	private int numofframes;
	private int currentFrame;
	
	public int height;
	public int width;
	
	private long startTime;
	private long delay;
	
	private boolean playedonce;
	
	public Animation() {
		playedonce = false;
	}
	
	public void setFrames(BufferedImage[] frames, int numofframes)
	{
		this.frames = frames;
		this.numofframes = numofframes;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedonce = false;
	}
	
	public void setDelay(long d) {delay = d;}
	public void setFrame(int i) {currentFrame = 1;}
	
	public void update()
	{
		if (delay == -1) return;
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) 
		{
			currentFrame++;
			startTime = System.nanoTime();
		}
		if (currentFrame == numofframes)
		{
			currentFrame = 0;
			playedonce = true;
		}
	}
	
	public int getFrame() {return currentFrame;}
	public BufferedImage getImage() {
		
		return frames[currentFrame];
		
	}
	public boolean hasPlayedOnce() {return playedonce;}
	

}
