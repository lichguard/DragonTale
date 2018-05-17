package drawcomponenets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import dt.entity.Entity;
import dt.network.Session;
import main.World;

public class DrawNamePlateComponent implements IRender {

	World world;
	Entity entity;
	Session session;
	
	public DrawNamePlateComponent(World world,Entity entity,Session session )
	{
		this.world =world;
		this.entity=entity;
		this.session=session;
	}
	
	@Override
	public void draw(Graphics2D g,Iterator<IRender> it) {

			double precentage = ((double) entity.health / (double) entity.maxHealth);
			int XPOS = (int) (entity.x + entity.xmap - entity.width / 2);
			int YPOS = (int) (entity.y + entity.ymap - entity.height * 0.6);
			int PlateBold = 4;

			g.setColor(Color.RED);
			g.fillRect(XPOS, YPOS, (int) (entity.getCWidth() * precentage), PlateBold);
			g.setColor(Color.BLACK);
			g.fillRect(XPOS + (int) (entity.getCWidth() * precentage), YPOS, (int) (entity.getCWidth() * (1 - precentage)), PlateBold);

		
	}

}
