package component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Iterator;

import DesertAdventures.World;
import Entity.ENTITY;
import Network.Session;

public class DrawNameComponent implements IRender {

	World world;
	ENTITY entity;
	Session session;
	Font font = new Font("Arial", Font.PLAIN,14);
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
