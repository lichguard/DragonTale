package Entity.Enemies;







import Entity.PED;
import TileMap.TileMap;

public class PlayerPED extends PED{
	public boolean is_player;
	public PlayerPED(TileMap tm) {
		super(tm);
		// TODO Auto-generated constructor stub
		cwidth = 30;
		width = 30;
		height = 30;
		currentAction = IDLE;
	
	}

	

}
