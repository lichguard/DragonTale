package objects;
import vmaps.GameMap;

public class Arachnik extends Enemy {

	public Arachnik(GameMap tm) {
		
		super(tm);
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		health = maxHealth = 250;
	}	
}
