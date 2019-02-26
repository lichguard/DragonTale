package network;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import PACKET.WorldPacket;
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

	public void start() throws IOException {
		try {
			outStream = worldsocket.clientSocket.getOutputStream();
			inputStream = worldsocket.clientSocket.getInputStream();
			objectOutput = new ObjectOutputStream(outStream);
			objectInput = new ObjectInputStream(inputStream);
			Listener.getInstance().WorkerRunnable.execute(this);
		} catch (IOException e) {
			throw e;
		}
	}

	public void run() {

		try {
			while (worldsocket.clientSocket != null && !worldsocket.clientSocket.isClosed()
					&& !Thread.currentThread().isInterrupted()) {
				if (!worldsocket.ProcessIncomingData((WorldPacket) objectInput.readObject()))
					break;
			}
		} catch (ClassNotFoundException e) {
			LOGGER.error("----FATAL ERROR ClassNotFoundException--- socket.id: " + worldsocket.id, this);
			e.printStackTrace();
		} catch (IOException e) {
			if (worldsocket.clientSocket != null && !worldsocket.clientSocket.isClosed()) {
				LOGGER.info("TCP thread stopped listening... socket.id: " + worldsocket.id, this);
			}
		}

		worldsocket.disconnect();
	}

	public boolean sendPacket(WorldPacket packet) {
		synchronized (objectOutput) {

			try {
				objectOutput.writeObject(packet);
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error("----FATAL ERROR IOException WHILE sendPacket socket.id: " + worldsocket.id, this);
				disconnect();
				return false;
			}
		}
		return true;
	}

	public void disconnect() {

		try {
			if (objectInput != null)
				objectInput.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		try {
			if (objectOutput != null)
				objectOutput.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		try {
			if (inputStream != null)
				inputStream.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		try {
			if (outStream != null)
				outStream.close();

		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

}