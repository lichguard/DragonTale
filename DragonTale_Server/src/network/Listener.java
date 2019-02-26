package network;
import java.util.UUID;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import main.LOGGER;

public class Listener implements Runnable {

	private static final int MAX_CONNECTIONS = 10;
	protected int serverPort = 9000;
	protected ServerSocket serverSocket = null;
	protected Thread runningThread = null;
	protected ExecutorService WorkerRunnable = null;
	protected ConcurrentMap<UUID, WorldSocket> worldsockets = null;
	
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
		WorkerRunnable = Executors.newFixedThreadPool(3 * MAX_CONNECTIONS);
		worldsockets = new ConcurrentHashMap<UUID, WorldSocket>();
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
		LOGGER.info("Shuting down listener...", this);
		if (runningThread != null && runningThread.isAlive() && !runningThread.isInterrupted())
			runningThread.interrupt();
		
		//closed the server socket
		if (this.serverSocket != null && !this.serverSocket.isClosed()) {
			try {
				this.serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//closed the running workers
		if (this.WorkerRunnable != null && !this.WorkerRunnable.isShutdown()) {
			this.WorkerRunnable.shutdown();
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

		while (serverSocket != null && !serverSocket.isClosed() && !this.runningThread.isInterrupted()) {
			LOGGER.log(Level.INFO, "Waiting for new client...", this);
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
				clientSocket.setReuseAddress(true);
				WorldSocket ws = new WorldSocket(clientSocket);
				worldsockets.put(ws.id, ws);
				try {
					ws.start();
				} catch (Exception e) {
					ws.disconnect();
					worldsockets.remove(ws.id);
					LOGGER.log(Level.INFO, "removing the client!", this);
					e.printStackTrace();
				}
			} catch (SocketException e) {
				LOGGER.log(Level.INFO, "Closing server thread since the server socket has been closed!", this);
				break;
			} catch (IOException e) {
				LOGGER.log(Level.INFO, "Server thread encounterd IOException. ---- PRINTING STACK TRACE:", this);
				e.printStackTrace();
				break;
			}
		}
		
		
	}



}