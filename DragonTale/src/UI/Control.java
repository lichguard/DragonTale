package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import main.CONTROLS;
import main.GameConstants;


public abstract class Control {

	public Font font = new Font("Arial", Font.PLAIN, 11);
	public String text = "";
	public int x = 0;
	public int y = 0;
	public int width = 0;
	public int height = 0;
	public boolean visible = true;
	public Color bgcolor = new Color(0, 0, 0, 255);
	public Color forecolor = new Color(255, 255, 255, 255);
	public boolean delete_control = false;
	public UIManager parent = null;
	public Action action = null;
	
	public void setforecolor(Color color)
	{
		this.forecolor = color;
	}
	public void setparent(UIManager parent) {
		this.parent = parent;
	}

	public synchronized void setText(String txt) {
		this.text = txt;
	}

	public void destory() {
		parent.unregisterControl(this);
	}

	public void setvisible(boolean v) {
		this.visible = v;
	}

	public boolean isfocused() {
		return CONTROLS.focused_control == (Control) this;
	}

	public void setfont(Font f) {
		this.font = f;
	}

	public void setposition(float x, float y) {
		
		if (x > 1 || y > 1 || x < 0 || y < 0)
			throw new Error("Invalid Position");
			
		this.x = (int) ((float) GameConstants.WIDTH * x);
		this.y = (int) ((float) GameConstants.HEIGHT * y);
	}

	public void setsize(float width, float height) {
		if (width > 1 || height > 1 || width < 0 || height < 0)
			throw new Error("Invalid size");
		
		this.width = (int) ((float) GameConstants.WIDTH * width);
		this.height = (int) ((float) GameConstants.HEIGHT * height);

	}

	public void registerAction(Action action)
	{
		this.action = action;
	}
	
	public void KeyBoardEvent(KeyEvent key, int i, boolean b) {
		if (i == KeyEvent.VK_ENTER && b) {
			if (action != null)
				action.execute();
		}
	}

	public void focus() {
		if (parent != null)
			parent.requestFocus(this);
	}

	public void unfocus() {
		if (parent != null)
			parent.requestunFocus(this);
	}

	public void draw(Graphics2D g) {

	}
}
