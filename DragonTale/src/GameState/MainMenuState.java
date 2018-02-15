package GameState;



public class MainMenuState extends MenuState {

	public MainMenuState(GameStateManager gsm) {
		super(gsm);
		String[] arr = { "Solo", "Login", "Settings", "Create Server", "Help", "Quit" };
		this.options = arr;
		
	}

	public void select() {
		switch (currentChoice) {
		case 0: //start
			gsm.requestState(GameStateManager.OFFLINESTATE);
			break;
		case 1: //join server
			gsm.requestState(GameStateManager.CONNECTINGSTATE);
			break;
		case 2: //settings
			break;
		case 3: //start server
			gsm.requestState(GameStateManager.SERVERSTATE);
			break;
		case 4: //help
			break;
		case 5:
			System.exit(0);
			break;

		default:
			break;
		}
	}

	
	
}
