package entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;


public class Animation  {
	
	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int JUMPING = 2;
	public static final int FALLING = 3;
	public static final int GLIDING = 4;
	public static final int ATTACK1 = 5;
	public static final int ATTACK2 = 6;
	
	public int[] numFrames = { 2, 8, 1, 2, 4, 2, 5, 0, 0, 0 };
	public BufferedImage[][] sprites;
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
	
	public void load_entity(int type)
	{
		switch (type) {
		case Spawner.PLAYERPED:
			width = 30;
			height = 30;
			numFrames[Animation.IDLE] = 2;
			numFrames[Animation.WALKING] = 8;
			numFrames[Animation.JUMPING] = 1;
			numFrames[Animation.FALLING] = 2;
			numFrames[Animation.GLIDING] = 4;
			numFrames[Animation.ATTACK1] = 2;
			numFrames[Animation.ATTACK2] = 4;
			try {
				BufferedImage spritesheet = ImageIO
						.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));

				for (int i = 0; i < 7; i++) {
					// BufferedImage[] bi = new BufferedImage[numFrames[i]];
					for (int j = 0; j < numFrames[i]; j++) {

						if (i == 6)
							sprites[i][j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
						else
							sprites[i][j] = spritesheet.getSubimage(j * width, i * height, width, height);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			this.setFrames(sprites[Animation.IDLE], numFrames[Animation.IDLE]);
			break;

		default:
			break;
		}
		
		
		
		
		
	}

}
