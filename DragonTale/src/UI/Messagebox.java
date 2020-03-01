package UI;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import main.GameConstants;



public class Messagebox extends Control {


	public Messagebox(UIManager parent,String message)
	{
		this.text = message;
		this.setposition(0.2f, 0.37f);
		this.setsize(0.25f, 0.15f);
		this.font = new Font("Arial", Font.PLAIN, 11);
		this.setparent(parent);
		this.action = new Action() { @Override public void execute() 
		{parent.DisableMessageBox();
		
		}};
		
	}
	public void KeyBoardEvent(KeyEvent key, int i, boolean b) {
		super.KeyBoardEvent(key, i, b);
		if (key.getKeyCode() == KeyEvent.VK_ENTER && b)
		{
			this.delete_control = true;
		}
	}


	public void draw(Graphics g) {
		super.draw(g);
		g.setFont(font);
		g.setColor(bgcolor);
		g.fillRect(0, y, GameConstants.WIDTH, height + font.getSize());
		g.setColor(forecolor);
		g.drawString(text, x+2, y + font.getSize());
		
		g.drawString("OK", x+2, y + font.getSize()*3);
	}
	@Override
	public void MouseClick() {
		this.delete_control = true;
		
	}
	
}
