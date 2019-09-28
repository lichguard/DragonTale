package network;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import PACKET.AuthorizationPacket;
import PACKET.WorldPacket;
import database.Account;
import database.MySQLDB;
import game.World;
import main.LOGGER;

public class WorldSocket {

	public UUID id = null;
	public Socket clientSocket = null;
	protected int packet_size = 500;

	public WorldSession m_session = null;
	public TCPSocket tcp = null;
	public UDPSocket udp = null;

	public int udp_port = -1;
	public static int udp_port_inc = 1;

	public WorldSocket(Socket clientSocket) {
		id = UUID.randomUUID();
		this.clientSocket = clientSocket;
		udp_port = udp_port_inc++;
	}

	public void start() throws Exception {
		tcp = new TCPSocket(this);
		tcp.start();
		
		//udp = new UDPSocket(this,udp_port);
		//udp.start();
	}

	public void disconnect() {
		 LOGGER.info("Disconnecting client " + id + "...", this);

		if (m_session != null) {
			m_session.savePlayer();
			m_session.Despawn();
		}
		
		try {
			if (clientSocket != null) {
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

	public boolean SendWorldPacket(WorldPacket packet) {
		if (packet.packet_code == WorldPacket.MOVEMENT_DATA) {
			if (udp != null && packet != null)
				return udp.sendWorldPacket(packet);
		} else {
			LOGGER.info("SENDING DATA: " + packet.getCommandName(), this);
			if (tcp != null && packet != null)
				return tcp.sendPacket(packet);
		}
		return false;
	}

	public boolean ProcessIncomingData(WorldPacket pct) {
		try {
			
			if (pct.packet_code != WorldPacket.MOVEMENT_DATA) 
				LOGGER.info("INC:: " + pct.toString(), this);
			
			switch (pct.packet_code) {
			case WorldPacket.HAND_SHAKE:
				if (m_session != null) {
					LOGGER.error("WorldSocket::ProcessIncomingData: Player send CommandPacket.HAND_SHAKE again", this);
					return false;
				}
				return HandleAuthSession(pct);
			case WorldPacket.PING_REQUEST:
				return HandlePing(pct);
			case WorldPacket.REQUEST_UDP_PORT:
				return HandleUDP(pct);
			default:
				if (m_session == null) {
					LOGGER.error("WorldSocket::ProcessIncomingData: Client not authed opcode: " + pct.getCommandName(),this);
					return false;
				}
				m_session.QueuePacket(pct);
				return true;
			}
		} catch (Exception e) {
			System.out.println("NOT IMPLMENETED ERROR MSG");
			e.printStackTrace();
			// sLog.outError("WorldSocket::ProcessIncomingData ByteBufferException occured
			// while parsing an instant handled packet (opcode: %u) from client %s,
			// accountid=%i.",
			// opcode, GetRemoteAddress().c_str(), m_session ? m_session->GetAccountId() :
			// -1);

			// DETAIL_LOG("Disconnecting session [account id %i / address %s] for badly
			// formatted packet.",
			// m_session ? m_session->GetAccountId() : -1, GetRemoteAddress().c_str());

			return false;
		}

	}


	boolean HandleAuthSession(WorldPacket recvPacket) {
		AuthorizationPacket pct = (AuthorizationPacket) recvPacket.data;
		Account acc = MySQLDB.getInstance().GetUserAccountfromDB(pct.username, pct.password);
		if (acc == null) {
			SendWorldPacket(new WorldPacket(WorldPacket.HAND_SHAKE, "refused"));
			return false;
		}
		
		m_session = new WorldSession(this);
		m_session.account = acc;
		//m_session.AccountID = Accountid;
		m_session.id = id;
		
		//LOGGER.error("NOT IMPLMENETED YET!", this);
		World.getInstance().AddSession(m_session);
		SendWorldPacket(new WorldPacket(WorldPacket.HAND_SHAKE, "accepted"));
		//TODO: Implement refusal
		//SendWorldPacket(new WorldPacket(WorldPacket.HAND_SHAKE, "refused"));

		return true;
	}

	boolean HandlePing(WorldPacket recvPacket) {
		System.out.println("NOT IMPLMENETED");
		return true;
	}

	boolean HandleUDP(WorldPacket recvPacket) {

		if (udp != null) {
			return false;
		}

		udp = new UDPSocket(this, udp_port);
		try {
			udp.start();
			SendWorldPacket(new WorldPacket(WorldPacket.UDP_PORT, udp_port));
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return false;
	}

}
