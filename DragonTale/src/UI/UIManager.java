package UI;

import java.awt.Graphics;
import java.util.Vector;
import main.Controls;

public class UIManager {
	public Vector<Control> controls = new Vector<Control>();
	public int focsed_control = -1;

	public UIManager()
	{						
		Controls.focused_control = null;
	}
	
	public void requestFocus(Control control) {
		
		if (Controls.focused_control instanceof Messagebox)
			return;
		
		//System.out.println(control.getClass());
		Controls.focused_control = control;
	}

	public void requestunFocus(Control control) {
		
		if (Controls.focused_control instanceof Messagebox)
			return;
		
		if (Controls.focused_control == control)
			Controls.focused_control = null;
	}

	public Messagebox ShowMessageBox(String txt) {
		Messagebox msgbox = new Messagebox(this, txt);
		this.registerControl(msgbox);
		requestFocus(msgbox);
		return msgbox;
	}

	public void DisableMessageBox() {
		Control msgbox = Controls.focused_control;
		Controls.focused_control = null;
		focsed_control = -1;
		unregisterControl(msgbox);
	}

	public void update(long timeDelta) {
		
		if (Controls.isPressed(Controls.LMB)) {
			for (Control control : controls) {
				if (control.x <= Controls.mousex && control.x + control.width >= Controls.mousex  &&
						control.y <= Controls.mousey && control.y + control.height >= Controls.mousey 	) {
					requestFocus(control);
					control.MouseClick();
					break;
				}
			}
			
		}
	}

	public void draw(Graphics g) {
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
		
		if (Controls.focused_control instanceof Messagebox)
			return;
		
		if (controls.size() > 0) {
			focsed_control += 1;
			if (controls.size() <= focsed_control)
				focsed_control = 0;

			controls.get(focsed_control).focus();
		}
	}
	
	public void handleInput() {
		if (Controls.isPressed(Controls.TAB)) {
			switchcontrol();
		}
	}

	public void destroy() {
		
	}
}
