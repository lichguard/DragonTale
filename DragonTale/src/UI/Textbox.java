package UI;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;


public class Textbox extends Control {

	private int charlimit = 20;
	private boolean password = false;

	public synchronized void setText(String txt)
	{
		this.text = txt;
	}
	
	public void setpassword(boolean pass) {
		this.password = pass;
	}

	@Override
	public void KeyBoardEvent(KeyEvent key, int i,boolean b) {

		super.KeyBoardEvent(key, i, b);
		if (!b)
			return;
		
		if (i == KeyEvent.VK_BACK_SPACE && text.length() > 0)
		{
			text = text.substring(0, text.length() - 1);
		}
		else if (key.getKeyChar() >= 32 && key.getKeyChar() <= 126)
		{
			if (text.length() < charlimit)
			{
				
				text += key.getKeyChar();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		g.setFont(font);
		g.setColor(bgcolor);
		g.fillRect(x, y, width, height + font.getSize());
		g.setColor(forecolor);

		if (System.currentTimeMillis() % 1500 < 700 && isfocused()) {
			g.drawString((password ? text.replaceAll(".", "*") : text) + "|", x+2, y + font.getSize());
		} else
			g.drawString(password ? text.replaceAll(".", "*") : text, x+2, y +  font.getSize());

	}

}
