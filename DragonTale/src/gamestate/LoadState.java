package gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import main.GameConstants;


public class LoadState extends GameState{

	public LoadState()
	{
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, GameConstants.WIDTH, GameConstants.HEIGHT);
		g.setColor(Color.black); 
		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.drawString("Loading...", GameConstants.WIDTH/2 - g.getFontMetrics().stringWidth("Loading...")/2 , GameConstants.HEIGHT/2);
	}
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

}
