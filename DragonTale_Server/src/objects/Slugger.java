package objects;

import vmaps.GameMap;

public class Slugger extends Enemy {

	public Slugger(GameMap tm) {
		super(tm);
		width = 30;
		height = 30;
		cwidth = 30;
		cheight = 20;
		health = maxHealth = 100;
	
	}
}
