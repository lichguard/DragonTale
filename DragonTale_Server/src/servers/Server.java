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

public class Server {
	private static final int MAX_CONNECTIONS = 10;
	protected int serverPort = 9000;
	protected ServerSocket serverSocket = null;
	protected boolean running = false;
	protected Thread runningThread = null;
	protected ExecutorService WorkerRunnable = Executors.newFixedThreadPool(2 * MAX_CONNECTIONS);
	
	public HashMap<UUID, Session> sessions = new HashMap<UUID, Session>(MAX_CONNECTIONS);
	public Queue<CommandPacket> commandsPackets = new LinkedList<CommandPacket>();
	public Stack<WorldPacket> worldPackets = new Stack<WorldPacket>();

	public void addSession(UUID sessionid, Session session) {
		synchronized (sessions) {
			sessions.put(sessionid, session);
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
	
	public boolean isRunning() {
		return this.running;
	}

	public void removeSession(UUID sessionid) {
		if (sessions.containsKey(sessionid)) {
			synchronized (sessions) {
				sessions.remove(sessionid);
			}
		}
	}

}