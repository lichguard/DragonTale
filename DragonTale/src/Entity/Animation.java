package Entity;

import java.awt.image.BufferedImage;


public class Animation  {
	
	private BufferedImage[] frames;
	private int numofframes;
	private int currentFrame;
	
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
