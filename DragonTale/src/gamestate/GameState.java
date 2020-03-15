package gamestate;

import java.awt.Graphics;
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

	public void update(long timeDelta)
	{
		uimanager.update(timeDelta);
	}

	public void draw(Graphics g)
	{
		uimanager.draw(g);
	}

	public void handleInput()
	{
		uimanager.handleInput();
	}

	public void destroy() throws Exception {
		uimanager.destroy();
	}
}
