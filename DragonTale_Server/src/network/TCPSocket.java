package network;

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

public class TCPSocket implements Runnable {
	protected WorldSocket worldsocket = null;
	protected InputStream inputStream = null;
	protected OutputStream outStream = null;
	protected String username;
	protected String password;
	protected ObjectInputStream objectInput = null;
	protected ObjectOutputStream objectOutput = null;
	
	public TCPSocket(WorldSocket worldsocket) {
		this.worldsocket = worldsocket;
	}

	public void start() throws Exception {
		
		try {
			outStream = worldsocket.clientSocket.getOutputStream();
			inputStream = worldsocket.clientSocket.getInputStream();

			objectOutput = new ObjectOutputStream(outStream);
			objectInput = new ObjectInputStream(inputStream);

			CommandPacket packet = (CommandPacket) objectInput.readObject();
			if (packet.packet_code == CommandPacket.HAND_SHAKE) {
				String[] userdata = ((String) packet.data).split(",", 2);

				this.worldsocket.AccountID = 0;
				if (!userdata[0].equals("admin") && userdata[1].equals("admin")) {
					IDB db = MySQLDB.getInstance();
					this.worldsocket.AccountID =  db.GetUserfromDB(userdata[0], userdata[1]);
					if (this.worldsocket.AccountID == -1) {
						sendCommand(new CommandPacket(CommandPacket.HAND_SHAKE, "refused"));
						throw new Exception("Invalid account!");
					}
				}
				
				sendCommand(new CommandPacket(CommandPacket.HAND_SHAKE, "accepted"));
				sendCommand(new CommandPacket(CommandPacket.UDP_PORT,worldsocket.udp_port));
				
				Listener.getInstance().WorkerRunnable.execute(this);
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
			while (worldsocket.clientSocket != null && !worldsocket.clientSocket.isClosed()) {
				CommandPacket packet;
				packet = (CommandPacket) objectInput.readObject();
				packet.session_id = worldsocket.id;
				LOGGER.log(Level.INFO, " IN-(" + worldsocket.pedhandle  + ") [" + packet.toString() + "]", this);
				
				worldsocket.ProcessIncomingData(packet);
				//Listener.getInstance().commandsPackets.add(packet);
			}

		} catch (EOFException e) {
		} 
		catch (ClassNotFoundException | IOException e) {
			if (worldsocket.clientSocket != null && !worldsocket.clientSocket.isClosed()) {
				//LOGGER.log(Level.SEVERE, "An error has occured with client---- PRINTING STACK TRACE:", this);
				//e.printStackTrace();
			}
		}
		Listener.getInstance().removeSession(worldsocket.id);
	}

	public void sendCommand(CommandPacket packet) {
		LOGGER.log(Level.INFO, "OUT-(" + worldsocket.pedhandle + ") [" + packet.toString() + "]", this);
		try {
			objectOutput.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
			Listener.getInstance().removeSession(worldsocket.id);
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