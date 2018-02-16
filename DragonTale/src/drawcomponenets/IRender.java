package drawcomponenets;

import java.awt.Graphics2D;
import java.util.Iterator;


public interface IRender {
	
	public void draw(Graphics2D g,Iterator<IRender> it);
}
