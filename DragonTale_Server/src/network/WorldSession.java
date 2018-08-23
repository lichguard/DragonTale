package network;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;

import Entity.Enemies.PlayerPED;
import PACKET.CommandPacket;
import PACKET.WorldPacket;
import main.LOGGER;

public class WorldSession {
	
	enum SessionStatus
	{
	    STATUS_AUTHED,                                      ///< Player authenticated (_player==nullptr, m_playerRecentlyLogout = false or will be reset before handler call)
	    STATUS_LOGGEDIN,                                        ///< Player in game (_player!=nullptr, inWorld())
	    STATUS_TRANSFER,                                        ///< Player transferring to another map (_player!=nullptr, !inWorld())
	    STATUS_LOGGEDIN_OR_RECENTLY_LOGGEDOUT,                  ///< _player!= nullptr or _player==nullptr && m_playerRecentlyLogout)
	    STATUS_NEVER,                                           ///< Opcode not accepted from client (deprecated or server side only)
	    STATUS_UNHANDLED                                        ///< We don' handle this opcode yet
	};
	
	enum PacketProcessing
	{
	    PROCESS_INPLACE,                                    // process packet whenever we receive it - mostly for non-handled or non-implemented packets
	    PROCESS_THREADUNSAFE,                                   // packet is not thread-safe - process it in World::UpdateSessions()
	    PROCESS_THREADSAFE                                      // packet is thread-safe - process it in Map::Update()
	};
	
	class OpcodeHandler
	{
	    String name;
	    SessionStatus status;
	    PacketProcessing packetProcessing;
	    
	    //(WorldSession::*handler) ->(WorldPacket& recvPacket);
	}
	
	 void StoreOpcode(Runnable toRun)
     {
			StoreOpcode(WorldSession::test);
     }
	 public static void  test() {
		 
	 }
	 
	ConcurrentLinkedDeque<CommandPacket> m_recvQueue;
	int m_latency;
	boolean m_playerLogout;
	boolean m_playerRecentlyLogout;
	boolean m_playerSave;

	long _logoutTime;
	boolean m_inQueue; // session wait in auth.queue
	boolean m_playerLoading;

	// AccountTypes _security;
	PlayerPED _player;

	public void ExecuteOpcode() {
		
	
		
	}

      
     int _accountId;

	public WorldSocket worldsocket = null;
	
	public Integer id = null;
	public Socket clientSocket = null;
	
	protected int packet_size = 500;
	public String accountName = null;
	public boolean connected = true;
	public int AccountID = -1;
	public int pedhandle = -1;
	
	public TCPSocket tcp = null;
	public UDPSocket udp = null;
	public int udp_port = -1;
	public static int udp_port_inc = 1;

	/*
	 * HANDLERS FOR EVERYTHING
	 */
	
	public void QueuePacket(CommandPacket pck) {
		m_recvQueue.add(pck);
	}
	public WorldSession(Socket clientSocket) throws Exception {
		this.clientSocket = clientSocket;
		id = UUID.randomUUID();
		this.udp_port = udp_port_inc++;
	}

	public boolean Update() {
		return true;
	}
	public void setpedhandle(int pedhandle) {
		this.pedhandle = pedhandle;
	}

	public void establishConnection() throws Exception {
		//tcp = new TCPSocket(this);
		//tcp.start();
		//udp = new UDPSocket(this);
		//udp.start();
	}
	
	public void disconnect() {
		LOGGER.log(Level.INFO, "Client " + id + " has disconnected...", this);
		Listener.getInstance().commandsPackets.add(new CommandPacket(CommandPacket.DESPAWN, this.pedhandle));
		
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
