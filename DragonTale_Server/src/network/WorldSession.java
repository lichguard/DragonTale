package network;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import javax.swing.text.html.parser.Entity;

import PACKET.MovementData;
import PACKET.NetworkSpawner;
import PACKET.WorldPacket;
import componentNew.Animation;
import componentNew.EntityManager;
import componentNew.IComponent;
import componentNew.Network;
import componentNew.Position;
import database.Account;
import game.World;
import main.LOGGER;
import objects.Player;
import objects.Spawner;

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
	public int _playerid = -1;
	public Account account = null;
	
	public void savePlayer() {
		if (account != null && _playerid != -1) {

			Position poscomponent = (Position)EntityManager.getInstance().getEntityComponent(_playerid, EntityManager.PositionID);
			account.x = poscomponent.x;
			account.y = poscomponent.y;
			 Animation anicomponent = (Animation)EntityManager.getInstance().getEntityComponent(_playerid, EntityManager.AnimationID);
			 account.facingright = anicomponent.facingRight;
		}
	} 
	
	public void ExecuteOpcode() {
	}

	int _accountId;

	public WorldSocket worldsocket = null;

	public UUID id = null;
	protected int packet_size = 500;
	public String accountName = null;

	public int AccountID = -1;

	public void handleLogin(WorldPacket packet) {
		LOGGER.info("handleLogin", this);
		int handle = World.getInstance().requestObjectSpawn(account.name, Spawner.PLAYERPED, account.x, account.y, false, worldsocket); //0 - playerped
		SendWorldPacket(new WorldPacket(WorldPacket.LOGIN, 
				new NetworkSpawner(account.name,handle, Spawner.PLAYERPED,  account.x,  account.y, account.facingright, 1)
				));
		
		
	}
	
	public void handlechat(WorldPacket packet) {
		
		_player.broadcaster.QueuePacket(packet, true, -1);
		
	}
	
	public void handlespawncommand(WorldPacket packet) {
		World.getInstance().requestObjectSpawn((NetworkSpawner) packet.data); //0 - playerped
	}
	
	
	/*
	 * HANDLERS FOR EVERYTHING
	 */

	public void QueuePacket(WorldPacket packet) {
		if (WorldPacket.MOVEMENT_DATA == packet.packet_code) {
			m_location = packet;
		} else
			m_recvQueue.add(packet);
	}

	public WorldSession(WorldSocket worldsocket) {
		this.worldsocket = worldsocket;
	}

	public boolean Update() {
		
		if (worldsocket.clientSocket == null || worldsocket.clientSocket.isClosed())
			return false;
		
		synchronized (m_recvQueue){
			while (!m_recvQueue.isEmpty()) {
				WorldPacket packet = m_recvQueue.pop();
				switch (packet.packet_code) {
				case WorldPacket.LOGIN:
					handleLogin(packet);
					break;
				case WorldPacket.SPEECH:
					handlechat(packet);
					break;
				case WorldPacket.SPAWN_COMMAND:
					handlespawncommand(packet);
					break;
				case WorldPacket.SPAWN:
					handlespawncommand(packet);
					break;
				case WorldPacket.MELEE_ATTACK:
					handleMeleecommand(packet);
					break;

				default:
					LOGGER.warn("Unknown CommandPacket code: " + packet.packet_code, this);
					break;
				}
			}
			
		}
		if (_player != null) {
			
			if (m_location != null)
				_player.updatePacket((MovementData)m_location.data);
			_player.broadcaster.ProcessQueue(0);
		}
		return true;
	}

	
	public void handleMeleecommand(WorldPacket packet) {
		_player.setmeleeattack();
	}
	

	public void SendWorldPacket(WorldPacket packet) {
		worldsocket.SendWorldPacket(packet);
	}
}
