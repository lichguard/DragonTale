package gamestate;

import java.awt.Graphics2D;
import UI.UIManager;

public abstract class GameState {

	protected UIManager uimanager;
	
	public GameState() {
		uimanager = new UIManager();
	}

	public void init(String requestedStateInitMessage) 
	{
		if (requestedStateInitMessage != null && requestedStateInitMessage != "") {
			this.uimanager.ShowMessageBox(requestedStateInitMessage);
		}
	}

	public void update()
	{
		uimanager.update();
	}

	public void draw(Graphics2D g)
	{
		uimanager.draw(g);
	}

	public void handleInput()
	{
		uimanager.handleInput();
	}

	public void destroy() {
		uimanager.destroy();
	}
}
