package dt.gamestate;

import java.awt.Graphics2D;
import UI.UIManager;

public abstract class GameState {

	protected GameStateManager gsm;
	protected UIManager uimanager;
	
	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
		uimanager = new UIManager();
	}

	public void init() 
	{
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

}
