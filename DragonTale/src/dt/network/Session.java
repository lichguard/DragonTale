package dt.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;

import PACKET.AuthorizationPacket;
import PACKET.MovementData;
import PACKET.WorldPacket;
import UI.Control;
import dt.entity.Spawner;
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
	protected boolean connected = false;

	protected int handle = -1;
	public long lastbroadcast = 0;
	public Queue<WorldPacket> commandsPackets = new LinkedList<WorldPacket>();
	public Stack<MovementData> worldPackets = new Stack<MovementData>();
	public String username = "";
	public String password = "";
	public boolean isConnected()
	{
		return connected;
	}
	public void broadcastPosition(WorldPacket packet) {
	//	if (System.currentTimeMillis() - lastbroadcast > 100) {
	//		SendWorldPacket(packet);
	//		lastbroadcast = System.currentTimeMillis();
	//	}
	}

	public boolean authenticate()
	{
		
		return true;
	}
	public void Connect(Control status,String IP, int port,String username,String password) {
		if (connected) {
			status.setText("Connection failed, you are connected!");
			LOGGER.log(Level.WARNING, "Can't connect, you are connected!", this);
			return;
		}
		
		status.setText("Connecting...");
		this.username = username;
		this.password = password;
		this.port = port;
		this.IP = IP;
		LOGGER.log(Level.INFO, "Connecting to " + IP + "@" + port, this);
		try {
			clientSocket = new Socket(IP, port);
			connected = true;
			status.setText("Connecting...");
			if (!startTCP(status))
			{
				disconnect();
				status.setText("The information you have entered is not valid.");
				return;
			}

			status.setText("Connected!");
			LOGGER.log(Level.INFO, "Connected!", this);
			
		//	try {
			//	Thread.sleep(200);
			//} catch (InterruptedException e) {
				//// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
			
		} catch (UnknownHostException e) {
			status.setText("Unknonw error occured!");
			disconnect();
			return;
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Server offline!", this);
			status.setText("Server is offline");
			disconnect();
			return;
		}

	}

	public boolean startTCP(Control status) {
		LOGGER.log(Level.INFO, "Starting TCP...", this);
		tcp = new WorkerRunnableTCP(this);
		tcp.init(status);
		new Thread(tcp).start();
		
		status.setText("sending Authenticating...");
		SendCommand(new WorldPacket(WorldPacket.HAND_SHAKE, new AuthorizationPacket(username,password)));

		LOGGER.log(Level.INFO, "TCP running", this);
		return true;
	}

	public void startUDP(int udp_port) {
		LOGGER.log(Level.INFO, "Starting UDP...", this);
		this.udp_port = udp_port;
		udp = new WorkerRunnableUDP(this);
		udp.init();
		new Thread(udp).start();
		LOGGER.log(Level.INFO, "UDP running", this);
	}

	public void disconnect() {
		if (!connected) {
			return;
		}
		LOGGER.log(Level.INFO, "Disconnected from server...", this);
		connected = false;
		if (udp != null)
			udp.disconnect();

		if (tcp != null)
			tcp.disconnect();

		try {
			if (clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SendWorldPacket(MovementData packet) {	
		if (udp != null && packet != null && connected)
			udp.sendWorldPacket(packet);
	}

	public void SendCommand(WorldPacket packet) {
		LOGGER.log(Level.INFO, "Sending DATA: " + packet.getCommandName(), this);
		if (tcp != null && packet != null && connected)
			tcp.sendCommand(packet);
	}

	public boolean ProcessIncomingData(WorldPacket pct) {
		try {
			LOGGER.info("INC DATA: " + pct.getCommandName(), this);
			switch (pct.packet_code) {
			case WorldPacket.HAND_SHAKE:
				
				String res = (String) pct.data;
				
				System.out.println("HAND_SHAKE REUSLT: " + res);
				
				if (res.equals("accepted")) {
					SendCommand(new WorldPacket(WorldPacket.REQUEST_UDP_PORT,0));
					Thread.sleep(1000);
					return true;
				}
				else {
					return false;
				}
				
			case WorldPacket.UDP_PORT:
				startUDP((int)pct.data);
				SendCommand(new WorldPacket(WorldPacket.LOGIN,0));
				Thread.sleep(1000);
				return true;
				
			case WorldPacket.LOGIN:
				MovementData mv = (MovementData) pct.data;
				World.getInstance().spawn_requests.add(new Spawner(World.getInstance(), this, true, mv.handle, 0, mv.x, mv.y, mv.facingRight, false));
				Thread.sleep(1000);
				return true;
				
			case WorldPacket.MOVEMENT_DATA:
				worldPackets.add((MovementData) pct.data);
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