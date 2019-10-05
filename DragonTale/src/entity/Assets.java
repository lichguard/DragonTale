package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import component.Animation;
import main.LOGGER;

public class Assets {

	private static Map<Integer, Object[]> animationsCache = new HashMap<>();

	private static void setSprites(int action, int frames, int width, int height, int delay,String imageResourceName,int y,BufferedImage[][] sprites,int[] numFrames, int[] widthArr,int[] heightArr,int[] delayArr) throws IOException {
		widthArr[action] = width;
		heightArr[action] = height;
		delayArr[action] = delay;
		numFrames[action] = frames;

		BufferedImage spritesheet = ImageIO.read(Animation.class.getResourceAsStream(imageResourceName));
		for (int i = 0; i < numFrames[action]; i++) {
			sprites[action][i] = spritesheet.getSubimage(i * width, y, width, height);
		}
	}

	public static Object[] requestAnimation(int name) {
		Object[] animationAsset = animationsCache.get(name);
		if (animationAsset != null)
			return animationAsset;

		int[] numFrames = new int[Animation.ANIMATION_COUNT];
		BufferedImage[][] sprites = new BufferedImage[Animation.ANIMATION_COUNT][Animation.ANIMATION_COUNT];
		int[] width = new int[Animation.ANIMATION_COUNT];
		int[] height = new int[Animation.ANIMATION_COUNT];
		int[] delay = new int[Animation.ANIMATION_COUNT];
		try {
			switch (name) {
			case Spawner.COIN: //"coin":
				setSprites(Animation.IDLE, 6,20,30,220,"/Sprites/Player/coin.png",0,sprites,numFrames,width,height,delay);
				setSprites(Animation.FALLING, 6,20,30,220,"/Sprites/Player/coin.png",0,sprites,numFrames,width,height,delay);
				
				break;
			case Spawner.EXPLOSION: //"explosion":
				setSprites(Animation.IDLE, 6,30,30,50,"/Sprites/Enemies/explosion.gif",0,sprites,numFrames,width,height,delay);

			case Spawner.FIREBALL: //"fireball":
				//not really idle
				setSprites(Animation.IDLE, 4,30,30,40,"/Sprites/Player/fireball.gif",0,sprites,numFrames,width,height,delay);
				setSprites(Animation.WALKING, 4,30,30,40,"/Sprites/Player/fireball.gif",0,sprites,numFrames,width,height,delay);
				setSprites(Animation.DEAD, 4,30,30,40,"/Sprites/Player/fireball.gif",30,sprites,numFrames,width,height,delay);
				
				break;
			case Spawner.PLAYERPED: //"playerped":
				setSprites(Animation.IDLE, 2,30,30,150,"/Sprites/Player/playersprites.gif",0,sprites,numFrames,width,height,delay);
				setSprites(Animation.WALKING, 8,30,30,40,"/Sprites/Player/playersprites.gif",30,sprites,numFrames,width,height,delay);
				setSprites(Animation.JUMPING, 1,30,30,-1,"/Sprites/Player/playersprites.gif",60,sprites,numFrames,width,height,delay);
				setSprites(Animation.FALLING, 2,30,30,100,"/Sprites/Player/playersprites.gif",90,sprites,numFrames,width,height,delay);
				setSprites(Animation.GLIDING, 4,30,30,100,"/Sprites/Player/playersprites.gif",120,sprites,numFrames,width,height,delay);
				setSprites(Animation.ATTACK2, 2,30,30,60,"/Sprites/Player/playersprites.gif",150,sprites,numFrames,width,height,delay);
				setSprites(Animation.ATTACK1, 4,60,30,50,"/Sprites/Player/playersprites.gif",180,sprites,numFrames,width,height,delay);
				break;
			case Spawner.ARACHNIK: //"arachnik":
				//not really idle
				setSprites(Animation.IDLE, 1,30,30,40,"/Sprites/Enemies/pgmy_spirit.png",0,sprites,numFrames,width,height,delay);
				setSprites(Animation.WALKING, 3,30,30,40,"/Sprites/Enemies/pgmy_spirit.png",0,sprites,numFrames,width,height,delay);
				break;
			case Spawner.SLUGGER: //"slugger":
				//not really idle
				setSprites(Animation.IDLE, 1,30,30,40,"/Sprites/Enemies/slugger.gif",0,sprites,numFrames,width,height,delay);
				setSprites(Animation.WALKING, 3,30,30,40,"/Sprites/Enemies/slugger.gif",0,sprites,numFrames,width,height,delay);
				setSprites(Animation.DEAD, 1,30,30,40,"/Sprites/Enemies/dead_slugger.gif",30,sprites,numFrames,width,height,delay);
				
				break;
			default:
	
				throw new Exception("Add default texture!!");
				
				//should be set to default
				//setSprites(Animation.IDLE, 1,30,30,40,"/Sprites/Enemies/slugger.gif",0,sprites,numFrames,width,height,delay);
				//break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				
			} catch (Exception e2) {
				LOGGER.error("Add default texture!!", null);

				
			}
		}

		Object[] newanimationAsset = { numFrames, sprites,width,height,delay };
		animationsCache.put(name, newanimationAsset);
		return newanimationAsset;
	}

}
