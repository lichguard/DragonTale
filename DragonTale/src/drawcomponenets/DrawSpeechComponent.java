package drawcomponenets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Iterator;

import Entity.ENTITY;


public class DrawSpeechComponent implements IRender {

	ENTITY entity;
	String text = "";
	private Font font = new Font("Arial", Font.PLAIN,11);
	long start_time = 0;
	public DrawSpeechComponent(String text,ENTITY entity)
	{
		this.entity=entity;
		this.text = text;
		this.start_time = System.currentTimeMillis();
	}
	
	
	@Override
	public void draw(Graphics2D g,Iterator<IRender> it) {
		g.setFont(font);
		int TEXTHEIGHT = g.getFontMetrics().getHeight();
		int TEXTWIDTH = g.getFontMetrics().stringWidth(text);
		int xgap = 7;
		int ygap = 3;
		int XPOS = (int) (entity.x + entity.xmap - TEXTWIDTH / 2);
		int YPOS = (int) (entity.y + entity.ymap - TEXTHEIGHT) - 8;

		g.setColor(Color.black);
		g.fillRect(XPOS - xgap, YPOS - TEXTHEIGHT - ygap, xgap * 2 + TEXTWIDTH, ygap * 2 + TEXTHEIGHT);
		g.setColor(Color.white);
		g.drawString(text, XPOS, YPOS - 3);
		
		if (System.currentTimeMillis() - start_time > 1000 + 300 * text.length() / 5)
		{
			it.remove();
		}
	}

}
