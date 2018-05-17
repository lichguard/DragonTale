package dt.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import TileMap.Background;
import UI.Action;
import UI.Button;
import UI.Messagebox;
import UI.Textbox;
import main.CONTROLS;

public class LoginState extends GameState {

	protected Background bg;
	protected Color titleColor;
	protected Font titleFont;
	protected Font font;

	protected Textbox password_txtbox;
	protected Textbox username_txtbox;
	protected Button login_button;
	protected Thread connecting_thread;
	protected boolean cancel_login = false;

	public LoginState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		try {
			bg = new Background("/Backgrounds/menubg.gif", 0.1);
			bg.setVector(-0.1, 0);
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			font = new Font("Arial", Font.PLAIN, 12);
		} catch (Exception e) {
			e.printStackTrace();
		}
		connecting_thread = null;
		init_UI();
	}

	public void init_UI()
	{
		Action action = new Action() { @Override public void execute() {login();}};
		
		
		username_txtbox = new Textbox();
		username_txtbox.setposition(0.35f, 0.35f);
		username_txtbox.setsize(0.3f, 0.02f);
		username_txtbox.bgcolor = new Color(0, 0, 0, 190);
		this.uimanager.registerControl(username_txtbox);
		username_txtbox.registerAction(action);
		
		password_txtbox = new Textbox();
		password_txtbox.setposition(0.35f, 0.5f);
		password_txtbox.setsize(0.3f, 0.02f);
		password_txtbox.bgcolor = new Color(0, 0, 0, 190);
		password_txtbox.setpassword(true);
		password_txtbox.registerAction(action);
		this.uimanager.registerControl(password_txtbox);
		
		login_button = new Button();
		login_button.setText("Login");
		login_button.setposition(0.4f, 0.65f);
		login_button.setsize(0.3f, 0.02f);
		login_button.registerAction(action);
		login_button.setforecolor(new Color(0,0,0,255));
		this.uimanager.registerControl(login_button);
		
	}
	public void login() {

		if (connecting_thread != null && connecting_thread.isAlive())
			return;
		if (!(username_txtbox.isfocused() || password_txtbox.isfocused()))
			return;

		
		connecting_thread = new Thread(new Runnable() {
			public void run() {
				Messagebox msgbox = uimanager.ShowMessageBox("Initiating...");
				gsm.ConnectToServer(msgbox, "localhost", 9000, username_txtbox.text, password_txtbox.text);
				password_txtbox.setText("");

				if (cancel_login) {
					gsm.session.disconnect();
					cancel_login = false;
					return;
				}
				
				if (gsm.session.isConnected())
					if (gsm.session.authenticate())
						gsm.requestState(GameStateManager.ONLINESTATE);

			}
		});
		connecting_thread.start();

	}

	@Override
	public void update() {
		super.update();
		bg.update();
		handleInput();
	}

	@Override
	public void handleInput() {
		super.handleInput();
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

		//g.setFont(font);
		//g.drawString("Login", 140, 180);
		//g.setFont(font);
		super.draw(g);
	}

}
