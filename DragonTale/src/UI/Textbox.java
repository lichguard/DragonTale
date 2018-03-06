package UI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;


public class Textbox extends Control {

	public String display_text = "";
	private int charlimit = 20;
	private boolean password = false;

	public void setpassword(boolean pass) {
		this.password = pass;
	}

	@Override
	public void KeyBoardEvent(KeyEvent key, int i,boolean b) {

		if (!b)
			return;
		
		if (i == KeyEvent.VK_BACK_SPACE && text.length() > 0)
		{
			text = text.substring(0, text.length() - 1);
			display_text = display_text.substring(0, display_text.length() - 1);
		}
		else if (key.getKeyChar() >= 32 && key.getKeyChar() <= 126)
		{
			if (text.length() < charlimit)
			{
				
				text += key.getKeyChar();
				if (password)
				display_text += "*";
				else
				display_text += key.getKeyChar();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setFont(font);
		g.setColor(Color.black);
		g.fillRect(x, y, width, height + font.getSize());
		g.setColor(Color.white);

		if (System.currentTimeMillis() % 1500 < 700 && isfocused()) {
			g.drawString(display_text + "|", x+2, y + font.getSize());
		} else
			g.drawString(display_text, x+2, y +  font.getSize());

	}

}
