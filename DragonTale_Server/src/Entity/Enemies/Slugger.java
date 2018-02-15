package Entity.Enemies;

import Entity.Enemy;
import TileMap.TileMap;

public class Slugger extends Enemy {

	public Slugger(TileMap tm) {
		super(tm);
		width = 30;
		height = 30;
		cwidth = 30;
		cheight = 20;
		health = maxHealth = 100;
	
	}
}
