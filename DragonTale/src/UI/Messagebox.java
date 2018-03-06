package UI;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;


public class Messagebox extends Control {


	public Messagebox(String message)
	{
		this.text = message;
		this.setposition(0.5f, 0.5f);
		this.focus();
	}
	public void KeyBoardEvent(KeyEvent key, int i, boolean b) {
		
	}

	public void focus() {

	
	}

	public void draw(Graphics2D g) {

	}
	
}
