package Network;

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

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendCommand(new CommandPacket(CommandPacket.HAND_SHAKE, session.username + "," + session.password));
			session.password = "";
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CommandPacket packet = (CommandPacket) objectInput.readObject();
			
			if (packet.packet_code != CommandPacket.HAND_SHAKE)
				return false;
			
			 return (((String) packet.data).compareTo("accepted") == 0);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
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
				System.out.println("Receieving command: " + packet.packet_code);
				synchronized (session.commandsPackets) {
					session.commandsPackets.add(packet);
				}
			}
			
		} 
		catch (EOFException e)
		{
				disconnect();
		}
		catch (ClassNotFoundException | IOException e) {
			if (session.connected) {
				e.printStackTrace();
				System.out.println("An error has occured with client..");
			}
		}

		session.disconnect();
	}

	public void sendCommand(CommandPacket packet) {
		System.out.println("Sending command: " + packet.packet_code);
		try {
			objectOutput.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
			session.disconnect();
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