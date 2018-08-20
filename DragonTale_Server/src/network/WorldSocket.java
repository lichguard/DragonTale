package network;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;

import PACKET.CommandPacket;
import PACKET.WorldPacket;
import main.LOGGER;

public class WorldSocket {
	public WorldSession m_session = null;
	public UUID id = null;
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
	
	 boolean ProcessIncomingData(CommandPacket packet) {
		return false;
	 
	 }
	 boolean ProcessIncomingData(WorldPacket packet) {
		return false;
	 
	 }
	 
		public WorldSocket(Socket clientSocket) throws Exception {
			this.clientSocket = clientSocket;
			id = UUID.randomUUID();
			this.udp_port = udp_port_inc++;
		}

		public void setpedhandle(int pedhandle) {
			this.pedhandle = pedhandle;
		}

		public void establishConnection() throws Exception {
			tcp = new TCPSocket(this);
			tcp.start();
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

		
	/*
	 boolean ProcessIncomingData() {
		
		    try
		    {
		        switch (opcode)
		        {
		            case CMSG_AUTH_SESSION:
		                if (m_session)
		                {
		                    sLog.outError("WorldSocket::ProcessIncomingData: Player send CMSG_AUTH_SESSION again");
		                    return false;
		                }

		                return HandleAuthSession(*pct);

		            case CMSG_PING:
		                return HandlePing(*pct);

		            case CMSG_KEEP_ALIVE:
		                DEBUG_LOG("CMSG_KEEP_ALIVE ,size: " SIZEFMTD " ", pct->size());

		                return true;

		            default:
		            {
		                if (!m_session)
		                {
		                    sLog.outError("WorldSocket::ProcessIncomingData: Client not authed opcode = %u", uint32(opcode));
		                    return false;
		                }

		                m_session->QueuePacket(pct);

		                return true;
		            }
		        }
		    }
		    catch (ByteBufferException&)
		    {
		        sLog.outError("WorldSocket::ProcessIncomingData ByteBufferException occured while parsing an instant handled packet (opcode: %u) from client %s, accountid=%i.",
		                      opcode, GetRemoteAddress().c_str(), m_session ? m_session->GetAccountId() : -1);

		        if (sLog.HasLogLevelOrHigher(LOG_LVL_DEBUG))
		        {
		            DEBUG_LOG("Dumping error-causing packet:");
		            pct->hexlike();
		        }

		        if (sWorld.getConfig(CONFIG_BOOL_KICK_PLAYER_ON_BAD_PACKET))
		        {
		            DETAIL_LOG("Disconnecting session [account id %i / address %s] for badly formatted packet.",
		                       m_session ? m_session->GetAccountId() : -1, GetRemoteAddress().c_str());

		            return false;
		        }
		    }

		 
		 
		 
		 
		 
		 
		};
	 boolean HandleAuthSession(WorldPacket recvPacket) {
		return false;
		};
		*/
}
