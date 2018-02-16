package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Network.Session;

import PACKET.WorldPacket;
import TileMap.Tile;
import TileMap.TileMap;
import component.AnimationComponent;
import component.BroadCastEntityComponent;
import component.ControlsComponent;
import component.IComponent;
import component.NetworkComponent;
import component.PhysicsComponent;
import drawcomponenets.DrawChatBoxComponenet;
import drawcomponenets.DrawEntityComponent;
import drawcomponenets.DrawHudComponent;
import drawcomponenets.DrawNameComponent;
import drawcomponenets.DrawNamePlateComponent;
import drawcomponenets.DrawSpeechComponent;
import drawcomponenets.IRender;
import main.Gameplay;
import main.World;

public class ENTITY {

	public ArrayList<IComponent> components = new ArrayList<IComponent>();
	public ArrayList<IRender> renders = new ArrayList<IRender>();
	public TileMap tileMap;
	public int tileSize;
	public float xmap;
	public float ymap;
	public int handle;
	public boolean isNetowrkEntity = false;

	// position and vector
	public float x;
	public float y;
	public float dx;
	public float dy;

	// dimensions
	public int width;
	public int height;

	// collision voz
	public int cwidth;
	public int cheight;

	// collision
	public int health = 1;
	public int currRow;
	public int currCol;
	public float xdest;
	public float ydest;
	public float xtemp;
	public float ytemp;
	public boolean topLeft;
	public boolean topRight;
	public boolean bottomLeft;
	public boolean bottomRight;

	// animation
	public Animation animation;
	public int currentAction;
	public int previousAction;
	public boolean facingRight;

	// movmeent
	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;
	public boolean falling;

	// movment attributes
	public float moveSpeed;
	public float maxSpeed;
	public float stopSpeed;
	public float fallSpeed;
	public float maxFallSpeed;
	public boolean removeEntity;
	public int affiliation;

	public WorldPacket entity_packet = new WorldPacket();
	public WorldPacket last_packet = new WorldPacket();
	public WorldPacket new_packet = new WorldPacket();
	public long ping = 200;
	public long interpolation_start = 0;

	public BufferedImage[][] sprites;
	public HashMap<String, AudioPlayer> sfx;
	public String name = "";

	public int[] numFrames = { 2, 8, 1, 2, 4, 2, 5, 0, 0, 0 };
	public int walkingframes;

	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int JUMPING = 2;
	public static final int FALLING = 3;
	public static final int GLIDING = 4;
	public static final int ATTACK1 = 5;
	public static final int ATTACK2 = 6;

	public boolean flinching;
	public long filinchTimer;
	public int maxHealth;
	public float maxGlideSpeed;
	public boolean gliding;
	public boolean jumping;
	public int doublejump = 2;
	public float jumpStart;
	public float stopJumpSpeed;
	public boolean firing;
	public boolean attack;
	public boolean scratching;
	public boolean sprint;
	public int coins = 0;

	public void setMaxSpeed(float value) {
		this.maxSpeed = value;
	}

	public void setCurrentAction(int newaction) {
		this.currentAction = newaction;
	}

	public void setPreviousAction(int oldaction) {
		this.previousAction = oldaction;
	}

	public int getCurrentAction() {
		return this.currentAction;
	}

	public int getPreviousAction() {
		return this.previousAction;
	}

	// constructor
	World world;

	int type;

	public ENTITY(int type, int handle, World world, Session session, boolean is_local_player, boolean is_network,
			float x, float y, boolean facing) {
		this.world = world;
		this.handle = handle;
		sprites = new BufferedImage[10][10];
		animation = new Animation();

		WorldPacket packet1 = new WorldPacket(handle, x, y, facing);
		WorldPacket packet2 = new WorldPacket(handle, x, y, facing);
		packet1.timeframe += ping;
		this.last_packet = packet1;
		this.type = type;
		this.new_packet = packet2;
		interpolation_start = packet2.timeframe;
		// setMapPosition();
		setPosition(x, y);
		setRight(facing);
		setLeft(!facing);
		setFacingright(facing);

		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;

		moveSpeed = 0.3f;
		maxSpeed = 1.6f;
		stopSpeed = 0.4f;
		fallSpeed = 0.15f;
		maxFallSpeed = 4.0f;
		jumpStart = -4.8f;
		stopJumpSpeed = 0.3f;
		doublejump = 0;
		jumpStart = -4.8f;
		health = maxHealth = 5;
		maxGlideSpeed = 1.2f;
		doublejump = 0;
		this.name = Integer.toString(handle);
		animation = new Animation();

		renders.add(new DrawNameComponent(world, this, session));
		if (is_local_player) {
			components.add(new ControlsComponent(world, this, session));
			components.add(new BroadCastEntityComponent(world, this, session));
			renders.add(new DrawHudComponent(world, this, session));
		} else {
			renders.add(new DrawNamePlateComponent(world, this, session));
		}

		if (is_network) {
			components.add(new NetworkComponent(world, this, session));

		} else {
			components.add(new PhysicsComponent(world, this, session));
			components.add(new AnimationComponent(world, this, session));
		}
		renders.add(new DrawEntityComponent(world, this, session));
		switch (type) {
		case Spawner.PLAYERPED:
			playerped();
			break;
		case Spawner.SLUGGER:
			slugger();
			break;
		case Spawner.ARACHNIK:
			arachnik();
			break;
		case Spawner.COIN:
			coin();
			break;
		case Spawner.EXPLOSION:
			explosion();
			break;
		case Spawner.FIREBALL:
			fireball();
			break;
		default:
			break;
		}
	}

