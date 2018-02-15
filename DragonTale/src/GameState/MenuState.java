package GameState;

import TileMap.Background;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import DesertAdventures.CONTROLS;


public class MenuState extends GameState {

	protected Background bg;

	protected int currentChoice = 0;
	protected String[] options;
	protected Color titleColor;
	protected Font titleFont;
	protected Font font;

	public MenuState(GameStateManager gsm) {
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

	public void handleInput() {
		if(CONTROLS.isPressed(CONTROLS.ENTER)) select();
		
		if(CONTROLS.isPressed(CONTROLS.UP)) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length-1;
			}
		}
		
		if(CONTROLS.isPressed(CONTROLS.DOWN)) {

			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
			
		}
		
		
		if(CONTROLS.isPressed(CONTROLS.ESCAPE))
		{
			if (currentChoice == 5)	
				System.exit(0);
			else
				currentChoice = 5;
		}
		
	}
	@Override
	
	public void draw(Graphics2D g) {
		bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Dragon Tale", 80, 70);

		g.setFont(font);
	
		
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.GREEN);
			}
			g.drawString(options[i], 145, 100 + i * 15);
		}
		
	}

	public void select() {

	}

}
