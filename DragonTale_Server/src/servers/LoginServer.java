package servers;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import PACKET.CommandPacket;
import PACKET.WorldPacket;


public class LoginServer implements Runnable {

	private static final int MAX_CONNECTIONS = 10;
	protected int serverPort = 9000;
	protected ServerSocket serverSocket = null;
	protected boolean running = false;
	protected Thread runningThread = null;
	protected ExecutorService WorkerRunnable = Executors.newFixedThreadPool(2 * MAX_CONNECTIONS);
	public HashMap<UUID, Session> sessions = new HashMap<UUID, Session>(MAX_CONNECTIONS);

	public Queue<CommandPacket> commandsPackets = new LinkedList<CommandPacket>();
	public Stack<WorldPacket> worldPackets = new Stack<WorldPacket>();

	public LoginServer(int port) {
		this.serverPort = port;
	}

	public void run() {

		System.out.println("Initializing server...");
		this.runningThread = Thread.currentThread();
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			return;
		}
		running = true;
		System.out.println("Waiting for clients...");
		while (running) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
				clientSocket.setReuseAddress(true);
				Session session = new Session(clientSocket, WorkerRunnable, this);
				addSession(session.id, session);
				
			} catch (IOException e) {
				if (running)
					throw new RuntimeException("Error accepting client connection", e);
			}
		}
		this.WorkerRunnable.shutdown();
		System.out.println("Server Stopped");
	}

	public void addSession(UUID sessionid, Session session)
	{
		synchronized (sessions) {
		sessions.put(sessionid, session);
		}
	}
	
	public void removeSession(UUID sessionid)
	{
		synchronized (sessions) {
		this.removesession(sessionid);
		}
	}
	
	public void SendCommand(UUID to, CommandPacket data) {
		sessions.get(to).SendCommand(data);
	}

	public void SendWorldPacket(UUID to, WorldPacket packet) {
		sessions.get(to).SendWorldPacket(packet);
	}
	
	public void BroadcastCommand(CommandPacket packet) {
		synchronized (sessions) {
			for (Map.Entry<UUID, Session> session : sessions.entrySet()) {
				SendCommand(session.getKey(), packet);
			}
		}
	}
/*
	public void BroadcastWorldPacket(WorldPacket packet,ArrayList<ENTITY> entities) {
		synchronized (sessions) {
			for (Session session : sessions.values()) {
				if (session.pedhandle)
					
					SendWorldPacket(session.id, packet);
			}
		}
	}
*/
	public boolean isRunning() {
		return this.running;
	}
	public void setsessionpedhandle(UUID sessionid, int pedhandle)
	{
		synchronized (sessions) {
			sessions.get(sessionid).setpedhandle(pedhandle);
		}
	}
	public  void removesession(UUID sessionid) {

		if (sessions.containsKey(sessionid)) {
			Session session = sessions.get(sessionid);
			synchronized (sessions) {
				sessions.remove(sessionid);
			}
			session.disconnect();
			synchronized (commandsPackets) {
				commandsPackets.add(new CommandPacket(CommandPacket.DESPAWN, session.pedhandle));
			}
		}
	}

	public void stop() {
		if (!running) {
			System.out.println("Server is not running, can't stop");
			return;
		}

		running = false;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
		synchronized (sessions) {
			Set<UUID> keys = sessions.keySet();
			for (UUID key : keys) {
				removesession(key);
			}
		}
	}
	


}