	public void coin() {
		width = 20;
		height = 30;
		cwidth = 20;
		cheight = 20;
		stopSpeed = 0.05f;

		Random rand = new Random();
		dy = -3.0f * rand.nextFloat() - 1;
		dx = 3.0f * rand.nextFloat();

		if (rand.nextInt(2) == 0)
			dx *= -1.0f;

		// dy = -3.5;
		falling = true;
		numFrames[IDLE] = 6;
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/coin.png"));

			// sprites = new BufferedImage[6];

			for (int i = 0; i < numFrames[IDLE]; i++) {
				sprites[IDLE][i] = spritesheet.getSubimage(i * width, 0, width, height);
			}

			animation = new Animation();
			animation.setFrames(sprites[IDLE], numFrames[IDLE]);
			animation.setDelay(110);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void explosion() {
		width = 30;
		height = 30;
		numFrames[IDLE] = 6;
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/explosion.gif"));

			// sprites = new BufferedImage[6];

			for (int i = 0; i < numFrames[IDLE]; i++) {
				sprites[IDLE][i] = spritesheet.getSubimage(i * width, 0, width, height);
			}

			animation.setFrames(sprites[IDLE], numFrames[IDLE]);
			animation.setDelay(50);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireball() {
		moveSpeed = 3.8f;

		facingRight = right;
		dx = right ? moveSpeed : -moveSpeed;

		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		numFrames[WALKING] = 4;
		numFrames[ATTACK1] = 3;
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fireball.gif"));
			for (int i = 0; i < numFrames[WALKING]; i++) {
				sprites[WALKING][i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			for (int i = 0; i < numFrames[ATTACK1]; i++) {
				sprites[ATTACK1][i] = spritesheet.getSubimage(i * width, height, width, height);
			}

			animation.setFrames(sprites[WALKING], numFrames[WALKING]);
			animation.setDelay(70);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void playerped() {
		cwidth = 30;
		width = 30;
		height = 30;
		numFrames[IDLE] = 2;
		numFrames[WALKING] = 8;
		numFrames[JUMPING] = 1;
		numFrames[FALLING] = 2;
		numFrames[GLIDING] = 4;
		numFrames[ATTACK1] = 2;
		numFrames[ATTACK2] = 4;

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

		currentAction = IDLE;
		animation.setFrames(sprites[IDLE], numFrames[IDLE]);

		// sfx = new HashMap<String, AudioPlayer>();
		// sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		// sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
		// sfx.put("fireball", new AudioPlayer("/SFX/fireball.mp3"));

	}

	public void arachnik() {
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		health = maxHealth = 250;
		numFrames[WALKING] = 3;
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO
					.read(getClass().getResourceAsStream("/Sprites/Enemies/pgmy_spirit.png"));

			for (int i = 0; i < numFrames[WALKING]; i++) {
				sprites[WALKING][i] = spritesheet.getSubimage(i * width, 0, width, height);
			}

			animation.setFrames(sprites[WALKING], numFrames[WALKING]);
			animation.setDelay(100);

			right = true;
			facingRight = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void slugger() {
		width = 30;
		height = 30;
		cwidth = 30;
		cheight = 20;
		health = maxHealth = 100;
		numFrames[WALKING] = 3;
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/slugger.gif"));

			for (int i = 0; i < numFrames[WALKING]; i++) {
				sprites[WALKING][i] = spritesheet.getSubimage(i * width, 0, width, height);
			}

			animation = new Animation();
			animation.setFrames(sprites[WALKING], numFrames[WALKING]);
			animation.setDelay(300);

			right = true;
			facingRight = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setHandle(int handle) {
		this.handle = handle;
		name = Integer.toString(handle);
	}

	public boolean intersects(ENTITY e) {
		return this.getRecangle().intersects(e.getRecangle());
	}

	public Rectangle getRecangle() {
		return new Rectangle((int) x - cwidth, (int) y - cheight, cwidth, cheight);
	}

	public void setJumping(boolean b) {

		if (!b && doublejump == 0)
			doublejump = 1;

		if (b && doublejump == 1) {
			doublejump = 2;
			falling = false;
			dy = 0;
		}

		jumping = b;
	}

	public int getx() {
		return (int) x;
	}

	public int gety() {
		return (int) y;
	}

	public float getdx() {
		return dx;
	}

	public float getdy() {
		return dy;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCWidth() {
		return cwidth;
	}

	public int getCHeight() {
		return cheight;
	}

	public void setPosition(float x, float y) {

		this.x = x;
		this.y = y;
	}

	public void setVector(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public boolean isFacingright() {
		return facingRight;
	}

	public void setFacingright(boolean face) {
		facingRight = face;
	}

	public void setMapPosition() {
		xmap = world.tm.getx();
		ymap = world.tm.gety();
	}

	public void setLeft(boolean b) {
		left = b;
	}

	public void setRight(boolean b) {
		right = b;
	}

	public void setUp(boolean b) {
		up = b;
	}

	public void setDown(boolean b) {
		down = b;
	}

	public boolean notonScreen() {

		return Math.abs(x - (-xmap + Gameplay.WIDTH / 2)) > Gameplay.WIDTH / 0.75f
				|| Math.abs(y - (-ymap + Gameplay.HEIGHT / 2)) > Gameplay.HEIGHT / 0.75f;
	}

	public void update() {

		for (IComponent component : components) {
			component.update();
		}
	}

	public int gethandle() {
		return handle;
	}

	public void SetActionAnimation(int newaction) {
		switch (newaction) {
		case ATTACK2:
			// sfx.get("scratch").play();
			animation.setFrames(sprites[ATTACK2], numFrames[ATTACK2]);
			animation.setDelay(50);
			width = 60;
			break;
		case ATTACK1:
			// sfx.get("fireball").play();
			animation.setFrames(sprites[ATTACK1], numFrames[ATTACK1]);
			animation.setDelay(100);
			width = 30;
			break;
		case GLIDING:
			animation.setFrames(sprites[GLIDING], numFrames[GLIDING]);
			animation.setDelay(100);
			width = 30;
			break;
		case FALLING:
			animation.setFrames(sprites[FALLING], numFrames[FALLING]);
			animation.setDelay(100);
			width = 30;
			break;
		case JUMPING:
			animation.setFrames(sprites[JUMPING], numFrames[JUMPING]);
			animation.setDelay(-1);
			width = 30;
			break;
		case WALKING:
			animation.setFrames(sprites[WALKING], numFrames[WALKING]);
			animation.setDelay(40);
			width = 30;
			break;
		case IDLE:
			animation.setFrames(sprites[IDLE], numFrames[IDLE]);
			animation.setDelay(400);
			width = 30;
			break;

		}
	}

	public void updatePacket(WorldPacket p, World world) {

		if (new_packet.timeframe < p.timeframe) {
			last_packet.clone(new_packet);
			new_packet.clone(p);

			ping = (new_packet.timeframe - last_packet.timeframe + ping) / 2;

			if (ping <= 0 || ping > 1000)
				ping = 500;

			interpolation_start = System.currentTimeMillis();
			setFacingright(last_packet.facingRight);
			health = new_packet.health;
			if (last_packet.currentAction != this.currentAction) {
				SetActionAnimation(last_packet.currentAction);
				setCurrentAction(last_packet.currentAction);

				if (last_packet.currentAction == 5) {
					// setattack();
					// world.re spawn_entity(Spawner.FIREBALL, x, y, facingRight, false);
				} else if (last_packet.currentAction == 6) {
					// setattack2();
					// scratchAttack(world);

				}
			}
		}
	}

	public void say(String s) {
		this.renders.add(new DrawSpeechComponent(s, this));
	}

	public void setattack() {
		firing = true;
	}

	public void setattack2() {
		scratching = true;
	}

	public WorldPacket getEntityPacket() {

		entity_packet.handle = handle;
		entity_packet.timeframe = System.currentTimeMillis();
		entity_packet.x = getx();
		entity_packet.y = gety();
		entity_packet.currentAction = getCurrentAction();
		entity_packet.facingRight = isFacingright();
		return entity_packet;
	}

	public void draw(Graphics2D g) {

		if (notonScreen()) {
			world.request_despawn(handle);
			return;
		}

		Iterator<IRender> it = renders.iterator();
		while (it.hasNext()) {
			IRender render = (IRender) it.next();
			render.draw(g, it);
		}
	}

	public void setGliding(boolean b) {
		gliding = b;
	}

	public void gethit(int damage, ENTITY enemy) {
		if (flinching)
			return;
		health -= damage;
		if (health < 0)
			health = 0;

		// if (health == 0)
		// this.dead = true;

		flinching = true;
		// flinchTimer = System.currentTimeMillis();

		if (x < enemy.getx())
			dx = -3.0f;
		else
			dx = 3.0f;

		dy = -2.5f;
	}
}
