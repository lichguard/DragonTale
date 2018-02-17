package drawcomponenets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Iterator;

import Network.Session;
import entity.Entity;
import main.World;

public class DrawNameComponent implements IRender {

	World world;
	Entity entity;
	Session session;
	Font font = new Font("Arial", Font.PLAIN,14);

	
	public DrawNameComponent(World world,Entity entity,Session session )
	{
		this.world =world;
		this.entity=entity;
		this.session=session;
	}
	
	@Override
	public void draw(Graphics2D g,Iterator<IRender> it) {
		g.setFont(font);
		
		int TEXTHEIGHT = g.getFontMetrics().getHeight();
		int TEXTWIDTH = g.getFontMetrics().stringWidth(entity.name);
		int XPOS = (int) (entity.x + entity.xmap - TEXTWIDTH / 2);
		int YPOS = (int) (entity.y + entity.ymap - TEXTHEIGHT) - 8;

		g.setColor(Color.black);
		g.drawString(entity.name, XPOS, YPOS - 3);
		
	}

}
