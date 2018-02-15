package component;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import DesertAdventures.Gameplay;
import DesertAdventures.World;
import Entity.ENTITY;
import Network.Session;

public class DrawHudComponent implements IRender {

	World world;
	ENTITY entity;
	Session session;
	BufferedImage image;
	private Font font = new Font("Arial", Font.PLAIN,11);

	public DrawHudComponent(World world,ENTITY entity,Session session )
	{
		this.world =world;
		this.entity=entity;
		this.session=session;
	}
	
	@Override
	public void draw(Graphics2D g,Iterator<IRender> it) {

		g.drawImage(image, 0, 10, null);
		g.setFont(font);
		g.drawString(entity.health + "/" + entity.maxHealth, 30, 25);
		// g.drawString(player.getFire() / 100 + "/" + player.getMaxFire()/100, 30, 45);
		g.drawString("Coins: " + entity.coins, 30, 65);

		if (!Gameplay.DEBUG)
			return;
		g.drawString("SpeedX: " + entity.getdx(), Gameplay.WIDTH - 100, 25);
		g.drawString("SpeedY: " + entity.getdy(), Gameplay.WIDTH - 100, 45);

		g.drawString("PosX: " + entity.getx(), Gameplay.WIDTH - 100, 70);
		g.drawString("PosY: " + entity .gety(), Gameplay.WIDTH - 100, 95);
		g.drawString("Entities: " + world.getEntitityCount(), Gameplay.WIDTH - 100, 115);

		g.drawString("frame: " + Gameplay.FPS + " ms", Gameplay.WIDTH - 100, 135);

	}

}
