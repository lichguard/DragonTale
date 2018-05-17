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

	public boolean init() {
		// connect_to_mysql();
		try {
			outStream = session.clientSocket.getOutputStream();
			inputStream = session.clientSocket.getInputStream();

			objectOutput = new ObjectOutputStream(outStream);
			objectInput = new ObjectInputStream(inputStream);

			CommandPacket packet = (CommandPacket) objectInput.readObject();
			if (packet.packet_code == CommandPacket.HAND_SHAKE) {
				String[] userdata = ((String) packet.data).split(",", 2);

				if (userdata[0].equals("admin") && userdata[1].equals("admin")) {
					this.session.AccountID = 0;
					return true;
				}
				
				IDB db = MySQLDB.getInstance();
				this.session.AccountID =  db.GetUserfromDB(userdata[0], userdata[1]);

				return (this.session.AccountID != -1);
			} else {
				
				LOGGER.log(Level.WARNING,"Expected HAND_SHAKE packet!",this);
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void run() {
		try {
			while (session.connected) {
				CommandPacket packet;
				packet = (CommandPacket) objectInput.readObject();
				packet.session_id = session.id;
				LOGGER.log(Level.INFO, "Reveiving command: " + packet.getCommandName(), this);
				synchronized (session.server.commandsPackets) {
					session.server.commandsPackets.add(packet);
				}
			}

		} catch (EOFException e) {
		} catch (ClassNotFoundException | IOException e) {
			if (session.connected) {
				e.printStackTrace();
				LOGGER.log(Level.SEVERE, "An error has occured with client..", this);
			}
		}

		session.server.removesession(session.id);
	}

	public void sendCommand(CommandPacket packet) {
		LOGGER.log(Level.INFO, "Sending command: " +  packet.getCommandName(), this);
		try {
			objectOutput.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
			session.server.removesession(session.id);
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