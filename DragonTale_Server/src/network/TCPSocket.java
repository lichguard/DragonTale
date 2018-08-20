package servers;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.io.EOFException;
import java.io.IOException;

import PACKET.CommandPacket;
import database.MySQLDB;
import database.IDB;
import main.LOGGER;

public class WorkerRunnableTCP implements Runnable {
	protected Session session;
	protected InputStream inputStream = null;
	protected OutputStream outStream = null;
	protected String username;
	protected String password;
	protected ObjectInputStream objectInput = null;
	protected ObjectOutputStream objectOutput = null;
	public WorkerRunnableTCP(Session session) {
		this.session = session;
	}

	public void start() throws Exception {
		
		try {
			outStream = session.clientSocket.getOutputStream();
			inputStream = session.clientSocket.getInputStream();

			objectOutput = new ObjectOutputStream(outStream);
			objectInput = new ObjectInputStream(inputStream);

			CommandPacket packet = (CommandPacket) objectInput.readObject();
			if (packet.packet_code == CommandPacket.HAND_SHAKE) {
				String[] userdata = ((String) packet.data).split(",", 2);

				this.session.AccountID = 0;
				if (!userdata[0].equals("admin") && userdata[1].equals("admin")) {
					IDB db = MySQLDB.getInstance();
					this.session.AccountID =  db.GetUserfromDB(userdata[0], userdata[1]);
					if (this.session.AccountID == -1) {
						session.SendCommand(new CommandPacket(CommandPacket.HAND_SHAKE, "refused"));
						throw new Exception("Invalid account!");
					}
				}
				
				session.SendCommand(new CommandPacket(CommandPacket.HAND_SHAKE, "accepted"));
				session.SendCommand(new CommandPacket(CommandPacket.UDP_PORT,session.udp_port));
				
				Server.getInstance().WorkerRunnable.execute(this);
			} else {
				throw new Exception("Expected HAND_SHAKE packet!");
			}
		} catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		}	
	}

	public void run() {
		try {
			while (session.clientSocket != null && !session.clientSocket.isClosed()) {
				CommandPacket packet;
				packet = (CommandPacket) objectInput.readObject();
				packet.session_id = session.id;
				LOGGER.log(Level.INFO, " IN-(" + session.pedhandle  + ") [" + packet.toString() + "]", this);
				
				Server.getInstance().commandsPackets.add(packet);
			}

		} catch (EOFException e) {
		} 
		catch (ClassNotFoundException | IOException e) {
			if (session.clientSocket != null && !session.clientSocket.isClosed()) {
				//LOGGER.log(Level.SEVERE, "An error has occured with client---- PRINTING STACK TRACE:", this);
				//e.printStackTrace();
			}
		}
		Server.getInstance().removeSession(session.id);
	}

	public void sendCommand(CommandPacket packet) {
		LOGGER.log(Level.INFO, "OUT-(" + session.pedhandle + ") [" + packet.toString() + "]", this);
		try {
			objectOutput.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
			Server.getInstance().removeSession(session.id);
		}
	}

	public void disconnect() {

		try {
			if (objectInput != null)
				objectInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (objectOutput != null)
				objectOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (inputStream != null)
				inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (outStream != null)
				outStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}