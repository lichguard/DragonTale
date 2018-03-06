package GameState;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import TileMap.Background;
import UI.Messagebox;
import UI.Textbox;
import main.CONTROLS;

public class LoginState extends GameState {

	protected Background bg;

	protected int currentChoice = 0;
	protected String[] options;
	protected Color titleColor;
	protected Font titleFont;
	protected Font font;
	protected Textbox password_txtbox;
	protected Textbox username_txtbox;
	protected Button login_button;
	protected Thread connecting_thread = null;
	Messagebox msgbox = new Messagebox("");

	public LoginState(GameStateManager gsm) {
		super(gsm);
		try {
			bg = new Background("/Backgrounds/menubg.gif", 0.1);
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
		username_txtbox.bgcolor = new Color(0, 0, 0, 190);

		password_txtbox = new Textbox();
		password_txtbox.setposition(0.35f, 0.5f);
		password_txtbox.setsize(0.3f, 0.02f);
		password_txtbox.bgcolor = new Color(0, 0, 0, 190);
		password_txtbox.setpassword(true);

		
		this.registerControl(username_txtbox);
		this.registerControl(password_txtbox);
		super.controltab();
		connecting_thread = new Thread(new Runnable() {public void run() {}});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		bg.update();
		handleInput();

		if (gsm.session != null) {
			if (!connecting_thread.isAlive())
				if (gsm.session.isConnected())
					if (gsm.session.authenticate())
					gsm.requestState(GameStateManager.ONLINESTATE);
		}

	}

	@Override
	public void handleInput() {

		super.handleInput();
		if (CONTROLS.isPressed(CONTROLS.ENTER) && (!connecting_thread.isAlive())
				&& (username_txtbox.isfocused() || password_txtbox.isfocused()) ) {

			msgbox.setText("Connecting...");
			this.registerControl(msgbox);
			connecting_thread = new Thread(new Runnable() {
				public void run() {
					msgbox.setText("Initiating...");
					gsm.ConnectToServer(msgbox,"localhost", 9000, username_txtbox.text, password_txtbox.text);
					password_txtbox.setText("");
				}
			});
			connecting_thread.start();
		}

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
		super.draw(g);
	}

}
