package dt.network;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import PACKET.WorldPacket;

import java.io.EOFException;
import java.io.IOException;
import UI.Control;
import main.LOGGER;

public class WorkerRunnableTCP implements Runnable {
	protected Session session;

	protected InputStream inputStream = null;
	protected OutputStream outStream = null;

	protected ObjectInputStream objectInput = null;
	protected ObjectOutputStream objectOutput = null;

	public WorkerRunnableTCP(Session session) {
		this.session = session;
	}

	public void init(Control status) {
		try {
			outStream = session.clientSocket.getOutputStream();
			inputStream = session.clientSocket.getInputStream();

			objectOutput = new ObjectOutputStream(outStream);
			objectInput = new ObjectInputStream(inputStream);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		LOGGER.log(Level.INFO, "TCP running", this);
		try {
			while (session.connected) {
				WorldPacket packet;
				packet = (WorldPacket) objectInput.readObject();
				LOGGER.log(Level.INFO, "Receieving command: " + packet.getCommandName(), this);

				session.ProcessIncomingData(packet);
			}

		} catch (EOFException e) {
			disconnect();
		} catch (ClassNotFoundException | IOException e) {
			if (session.connected) {
				e.printStackTrace();
				LOGGER.log(Level.SEVERE, "An error has occured with server..", this);
			}
		}

		session.disconnect();
	}

	public void sendCommand(WorldPacket packet) {
		LOGGER.log(Level.INFO, "Sending command: " + packet.getCommandName(), this);
		synchronized (objectOutput) {
		try {
			objectOutput.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
			session.disconnect();
		}
	}
	}

	public void disconnect() {

		try {
			if (objectInput != null)
				objectInput.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		try {
			if (objectOutput != null)
				objectOutput.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		try {
			if (inputStream != null)
				inputStream.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		try {
			if (outStream != null)
				outStream.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}

	}

}