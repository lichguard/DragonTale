package dt.gamestate;

import UI.Control;
import dt.network.Session;


public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	private int requestedState;
	public Session session = null;
	public static final int NUMGAMESTATES = 7;
	
	public static final int MAINMENUSTATE = 0;
	public static final int LOADSTATE = 1;
	public static final int OFFLINESTATE = 2;
	public static final int ONLINESTATE = 3;
	public static final int SERVERSTATE = 4;
	public static final int CONNECTINGSTATE = 5;
	public static final int LOGINSTATE = 6;
	
	private static GameStateManager instance = null;
	
	public static GameStateManager getInstance() {
		if (instance == null)
			instance = new GameStateManager();
		return instance;
	}
	
	private GameStateManager() {
		gameStates = new GameState[NUMGAMESTATES];
		currentState = requestedState = LOADSTATE;
		loadState(currentState);
		requestState(LOGINSTATE);
	}
	
	public void ConnectToServer(Control status, String host, int port, String username,String password)
	{
		session = new Session();
		session.Connect(status, host, port,username,password);
	}

	private void loadState(int state) {
		if (currentState == ONLINESTATE)
			gameStates[currentState] = new OnlineState();
		else if (currentState == LOADSTATE)
			gameStates[currentState] = new LoadState();
		else if (currentState == OFFLINESTATE)
			gameStates[currentState] = new OfflineState();
		else if (currentState == LOGINSTATE)
			gameStates[currentState] = new LoginState();
		
		gameStates[currentState].init();
	}

	private void unloadState(int state) {
		gameStates[state] = null;
		System.gc();
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}

	public void requestState(int state) {
		requestedState = state;
	}

	public void update() {

		if (requestedState != currentState && currentState == LOADSTATE)
			setState(requestedState);
		else if (requestedState != currentState)
			setState(LOADSTATE);

		gameStates[currentState].update();
	}

	public void draw(java.awt.Graphics2D g) {

		gameStates[currentState].draw(g);

	}
	
}
