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

		/*
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform transform = g2d.getTransform();
		transform.translate(x, y);
		g2d.transform(transform);
		g2d.setColor(Color.black);
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout tl = new TextLayout(text, g.getFont().deriveFont(45F), frc);
		Shape shape = tl.getOutline(null);
		g2d.setStroke(new BasicStroke(5f));
		g2d.draw(shape);
		g2d.setColor(Color.white);
		g2d.fill(shape);
		*/
	
	}

	@Override
	public void MouseClick() {
		// TODO Auto-generated method stub
		
	}

}
