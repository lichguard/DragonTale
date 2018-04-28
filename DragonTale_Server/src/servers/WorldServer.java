package servers;
import java.util.Map;
import java.util.UUID;

import PACKET.CommandPacket;
import PACKET.WorldPacket;

public class WorldServer extends Server {

	public WorldServer() {

	}

	public void run() {

	}

	public void addSession(UUID sessionid, Session session)
	{
		sessions.put(sessionid, session);
	}
	public void removeSession(UUID sessionid)
	{
		this.removesession(sessionid);
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

}
