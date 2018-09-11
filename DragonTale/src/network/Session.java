package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;

import PACKET.AuthorizationPacket;
import PACKET.MovementData;
import PACKET.NetworkSpawner;
import PACKET.SpeechPacket;
import PACKET.WorldPacket;
import UI.Control;
import entity.Entity;
import entity.Spawner;
import gamestate.GameStateManager;
import main.LOGGER;
import main.World;

public class Session {

	protected Socket clientSocket = null;
	protected WorkerRunnableTCP tcp = null;
	protected WorkerRunnableUDP udp = null;
	protected int packet_size = 500;

	protected String IP = "localhost";
	protected int udp_port = -1;
	protected int port = 9000;

	protected int handle = -1;
	public long lastbroadcast = 0;
	public Queue<WorldPacket> commandsPackets = new LinkedList<WorldPacket>();
	public Stack<MovementData> worldPackets = new Stack<MovementData>();
	protected Control callbackstatusControl = null;
	private static Session instance = null;
	public Entity _player = null;
	
	public static Session getInstance() {
		if (instance == null)
			instance = new Session();
		return instance;
	}


	public void Connect(Control callbackstatusControl, String IP, int port, String username, String password) {
		this.callbackstatusControl = callbackstatusControl;

		if (clientSocket != null && !clientSocket.isClosed()) {
			callbackstatusControl.setText("Connection failed, you are connected!");
			LOGGER.log(Level.WARNING, "Connection call failed, the user is connected!", this);
			callbackstatusControl = null;
		} else {
			callbackstatusControl.setText("Connecting...");
			this.port = port;
			this.IP = IP;
			LOGGER.log(Level.INFO, "Connecting to " + IP + "@" + port, this);
			try {
				clientSocket = new Socket(IP, port);
				startTCP();
				callbackstatusControl.setText("Authenticating...");
				LOGGER.log(Level.INFO, "Sending Autherizaing request...", this);
				SendCommand(new WorldPacket(WorldPacket.HAND_SHAKE, new AuthorizationPacket(username, password)));
			} catch (UnknownHostException e) {
				callbackstatusControl.setText("Unknonw error occured!");
				callbackstatusControl = null;
				disconnect();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Server offline!", this);
				callbackstatusControl.setText("Server is offline");
				callbackstatusControl = null;
				disconnect();
			}
		}
	}

	public void startTCP() throws IOException {
		LOGGER.log(Level.INFO, "Starting TCP thread...", this);
		tcp = new WorkerRunnableTCP(this);
		tcp.init();
		new Thread(tcp).start();
		try {Thread.sleep(200);} 
		catch (InterruptedException e) {e.printStackTrace();}
		}

	public void startUDP(int udp_port) {
		LOGGER.log(Level.INFO, "Starting UDP Thread...", this);
		this.udp_port = udp_port;
		udp = new WorkerRunnableUDP(this);
		udp.init();
		new Thread(udp).start();
	}

	public void disconnect() {
		LOGGER.log(Level.INFO, "Disconnected from server...", this);
		
		World.getInstance().restart();
		
		try {
			if (clientSocket != null && !clientSocket.isClosed())
				clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (udp != null)
			udp.disconnect();

		if (tcp != null)
			tcp.disconnect();

	}

	public void SendWorldPacket(MovementData packet) {
		if (udp != null && packet != null && clientSocket != null && !clientSocket.isClosed())
			udp.sendWorldPacket(packet);
	}

	public void SendCommand(WorldPacket packet) {
		if (tcp != null && packet != null && clientSocket != null && !clientSocket.isClosed())
			tcp.sendCommand(packet);
	}

	public boolean ProcessIncomingData(WorldPacket pct) {
		try {
			switch (pct.packet_code) {
			case WorldPacket.HAND_SHAKE:

				String res = (String) pct.data;

				LOGGER.debug("HAND_SHAKE REUSLT: " + res, this);

				if (res.equals("accepted")) {
					callbackstatusControl.setText("Connected!");
					GameStateManager.getInstance().requestState(GameStateManager.ONLINESTATE);
					SendCommand(new WorldPacket(WorldPacket.REQUEST_UDP_PORT, null));
					return true;
				} else {
					callbackstatusControl.setText("The information you have entered is not valid.!");
					return false;
				}

			case WorldPacket.UDP_PORT:
				startUDP((int) pct.data);
				return true;

			case WorldPacket.LOGIN:
				NetworkSpawner spwaner = (NetworkSpawner) pct.data;
				World.getInstance().request_spawn(spwaner.name, true, spwaner.handle, spwaner.type, spwaner.x, spwaner.y, spwaner.facing, spwaner.network);
				//World.getInstance().request_spawn(Integer.toString(mv.handle),true, mv.handle, Spawner.PLAYERPED, mv.x, mv.y, mv.facingRight, false);
				
				return true;
				
			case WorldPacket.SETNAME:
				_player.name = (String) pct.data;
				return true;

			case WorldPacket.SPAWN:
				NetworkSpawner spawner = (NetworkSpawner) pct.data;

				World.getInstance().request_spawn(spawner.name, false, spawner.handle, spawner.type, spawner.x, spawner.y,
						spawner.facing, spawner.network);
				return true;
			case WorldPacket.DESPAWN:
				World.getInstance().entities.get((int)pct.data).fadeOut();
				//World.getInstance().request_despawn((int) pct.data);
				return true;
			case WorldPacket.SPEECH:
				SpeechPacket data = (SpeechPacket) pct.data;
				World.getInstance().entities.get(data.handle).say(data.text);
				return true;
			case WorldPacket.MOVEMENT_DATA:
				synchronized (worldPackets) {
					worldPackets.add((MovementData) pct.data);
				}
				return true;

			default:
				synchronized (commandsPackets) {
					commandsPackets.add(pct);
				}
				return true;
			}
		} catch (Exception e) {
			System.out.println("UNKNOWN MSG");
			// sLog.outError("WorldSocket::ProcessIncomingData ByteBufferException occured
			// while parsing an instant handled packet (opcode: %u) from client %s,
			// accountid=%i.",
			// opcode, GetRemoteAddress().c_str(), m_session ? m_session->GetAccountId() :
			// -1);

			// DETAIL_LOG("Disconnecting session [account id %i / address %s] for badly
			// formatted packet.",
			// m_session ? m_session->GetAccountId() : -1, GetRemoteAddress().c_str());

			return false;
		}

	}

}