package component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Iterator;


import DesertAdventures.Gameplay;
import Entity.ENTITY;


public class DrawChatBoxComponenet implements IRender {


	ENTITY entity;
	String text = "";	
	private Font font = new Font("Arial", Font.PLAIN,11);
	long start_time = 0;
	public DrawChatBoxComponenet(String text,ENTITY entity)
	{
		this.entity=entity;
		this.text = text;
		this.start_time = System.currentTimeMillis();
	}
	
	
	@Override
	public void draw(Graphics2D g,Iterator<IRender> it) {
			g.setFont(font);
			g.setColor(Color.black);
			g.fillRect(17, Gameplay.HEIGHT - 60, 120, 15);
			g.setColor(Color.white);
			g.drawString(text, 20, Gameplay.HEIGHT - 50);
			
			if (System.currentTimeMillis() - start_time > 1500)
			{
				entity.renders.remove(this);
			}
	}

}
