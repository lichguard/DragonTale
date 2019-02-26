package objects;
import network.WorldSocket;

public class Player extends Unit{
	public boolean is_player;
	public Player(WorldSocket session) {
		// TODO Auto-generated constructor stub
		super();
		cwidth = 30;
		width = 30;
		height = 30;
		currentAction = IDLE;
		this.broadcaster.setSocket(session);
	
	}

	

}
