package gamestate;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;


import TileMap.Background;
import UI.Action;
import UI.Button;
import UI.Label;
import UI.Messagebox;
import UI.Textbox;
import main.Controls;
import network.Session;

public class SettingsState extends GameState {

	protected Background bg;
	protected Font font;

	protected Label title;
	protected Textbox password_txtbox;
	protected Textbox username_txtbox;
	protected Button login_button;
	protected Thread connecting_thread;
	protected boolean cancel_login = false;
	
	
	@Override
	public void init(String requestedStateInitMessage) {
		super.init(requestedStateInitMessage);
		bg = new Background("/Backgrounds/menubg.gif", 0.1);
		bg.setVector(-0.1, 0);
		//titleColor = new Color(128, 0, 0);
		//titleFont = new Font("Century Gothic", Font.PLAIN, 28);
		font = new Font("Arial", Font.PLAIN, 12);
		connecting_thread = null;
		init_UI();
	}

	public void init_UI()
	{
		Action action = new Action() { @Override public void execute() {login();}};
		
		
		username_txtbox = new Textbox();
		username_txtbox.setposition(0.35f, 0.35f);
		username_txtbox.setsize(0.3f, 0.05f);
		username_txtbox.bgcolor = new Color(0, 0, 0, 190);
		username_txtbox.name = "txtbox_username";
		this.uimanager.registerControl(username_txtbox);
		username_txtbox.registerAction(action);
		
		password_txtbox = new Textbox();
		password_txtbox.setposition(0.35f, 0.5f);
		password_txtbox.setsize(0.3f, 0.05f);
		password_txtbox.bgcolor = new Color(0, 0, 0, 190);
		password_txtbox.setpassword(true);
		password_txtbox.registerAction(action);
		password_txtbox.name = "password_txtbox";
		this.uimanager.registerControl(password_txtbox);
		
		login_button = new Button();
		login_button.setText("Login");
		login_button.setposition(0.45f, 0.65f);
		login_button.setsize(0.1f, 0.05f);
		login_button.registerAction(action);
		login_button.setforecolor(new Color(255,255,255,255));
		login_button.name = "login_button";
		this.uimanager.registerControl(login_button);
		
		title = new Label();
		title.setText("Dragon Tale");
		title.setposition(0.3f, 0.17f);
		title.setforecolor(new Color(0,0,0,255));
		title.setfont(new Font("Century Gothic", Font.PLAIN, 40));
		title.name = "title_label";
		this.uimanager.registerControl(title);
		
		title = new Label();
		title.setText("Username");
		title.setposition(0.35f, 0.34f);
		title.setforecolor(new Color(0,0,0,255));
		title.name = "title_label";
		this.uimanager.registerControl(title);
		
		title = new Label();
		title.setText("password");
		title.setposition(0.35f, 0.49f);
		title.setforecolor(new Color(0,0,0,255));
		title.name = "title_label";
		this.uimanager.registerControl(title);
		
	}

	public void login() {

		if (connecting_thread != null && connecting_thread.isAlive())
			return;
		
		if (!(username_txtbox.isfocused() || password_txtbox.isfocused())) {
			uimanager.ShowMessageBox("Missing password or username...");
			return;
		}

		Messagebox msgbox = uimanager.ShowMessageBox("Initiating...");
		connecting_thread = new Thread(new Runnable() {
			public void run() {
				
				Session.getInstance().Connect(msgbox, "localhost", 9000, username_txtbox.text, password_txtbox.text);
				password_txtbox.setText("");

				if (cancel_login) {
					Session.getInstance().disconnect("");
					cancel_login = false;
					return;
				}

				//TODO: GET msgbox text
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
		if (Controls.isPressed(Controls.ESCAPE)) {
			System.exit(0);
		}

	}

	@Override
	public void draw(Graphics g) {
		bg.draw(g);
		super.draw(g);
	}

}
