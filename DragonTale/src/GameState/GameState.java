package GameState;

import java.awt.Graphics2D;
import java.util.Vector;


import UI.Control;
import main.CONTROLS;

public abstract class GameState {

	public GameStateManager gsm;
	public Vector<Control> controls;
	public int focsed_control = -1;

	public GameState(GameStateManager gsm) {
		controls = new Vector<Control>();
		this.gsm = gsm;
	}

	public abstract void init();

	public abstract void update();

	public void draw(Graphics2D g) {
		for (int i = 0; i < controls.size(); i++) {
			Control c = controls.get(i);
			if (c.visible)
				c.draw(g);

			if (c.delete_control) {
				controls.remove(i);
				c.delete_control = false;
				c.setparent(null);
				i--;
				controltab();
			}
		}
	}

	public void registerControl(Control c) {
		controls.addElement(c);

		c.focus();
		c.setparent(this);
	}

	public void unregisterControl(Control c) {
	}

	public void controltab()
	{
		if (controls.size() > 0) {
			focsed_control += 1;
			if (controls.size() <= focsed_control)
				focsed_control = 0;

			controls.get(focsed_control).focus();
		}
	}
	public void handleInput() {
		if (CONTROLS.isPressed(CONTROLS.TAB)) {
			controltab();
		}
	}

}
