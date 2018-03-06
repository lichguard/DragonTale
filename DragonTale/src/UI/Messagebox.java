package UI;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import main.Gameplay;


public class Messagebox extends Control {


	public Messagebox(String message)
	{
		this.text = message;
		this.setposition(0.2f, 0.37f);
		this.setsize(0.25f, 0.15f);
		this.font = new Font("Arial", Font.PLAIN, 11);
	}
	public void KeyBoardEvent(KeyEvent key, int i, boolean b) {
		
		if (key.getKeyCode() == KeyEvent.VK_ENTER && b)
		{
			this.delete_control = true;
		}
	}


	public void draw(Graphics2D g) {
		super.draw(g);
		g.setFont(font);
		g.setColor(bgcolor);
		g.fillRect(0, y, Gameplay.WIDTH, height + font.getSize());
		g.setColor(forecolor);
		g.drawString(text, x+2, y + font.getSize());
		
		g.drawString("OK", x+2, y + font.getSize()*3);
	}
	
}
