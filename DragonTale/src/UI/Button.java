package UI;

import java.awt.Graphics;

public class Button extends Control {

	@Override
	public void draw(Graphics g) {
		super.draw(g);

		if (texture != null) {

			g.drawImage(texture, x, y,width, height, null);
			
		} else {
			g.setColor(bgcolor);
			// g.fillRect(x, y, font.getSize() * text.length() - 10, height +
			// font.getSize());

			g.fillRect(x, y, width, height);

			g.setFont(font);
			g.setColor(forecolor);
			g.drawString(text, x + text.length() * font.getSize() / 2, y + font.getSize());

		}

	}

	@Override
	public void MouseClick() {
		if (action != null)
			action.execute();
		
	}
}
