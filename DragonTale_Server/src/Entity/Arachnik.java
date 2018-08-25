package Entity;
import TileMap.TileMap;

public class Arachnik extends Enemy {

	public Arachnik(TileMap tm) {
		
		super(tm);
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		health = maxHealth = 250;
	}	
}
