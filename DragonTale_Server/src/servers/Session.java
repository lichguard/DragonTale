package servers;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

import PACKET.CommandPacket;
import PACKET.WorldPacket;
import main.LOGGER;

public class Session {
	public UUID id = null;
	protected int packet_size = 500;
	public String accountName = null;
	public LoginServer server = null;
	public boolean connected = true;
	public int AccountID = -1;
	public int pedhandle = -1;
	public Socket clientSocket = null;
	public WorkerRunnableTCP tcp = null;
	public WorkerRunnableUDP udp = null;
	public int udp_port = -1;
	public static int udp_port_inc = 1;

	public Session(Socket clientSocket, ExecutorService WorkerRunnable, LoginServer server) {
		this.server = server;
		this.clientSocket = clientSocket;
		id = UUID.randomUUID();
		this.udp_port = udp_port_inc++;
		if (!startTCP(WorkerRunnable)) {
			this.SendCommand(new CommandPacket(CommandPacket.HAND_SHAKE, "refused"));
			server.removeSession(id);
			return;
		}
		this.SendCommand(new CommandPacket(CommandPacket.HAND_SHAKE, "accepted"));
		startUDP(WorkerRunnable);
		SendCommand(new CommandPacket(CommandPacket.UDP_PORT, udp_port));
	}

	public void swithctoserver(LoginServer server)
	{
		this.server = server;
	}
	public void setpedhandle(int pedhandle) {
		this.pedhandle = pedhandle;
	}

	public boolean startTCP(ExecutorService WorkerRunnable) {
		tcp = new WorkerRunnableTCP(this);
		if (!tcp.init())
			return false;
		WorkerRunnable.execute(tcp);
		LOGGER.log(Level.INFO, "TCP running", this);
		return true;
	}

	public void handshake() {
		SendCommand(new CommandPacket(CommandPacket.UDP_PORT, udp_port));
	}

	public void startUDP(ExecutorService WorkerRunnable) {
		udp = new WorkerRunnableUDP(this);
		udp.init();
		WorkerRunnable.execute(udp);
		LOGGER.log(Level.INFO, "UDP running", this);
	}

	public void disconnect() {
		if (!connected) {
			return;
		}
		LOGGER.log(Level.INFO, "Client " + id + " has disconnected...", this);
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
		if (tcp != null && packet != null && connected)
			tcp.sendCommand(packet);
	}
}
