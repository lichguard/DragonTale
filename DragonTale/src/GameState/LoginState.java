package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import TileMap.Background;
import UI.Textbox;
import main.CONTROLS;

public class LoginState extends GameState {


	protected Background bg;

	protected int currentChoice = 0;
	protected String[] options;
	protected Color titleColor;
	protected Font titleFont;
	protected Font font;
	private Textbox password_txtbox;
	private Textbox username_txtbox;

	public LoginState(GameStateManager gsm) {
		super(gsm);
		try {
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			font = new Font("Arial", Font.PLAIN, 12);
		} catch (Exception e) {
			e.printStackTrace();
		}
		username_txtbox = new Textbox();
		username_txtbox.setposition(0.35f, 0.35f);
		username_txtbox.setsize(0.3f, 0.02f);
		username_txtbox.focus();

		password_txtbox = new Textbox();
		password_txtbox.setposition(0.35f, 0.5f);
		password_txtbox.setsize(0.3f, 0.02f);
		password_txtbox.setpassword(true);
		// password_txtbox.focus();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		bg.update();
		handleInput();
	}
	@Override
	public void handleInput() {

		if (CONTROLS.isPressed(CONTROLS.TAB)) {
			if (username_txtbox.isfocused()) {
				password_txtbox.focus();
			} else {
				username_txtbox.focus();
			}
		}

		if (CONTROLS.isPressed(CONTROLS.ENTER)) {
			gsm.ConnectToServer(username_txtbox.text,password_txtbox.text);
			if (gsm.session.isConnected())
				gsm.requestState(GameStateManager.ONLINESTATE);
		}
		// gsm.requestState(GameStateManager.CONNECTINGSTATE);

		if (CONTROLS.isPressed(CONTROLS.ESCAPE)) {
			System.exit(0);
		}

	}
	
	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Dragon Tale", 80, 70);

		g.setFont(font);
		g.drawString("Login", 140, 180);
		g.setFont(font);
		username_txtbox.draw(g);
		password_txtbox.draw(g);
		
	}


	
}
