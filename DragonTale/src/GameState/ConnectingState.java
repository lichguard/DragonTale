package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import main.Gameplay;

public class ConnectingState extends GameState {

	boolean oneround = false;

	public ConnectingState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		if (oneround) { //oneround so we can draw connecting...
			//gsm.ConnectToServer();
			if (gsm.session.isConnected())
				gsm.requestState(GameStateManager.ONLINESTATE);
			else
				gsm.requestState(GameStateManager.MAINMENUSTATE);
		}
		oneround = true;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, Gameplay.WIDTH, Gameplay.HEIGHT);
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.drawString("Connecting...", Gameplay.WIDTH / 2 - g.getFontMetrics().stringWidth("Loading...") / 2,
				Gameplay.HEIGHT / 2);
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

}
