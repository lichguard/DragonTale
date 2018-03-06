package drawcomponenets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Iterator;

import entity.Entity;
import main.Gameplay;


public class DrawChatBoxComponenet implements IRender {


	Entity entity;
	private Font font = new Font("Arial", Font.PLAIN,11);
	public DrawChatBoxComponenet(Entity entity)
	{
		this.entity=entity;
	}
	
	
	@Override
	public void draw(Graphics2D g,Iterator<IRender> it) {
			g.setFont(font);
			g.setColor(Color.black);
			g.fillRect(17, Gameplay.HEIGHT - 60, 120, 15);
			g.setColor(Color.white);
			g.drawString(entity.txtbox.text , 20, Gameplay.HEIGHT - 50);
	}

}
