package network;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;

import PACKET.AuthorizationPacket;
import PACKET.MovementData;
import PACKET.WorldPacket;
import UI.Control;
import gamestate.GameStateManager;
import main.LOGGER;

public class Session {

	protected WorkerRunnableTCP tcp = null;
	protected WorkerRunnableUDP udp = null;
	protected int packet_size = 500;

	protected String IP = "localhost";
	protected int udp_port = -1;
	protected int port = 9000;

	public int handle = -1;
	public Queue<WorldPacket> commandsPackets = new LinkedList<WorldPacket>();
	public Stack<MovementData> worldPackets = new Stack<MovementData>();
	protected Control callbackstatusControl = null;
	private static Session instance = null;
	public String playerName = null;
	protected boolean isConnected = false;

	public static Session getInstance() {
		if (instance == null)
			instance = new Session();
		return instance;
	}

	public synchronized void Connect(Control callbackstatusControl, String IP, int port, String username,
			String password) {
		this.callbackstatusControl = callbackstatusControl;

		if (isConnected) {
			callbackstatusControl.setText("Connecting failed, you are connected!");
			LOGGER.log(Level.WARNING, "Connecting failed, the user is connected!", this);
			this.callbackstatusControl = null;
		} else {
			callbackstatusControl.setText("Connecting...");
			this.port = port;
			this.IP = IP;
			LOGGER.info("Connecting to " + IP + "@" + port, this);
			try {
				startTCP(IP, port);
				callbackstatusControl.setText("Authenticating...");
				LOGGER.info("Sending authorization request...", this);
				if (isConnected) {
					SendCommand(new WorldPacket(WorldPacket.HAND_SHAKE, new AuthorizationPacket(username, password)));
				}
				else {
					throw new UnknownHostException();
				}
			} catch (UnknownHostException e) {
				callbackstatusControl.setText("Unknonw error occured!");
				callbackstatusControl = null;
				disconnect("Unknonw error occured while trying to connect!");
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Server offline!", this);
				callbackstatusControl.setText("Server is offline");
				callbackstatusControl = null;
				disconnect("");
			}
		}
	}

	public void startTCP(String IP, int port) throws IOException {
		LOGGER.log(Level.INFO, "Starting TCP thread...", this);
		tcp = new WorkerRunnableTCP(this);
		isConnected = true;
		tcp.init(IP, port);
	}

	public void startUDP(int udp_port) throws IOException {
		LOGGER.log(Level.INFO, "Starting UDP Thread...", this);
		this.udp_port = udp_port;
		udp = new WorkerRunnableUDP(this);
		udp.init(tcp.clientSocket.getPort() + udp_port);

	}

	public synchronized void disconnect(String reason) {
		if (!isConnected)
			return;

		LOGGER.info("Disconnected from the server...", this);
		GameStateManager.getInstance().requestState(GameStateManager.LOGINSTATE, "Disconnected from the server...");

		if (udp != null)
			udp.disconnect();

		if (tcp != null)
			tcp.disconnect();

		isConnected = false;
	}

	public void SendWorldPacket(MovementData packet) {
		udp.sendWorldPacket(packet);
	}

	public synchronized void SendCommand(WorldPacket packet) {
		try {
			tcp.sendCommand(packet);
		} catch (IOException e) {
			e.printStackTrace();
			disconnect("error sending command packet");
		}
	}

	public void ProcessIncomingData(WorldPacket pct) {
		try {
			switch (pct.packet_code) {
			case WorldPacket.HAND_SHAKE:
				boolean accepeted = handleHandShake(pct);
				if (accepeted) {
					GameStateManager.getInstance().requestState(GameStateManager.ONLINESTATE, "");
					SendCommand(new WorldPacket(WorldPacket.REQUEST_UDP_PORT, null));
				} else {
					disconnect("Server refused connection for this account");
				}
				break;
			case WorldPacket.SETNAME:
				playerName = (String) pct.data;
				break;
			case WorldPacket.MOVEMENT_DATA:
				synchronized (worldPackets) {
					worldPackets.add((MovementData) pct.data);
				}
				break;
			default:
				synchronized (commandsPackets) {
					commandsPackets.add(pct);
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("UNKNOWN ERROR (PACKET_CODE " + pct.packet_code + ") : " + e.getMessage(), this);
		}

	}

	public boolean handleHandShake(WorldPacket pct) {
		String res = (String) pct.data;
		LOGGER.info("HAND_SHAKE result: " + res, this);
		if (res.equals("accepted")) {
			callbackstatusControl.setText("Connected!");
			return true;
		} else if (res.equals("refused")) {
			callbackstatusControl.setText("The information you have entered is not valid!");
		} else if (res.equals("temp banned")) {
			callbackstatusControl.setText("Your account has been suspended, try again later");
		} else if (res.equals("banned")) {
			callbackstatusControl.setText("Your account has been banned");
		} else {
			callbackstatusControl.setText("The server refused the connection for unknown reason");
		}
		return false;
	}

}