package UI;

import java.awt.Graphics2D;
import java.util.Vector;
import main.CONTROLS;

public class UIManager {
	public Vector<Control> controls = new Vector<Control>();
	public int focsed_control = -1;

	public UIManager()
	{						
		CONTROLS.focused_control = null;
	}
	
	public void requestFocus(Control control) {
		
		if (CONTROLS.focused_control instanceof Messagebox)
			return;
		
		//System.out.println(control.getClass());
		CONTROLS.focused_control = control;
	}

	public void requestunFocus(Control control) {
		
		if (CONTROLS.focused_control instanceof Messagebox)
			return;
		
		if (CONTROLS.focused_control == control)
			CONTROLS.focused_control = null;
	}

	public Messagebox ShowMessageBox(String txt) {
		Messagebox msgbox = new Messagebox(this, txt);
		this.registerControl(msgbox);
		requestFocus(msgbox);
		return msgbox;
	}

	public void DisableMessageBox() {
		Control msgbox = CONTROLS.focused_control;
		CONTROLS.focused_control = null;
		focsed_control = -1;
		unregisterControl(msgbox);
	}

	public void update() {
	}

	public void draw(Graphics2D g) {
		for (Control c : controls) {
			if (c.visible)
				c.draw(g);
		}
	}

	public void registerControl(Control control) {
		controls.addElement(control);
		control.setparent(this);
		switchcontrol();
	}

	public void unregisterControl(Control c) {
		this.controls.remove(c);
		switchcontrol();
	}

	public void switchcontrol() {
		
		if (CONTROLS.focused_control instanceof Messagebox)
			return;
		
		if (controls.size() > 0) {
			focsed_control += 1;
			if (controls.size() <= focsed_control)
				focsed_control = 0;

			controls.get(focsed_control).focus();
		}
	}
	
	public void handleInput() {
		if (CONTROLS.isPressed(CONTROLS.TAB)) {
			switchcontrol();
		}
	}

}
