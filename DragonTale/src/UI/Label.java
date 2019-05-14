package UI;

import java.awt.Graphics;
public class Label extends Control {

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		//g.setColor(bgcolor);
		//g.fillRect(x, y, font.getSize() * text.length() - 10, height + font.getSize());
		g.setFont(font);
		g.setColor(forecolor);
		g.drawString(text, x , y );
		
	
	}

}
