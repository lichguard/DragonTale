package Network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;

import PACKET.CommandPacket;
import PACKET.WorldPacket;
import UI.Control;
import main.LOGGER;

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
	public Queue<CommandPacket> commandsPackets = new LinkedList<CommandPacket>();
	public Stack<WorldPacket> worldPackets = new Stack<WorldPacket>();
	public String username = "";
	public String password = "";
	public boolean isConnected()
	{
		return connected;
	}
	public void broadcastPosition(WorldPacket packet) {
		if (System.currentTimeMillis() - lastbroadcast > 100) {
			SendWorldPacket(packet);
			lastbroadcast = System.currentTimeMillis();
		}
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
		if (!tcp.init(status))
			return false;
		new Thread(tcp).start();
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

	public void SendWorldPacket(WorldPacket packet) {	
		if (udp != null && packet != null && connected)
			udp.sendWorldPacket(packet);
	}

	public void SendCommand(CommandPacket packet) {
		LOGGER.log(Level.INFO, "Sending command: " + packet.packet_code, this);
		if (tcp != null && packet != null && connected)
			tcp.sendCommand(packet);
	}

}