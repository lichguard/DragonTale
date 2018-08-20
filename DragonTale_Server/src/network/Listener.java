package network;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import PACKET.CommandPacket;
import PACKET.WorldPacket;
import main.LOGGER;

public class Listener implements Runnable {

	private static final int MAX_CONNECTIONS = 10;
	protected int serverPort = 9000;
	protected ServerSocket serverSocket = null;
	protected Thread runningThread = null;
	protected ExecutorService WorkerRunnable = Executors.newFixedThreadPool(3 * MAX_CONNECTIONS);
	public ConcurrentHashMap<UUID, WorldSession> sessions = new ConcurrentHashMap<UUID, WorldSession>(MAX_CONNECTIONS);
	public ConcurrentHashMap<UUID, WorldSession> newSessions = new ConcurrentHashMap<UUID, WorldSession>(MAX_CONNECTIONS);
	
	public Queue<CommandPacket> commandsPackets = new ConcurrentLinkedQueue<CommandPacket>();
	public ConcurrentLinkedDeque<WorldPacket> worldPackets = new ConcurrentLinkedDeque<WorldPacket>();
	private static volatile Object syncroot = new Object();
	private static Listener instance = null;
	
	////////////MANAGING SERVER///////////////////////
	public static Listener getInstance() {
		if (instance == null) {
			synchronized (syncroot) {
				if (instance == null) {
					instance = new Listener(9000);
				}
			}
		}
		return instance;
	}
	
	private Listener(int port) {
		this.serverPort = port;
	}

	public void start() {
		if (runningThread != null && runningThread.isAlive()) {
			LOGGER.log(Level.INFO, "Server instance already running...", this);	
			return;
		}
		runningThread = new Thread(this);
		runningThread.start();
	}
	
	public void shutdown() {
		
		//closed the server socket
		if (this.serverSocket != null && !this.serverSocket.isClosed()) {
			try {
				this.serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		while (sessions.size() > 0) {
			this.removeSession(sessions.entrySet().iterator().next().getKey());
		}
		
		//closed the running workers
		if (this.WorkerRunnable != null && !this.WorkerRunnable.isShutdown()) {
			this.WorkerRunnable.shutdown();
		}
		
		//closes the server thread
		if (this.runningThread != null && this.runningThread != Thread.currentThread()) {
			if (this.runningThread.isAlive()) {
				this.runningThread.interrupt();
			}
		}
		

		LOGGER.log(Level.INFO, "Server Stopped", this);
	}
	
	public void run() {

		LOGGER.log(Level.INFO, "Initializing server...", this);	
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			LOGGER.log(Level.INFO, "Failed to start server...", this);
			runningThread.interrupt();
		}

		while (serverSocket != null && !serverSocket.isClosed()) {
			LOGGER.log(Level.INFO, "Waiting for new client...", this);
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
				clientSocket.setReuseAddress(true);
				addSession(clientSocket);

			} catch (SocketException e) {
				LOGGER.log(Level.INFO, "Closing server thread since the server socket has been closed!", this);
				break;
			} catch (IOException e) {
				LOGGER.log(Level.INFO, "Server thread encounterd IOException. ---- PRINTING STACK TRACE:", this);
				e.printStackTrace();
				break;
			}
		}
		
		this.shutdown();
	}

	
	////////////   UTILITY///////////////////////
	public void addSession(Socket clientSocket)
	{
		WorldSession session =  null;
		try {
			session = new WorldSession(clientSocket);
			synchronized (syncroot) {
				newSessions.put(session.id, session);
				session.establishConnection();
				sessions.put(session.id, newSessions.remove(session.id));
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.disconnect();
			//removeSession(session.id);
		}
	}
	
	public void removeSession(UUID sessionid)
	{
		WorldSession deletedSession = null;
		WorldSession deletednewSession = null;
		synchronized (syncroot) {
			deletedSession = sessions.remove(sessionid);
			deletednewSession = newSessions.remove(sessionid);
		}
		
		if (deletedSession != null)
			deletedSession.disconnect();
		
		if (deletednewSession != null)
			deletednewSession.disconnect();
	}
	
	public void SendCommand(UUID to, CommandPacket data) {
		sessions.get(to).SendCommand(data);
	}

	public void SendWorldPacket(UUID to, WorldPacket packet) {
		sessions.get(to).SendWorldPacket(packet);
	}
	
	public void BroadcastCommand(CommandPacket packet) {
		synchronized (syncroot) {
			for (Map.Entry<UUID, WorldSession> session : sessions.entrySet()) {
				SendCommand(session.getKey(), packet);
			}
		}
	}
	
	public void BroadWorldPacket(WorldPacket packet) {
		synchronized (syncroot) {
			for (Map.Entry<UUID, WorldSession> session : sessions.entrySet()) {
				SendWorldPacket(session.getKey(), packet);
			}
		}
	}

	public void setsessionpedhandle(UUID sessionid, int pedhandle)
	{
			sessions.get(sessionid).setpedhandle(pedhandle);
	}

}