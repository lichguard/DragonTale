package objects;
import vmaps.GameMap;

public class Player extends Unit{
	public boolean is_player;
	public Player(GameMap tm) {
		super(tm);
		// TODO Auto-generated constructor stub
		cwidth = 30;
		width = 30;
		height = 30;
		currentAction = IDLE;
	
	}

	

}
