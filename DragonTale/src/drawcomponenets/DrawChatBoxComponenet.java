package drawcomponenets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Iterator;

import Entity.ENTITY;
import main.CONTROLS;
import main.Gameplay;


public class DrawChatBoxComponenet implements IRender {


	ENTITY entity;
	private Font font = new Font("Arial", Font.PLAIN,11);
	public DrawChatBoxComponenet(ENTITY entity)
	{
		this.entity=entity;
	}
	
	
	@Override
	public void draw(Graphics2D g,Iterator<IRender> it) {
			g.setFont(font);
			g.setColor(Color.black);
			g.fillRect(17, Gameplay.HEIGHT - 60, 120, 15);
			g.setColor(Color.white);
			g.drawString(CONTROLS.getCapturedText() , 20, Gameplay.HEIGHT - 50);
	}

}
