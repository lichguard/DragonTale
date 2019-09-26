package network;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

import PACKET.MovementData;
import PACKET.NetworkSpawner;
import PACKET.WorldPacket;
import componentNew.Animation;
import componentNew.Broadcast;
import componentNew.EntityManager;
import componentNew.Network;
import componentNew.Position;
import database.Account;
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
	int _accountId;
	public WorldSocket worldsocket = null;
	public UUID id = null;
	protected int packet_size = 500;
	public String accountName = null;
	public int AccountID = -1;

	public void savePlayer() {
		if (account != null && _playerid != -1) {

			Position poscomponent = (Position) EntityManager.getInstance().getEntityComponent(_playerid,
					EntityManager.PositionID);
			account.x = poscomponent.x;
			account.y = poscomponent.y;
			Animation anicomponent = (Animation) EntityManager.getInstance().getEntityComponent(_playerid,
					EntityManager.AnimationID);
			account.facingright = anicomponent.facingRight;
		}
	}

	public void QueuePacket(WorldPacket packet) {
		if (WorldPacket.MOVEMENT_DATA == packet.packet_code) {
			m_location = packet;
		} else {
			synchronized (m_recvQueue) {
				m_recvQueue.add(packet);
			}
		}
	}

	public WorldSession(WorldSocket worldsocket) {
		this.worldsocket = worldsocket;
	}

	public boolean Update() {

		if (worldsocket.clientSocket == null || worldsocket.clientSocket.isClosed())
			return false;

		if (m_recvQueue.size() > 0) {
			synchronized (m_recvQueue) {
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
		}
		Network.setNewPacket(_playerid,(MovementData) m_location.data);

		return true;
	}

	public void SendWorldPacket(WorldPacket packet) {
		worldsocket.SendWorldPacket(packet);
	}

	/*
	 * HANDLERS FOR EVERYTHING
	 */

	public void handleMeleecommand(WorldPacket packet) {
		// _player.setmeleeattack();
	}

	public void handleLogin(WorldPacket packet) {
		LOGGER.info("handleLogin", this);

		int handle = World.getInstance().requestObjectSpawn(account.name, 0, account.x, account.y,
				false, 1, worldsocket); // 0 - playerped
		
		SendWorldPacket(new WorldPacket(WorldPacket.LOGIN, handle));

	}

	public void handlechat(WorldPacket packet) {
		Broadcast.QueuePacket(_playerid, packet, true);

		// Broadcast.BroadcastData0
		// _player.broadcaster.QueuePacket(packet, true, -1);

	}

	public void handlespawncommand(WorldPacket packet) {
		World.getInstance().requestObjectSpawn((NetworkSpawner) packet.data); // 0 - playerped
	}

	public void Despawn() {
		// TODO Auto-generated method stub
		
	}

}
