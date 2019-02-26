package network;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;

import PACKET.WorldPacket;

import java.io.EOFException;
import java.io.IOException;
import main.LOGGER;

public class WorkerRunnableTCP implements Runnable {
	protected Session session;
	protected Socket clientSocket = null;
	protected InputStream inputStream = null;
	protected OutputStream outStream = null;

	protected ObjectInputStream objectInput = null;
	protected ObjectOutputStream objectOutput = null;
	protected Thread listeningThread = null;
	
	public WorkerRunnableTCP(Session session) {
		this.session = session;
	}

	public void init(String IP, int port) throws IOException {
		clientSocket = new Socket(IP, port);
		
		outStream = clientSocket.getOutputStream();
		inputStream = clientSocket.getInputStream();

		objectOutput = new ObjectOutputStream(outStream);
		objectInput = new ObjectInputStream(inputStream);
		
		listeningThread = new Thread(this);
		listeningThread.start();
		
		
		try {
			synchronized (listeningThread) {
				listeningThread.wait(5000);
			}
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	public boolean isConnected() {
		return clientSocket != null && !clientSocket.isClosed() && clientSocket.isConnected();
	}

	public void run() {
		LOGGER.info("Thread running...", this);
		synchronized (listeningThread) {
			listeningThread.notifyAll();
		}
		try {
			while (!Thread.interrupted()) {
				WorldPacket packet;
				packet = (WorldPacket) objectInput.readObject();
				LOGGER.info("Received command: " + packet.getCommandName(), this);
				session.ProcessIncomingData(packet);
			}
			
		} 
		catch (EOFException e1) {
			LOGGER.info("EOF Exception", this);
		}
		catch (ClassNotFoundException | IOException e) {
			if (e != null && e.getMessage().equals("Socket closed")) {
				LOGGER.info("TCP Socket closed", this);
			} else {
				e.printStackTrace();
				LOGGER.error("An error has occured with the server: " + e.getMessage(), this);
			}
		}

		LOGGER.info("Thread terminating...", this);
		Session.getInstance().disconnect("tcp listening error");
	}

	public void sendCommand(WorldPacket packet) throws IOException {
		LOGGER.log(Level.INFO, "Sending command: " + packet.getCommandName(), this);
		objectOutput.writeObject(packet);
	}

	public void disconnect() {
		try {
			if (isConnected()) {
				clientSocket.close();
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
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

		if (listeningThread != null)
			listeningThread.interrupt();
	}

}