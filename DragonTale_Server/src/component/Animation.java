package component;

import java.awt.image.BufferedImage;

import entity.Assets;

public class Animation implements component.IComponent {
	
	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int JUMPING = 2;
	public static final int FALLING = 3;
	public static final int GLIDING = 4;
	public static final int ATTACK1 = 5;
	public static final int ATTACK2 = 6;
	public static final int DEAD = 7;

	public static final int ANIMATION_COUNT = 8;
	
	
	public boolean facingRight;
	public int[] numFrames;
	public BufferedImage[][] sprites;
	public BufferedImage[] frames;
	public int widthArr[];
	public int heightArr[];
	public int delayArr[];
	public int numofframes;
	public int currentFrame;
	public int height;
	public int width;

	public long startTime;
	public long delay;
	public float animationSpeed;
	public boolean playedonce;
	public int texture;
	public int currentPlayingAction;
	
	public Animation(int texture, int startingAction) {
		animationSpeed = 1.0f;
		this.texture = texture;
		loadAssets(texture);
		setAnimation(startingAction);
	}
	
	public void setAnimation(int currentPlayingAction) {
		this.currentPlayingAction = currentPlayingAction;
		this.frames = sprites[currentPlayingAction];
		this.numofframes = numFrames[currentPlayingAction];
		this.delay = delayArr[currentPlayingAction];
		this.width = widthArr[currentPlayingAction];
		this.height = heightArr[currentPlayingAction];

		currentFrame = 0;
		startTime = System.nanoTime();
		playedonce = false;
	}
	
	public void loadAssets(int name) {
		Object[] animationAssets = Assets.requestAnimation(name);

		this.numFrames = (int[]) animationAssets[0];
		this.sprites = (BufferedImage[][]) animationAssets[1];
		this.widthArr = (int[]) animationAssets[2];
		this.heightArr = (int[]) animationAssets[3];
		this.delayArr = (int[]) animationAssets[4];
	}
	

	public int getFrame() {
		return currentFrame;
	}

	public BufferedImage getImage() {

		return frames[currentFrame];
	}

	public boolean hasPlayedOnce() {
		return playedonce;
	}
	

	public void setAnimationSpeed(float animationSpeed) {
		this.animationSpeed = animationSpeed;
	}

	public int getCurrenPlayingAnimation() {
		return currentPlayingAction;
	}
	

}
