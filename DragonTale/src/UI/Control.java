package UI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import GameState.GameState;
import main.CONTROLS;
import main.Gameplay;

public abstract class Control {

	public Font font = new Font("Arial", Font.PLAIN, 11);
	public String text = "";
	public int x = 0;
	public int y = 0;
	public int width = 0;
	public int height = 0;
	public boolean visible = true;
	public Color bgcolor = new Color(0,0,0,255);
	public Color forecolor = new Color(255,255,255,255);
	public boolean delete_control = false;
	public GameState parent = null;
	
	public void setparent(GameState parent)
	{
		this.parent = parent;
	}
	public synchronized void setText(String txt)
	{
		this.text = txt;
	}
	public void destory()
	{
		delete_control= true;
	}
	public void setvisible(boolean t)
	{
		this.visible = t;
	}
	public boolean isfocused() {
		return CONTROLS.focused_control == (Control)this;
	}

	public void setfont(Font f)
	{
		this.font = f;
	}
	
	public void setposition(float x, float y) {
		this.x = (int) ((float)Gameplay.WIDTH * x);
		this.y = (int) ((float)Gameplay.HEIGHT * y);
	}

	public void setsize(float width, float height) {
		this.width = (int) ((float)Gameplay.WIDTH * width);
		this.height = (int) ((float)Gameplay.HEIGHT * height);

	}

	public void KeyBoardEvent(KeyEvent key, int i, boolean b) {
	}

	public void focus() {

		CONTROLS.focused_control = this;
	}
	public void unfocus() {

		CONTROLS.focused_control = null;
	}

	public void draw(Graphics2D g) {

	}
}
