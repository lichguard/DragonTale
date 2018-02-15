package servers;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.EOFException;
import java.io.IOException;


import PACKET.CommandPacket;

public class WorkerRunnableTCP implements Runnable {
	protected Session session;

	protected InputStream inputStream = null;
	protected OutputStream outStream = null;

	protected ObjectInputStream objectInput = null;
	protected ObjectOutputStream objectOutput = null;

	public WorkerRunnableTCP(Session session) {
		this.session = session;
	}
	public boolean init() {
		try {
			outStream = session.clientSocket.getOutputStream();
			inputStream = session.clientSocket.getInputStream();

			objectOutput = new ObjectOutputStream(outStream);
			objectInput = new ObjectInputStream(inputStream);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void run() {
		try {
			while (session.connected) {
				CommandPacket packet;
				packet = (CommandPacket) objectInput.readObject();
				packet.session_id = session.id;
				System.out.println("Reveiving command: " + packet.packet_code);
				synchronized (session.server.commandsPackets) {
					session.server.commandsPackets.add(packet);
					}
			}
			
		} 
		catch (EOFException e)
		{
		}
		catch (ClassNotFoundException | IOException e) {
			if (session.connected) {
				e.printStackTrace();
				System.out.println("An error has occured with client..");
			}
		}

		session.server.removesession(session.id);
	}

	public void sendCommand(CommandPacket packet) {
		System.out.println("Sending command: " + packet.packet_code);
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

			if (objectOutput != null)
				objectOutput.close();

			if (inputStream != null)
				inputStream.close();

			if (outStream != null)
				outStream.close();

		} catch (IOException e) {
			 e.printStackTrace();
		}
	}

}