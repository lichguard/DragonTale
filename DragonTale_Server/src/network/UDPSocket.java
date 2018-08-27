package network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;

import PACKET.WorldPacket;
import main.LOGGER;

public class UDPSocket implements Runnable {


	protected WorldSocket worldsocket = null;
	protected Thread runningThread = null;
	protected InputStream inputStream = null;
	protected ObjectInputStream objectInput = null;
	protected DatagramSocket socket = null;
	protected DatagramPacket outpacket = null;
	protected DatagramPacket inpacket = null;
	protected byte[] outbuf = null;
	protected byte[] inbuf = null;
	protected long last_packet_received_time = 0;
	protected int port = 0;

	public UDPSocket(WorldSocket session,int port) {
		this.worldsocket = session;
		this.port = port;
	}

	public void start() throws SocketException {
		LOGGER.error("PORT: " + port, this);
		outbuf = new byte[worldsocket.packet_size];
		inbuf = new byte[worldsocket.packet_size];
		outpacket = new DatagramPacket(outbuf, outbuf.length, worldsocket.clientSocket.getInetAddress(),
				worldsocket.clientSocket.getLocalPort() + port);
		inpacket = new DatagramPacket(inbuf, inbuf.length);
		try {
			socket = new DatagramSocket(worldsocket.clientSocket.getPort() + port);
		} catch (SocketException e) {
			throw e;
		}
		
		Listener.getInstance().WorkerRunnable.execute(this);
	}

	public void run() {
		try {
			while (worldsocket.clientSocket != null && !worldsocket.clientSocket.isClosed()) {
				socket.receive(inpacket);
				ByteArrayInputStream in = new ByteArrayInputStream(inpacket.getData());
				ObjectInputStream is = new ObjectInputStream(in);
				WorldPacket packet = (WorldPacket) is.readObject();
				is.close();
				in.close();
				worldsocket.ProcessIncomingData(packet);
			}
		} catch (Exception e) {
			if (worldsocket.clientSocket != null && !worldsocket.clientSocket.isClosed()) {
				LOGGER.log(Level.SEVERE, "An error has occured with client---- PRINTING STACK TRACE:", this);
				e.printStackTrace();
			}
		}
		disconnect();
	}


	public boolean sendWorldPacket(WorldPacket packet) {
		//LOGGER.debug("x: " + ((MovementData)packet.data).handle, this);
		try {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bStream);
			bStream = new ByteArrayOutputStream();
			oo = new ObjectOutputStream(bStream);
			oo.writeObject(packet);
			outpacket.setData(bStream.toByteArray());
			bStream.close();
			oo.close();
			socket.send(outpacket);
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
			return false;
		}
		
		return true;

	}

	public void disconnect() {

		if (socket != null)
			socket.close();
	}
}
