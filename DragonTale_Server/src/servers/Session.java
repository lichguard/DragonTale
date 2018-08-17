package servers;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;

import PACKET.CommandPacket;
import PACKET.WorldPacket;
import main.LOGGER;

public class Session {
	public UUID id = null;
	public Socket clientSocket = null;
	
	protected int packet_size = 500;
	public String accountName = null;
	public boolean connected = true;
	public int AccountID = -1;
	public int pedhandle = -1;
	
	public WorkerRunnableTCP tcp = null;
	public WorkerRunnableUDP udp = null;
	public int udp_port = -1;
	public static int udp_port_inc = 1;

	public Session(Socket clientSocket) throws Exception {
		this.clientSocket = clientSocket;
		id = UUID.randomUUID();
		this.udp_port = udp_port_inc++;
	}

	public void setpedhandle(int pedhandle) {
		this.pedhandle = pedhandle;
	}

	public void establishConnection() throws Exception {
		tcp = new WorkerRunnableTCP(this);
		tcp.start();
		udp = new WorkerRunnableUDP(this);
		udp.start();
	}
	
	public void disconnect() {
		LOGGER.log(Level.INFO, "Client " + id + " has disconnected...", this);
		Server.getInstance().commandsPackets.add(new CommandPacket(CommandPacket.DESPAWN, this.pedhandle));
		
		try {
			if (clientSocket != null && !clientSocket.isClosed()) {
				clientSocket.close();
				clientSocket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (udp != null)
			udp.disconnect();

		if (tcp != null)
			tcp.disconnect();

	}

	public void SendWorldPacket(WorldPacket packet) {
		if (udp != null && packet != null && connected)
			udp.sendWorldPacket(packet);
	}

	public void SendCommand(CommandPacket packet) {
		if (tcp != null && packet != null && connected)
			tcp.sendCommand(packet);
	}
}
