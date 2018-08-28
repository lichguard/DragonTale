package objects;
import vmaps.TileMap;

public class Player extends Unit{
	public boolean is_player;
	public Player(TileMap tm) {
		super(tm);
		// TODO Auto-generated constructor stub
		cwidth = 30;
		width = 30;
		height = 30;
		currentAction = IDLE;
	
	}

	

}
