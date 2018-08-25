package network;

import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import Entity.Player;
import PACKET.MovementData;
import PACKET.NetworkSpawner;
import PACKET.WorldPacket;
import game.World;
import main.LOGGER;

public class WorldSession {

	enum SessionStatus {
		STATUS_AUTHED, /// < Player authenticated (_player==nullptr, m_playerRecentlyLogout = false or
						/// will be reset before handler call)
		STATUS_LOGGEDIN, /// < Player in game (_player!=nullptr, inWorld())
		STATUS_TRANSFER, /// < Player transferring to another map (_player!=nullptr, !inWorld())
		STATUS_LOGGEDIN_OR_RECENTLY_LOGGEDOUT, /// < _player!= nullptr or _player==nullptr && m_playerRecentlyLogout)
		STATUS_NEVER, /// < Opcode not accepted from client (deprecated or server side only)
		STATUS_UNHANDLED /// < We don' handle this opcode yet
	};

	enum PacketProcessing {
		PROCESS_INPLACE, // process packet whenever we receive it - mostly for non-handled or
							// non-implemented packets
		PROCESS_THREADUNSAFE, // packet is not thread-safe - process it in World::UpdateSessions()
		PROCESS_THREADSAFE // packet is thread-safe - process it in Map::Update()
	};

	class OpcodeHandler {
		String name;
		SessionStatus status;
		PacketProcessing packetProcessing;

		// (WorldSession::*handler) ->(WorldPacket& recvPacket);
	}

	void StoreOpcode(Runnable toRun) {

	}

	Deque<WorldPacket> m_recvQueue = new ArrayDeque<WorldPacket>();
	WorldPacket m_location;

	int m_latency;
	boolean m_playerLogout;
	boolean m_playerRecentlyLogout;
	boolean m_playerSave;

	long _logoutTime;
	boolean m_inQueue; // session wait in auth.queue
	boolean m_playerLoading;

	// AccountTypes _security;
	public Player _player;

	public void ExecuteOpcode() {
	}

	int _accountId;

	public WorldSocket worldsocket = null;

	public UUID id = null;
	public Socket clientSocket = null;

	protected int packet_size = 500;
	public String accountName = null;
	public boolean connected = true;
	public int AccountID = -1;

	public void handleLogin(WorldPacket packet) {
		int handle = World.getInstance().spawn_entity(0, 10, 20, false, true,worldsocket); //0 - playerped
		SendWorldPacket(new WorldPacket(WorldPacket.LOGIN, 
					new MovementData(handle, 10, 20, false)
				));
		
	}
	
	public void handlechat(WorldPacket packet) {
		_player.broadcaster.QueuePacket(packet, true, -1);
		
	}
	
	public void handlespawncommand(WorldPacket packet) {
		World.getInstance().spawn_entity((NetworkSpawner) packet.data); //0 - playerped
	}
	
	
	/*
	 * HANDLERS FOR EVERYTHING
	 */

	public void QueuePacket(WorldPacket packet) {
		if(WorldPacket.MOVEMENT_DATA == packet.packet_code) {
			m_location = packet;
		}
		else
		m_recvQueue.add(packet);
	}

	public WorldSession(WorldSocket worldsocket) {
		this.worldsocket = worldsocket;
	}

	public boolean Update() {
		
		if (_player == null)
			return true;
		
		_player.updatePacket((MovementData)m_location.data);
		synchronized (m_recvQueue){
			while (!m_recvQueue.isEmpty()) {
				WorldPacket packet = m_recvQueue.pop();
				switch (packet.packet_code) {
				case WorldPacket.LOGIN:
					handleLogin(packet);
					break;
				case WorldPacket.CHAT:
					handlechat(packet);
					break;
				case WorldPacket.SPAWN_COMMAND:
					handlespawncommand(packet);
					break;

				default:
					LOGGER.warn("Unknown CommandPacket code: " + packet.packet_code, this);
					break;
				}
			}
			
		}
		_player.broadcaster.ProcessQueue(0);
		return true;
	}

	

	public void SendWorldPacket(WorldPacket packet) {
		worldsocket.SendWorldPacket(packet);
	}
}
