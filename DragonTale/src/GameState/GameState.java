package GameState;

import java.util.Vector;

import UI.Control;

public abstract class GameState {

	public GameStateManager gsm;
	public Vector<Control> controls;
	public int focsed_control = -1;
	
	public GameState(GameStateManager gsm)
	{
		this.gsm = gsm;
	}

	public abstract void init();
	public abstract void update();
	public void draw(java.awt.Graphics2D g)
	{
		for(Control c : controls)
		{
			if (c.visible)
			c.draw(g);
		}
	}
	public void registerControl(Control c)
	{
		controls.addElement(c);
	}
	public void unregisterControl(Control c)
	{
		controls.removeElement(c);
	}

	public abstract void handleInput();
	
	
}
