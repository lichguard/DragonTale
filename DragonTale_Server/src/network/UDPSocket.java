package servers;

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

public class WorkerRunnableUDP implements Runnable {

	protected Session session = null;
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

	public WorkerRunnableUDP(Session session) {
		this.session = session;
	}

	public void start() throws SocketException {
		outbuf = new byte[session.packet_size];
		inbuf = new byte[session.packet_size];
		outpacket = new DatagramPacket(outbuf, outbuf.length, session.clientSocket.getInetAddress(),
				session.clientSocket.getLocalPort() + session.udp_port);
		inpacket = new DatagramPacket(inbuf, inbuf.length);
		try {
			socket = new DatagramSocket(session.clientSocket.getPort() + session.udp_port);
		} catch (SocketException e) {
			throw e;
		}
		
		Server.getInstance().WorkerRunnable.execute(this);
	}

	public void run() {
		try {
			while (session.clientSocket != null && !session.clientSocket.isClosed()) {
				socket.receive(inpacket);
				ByteArrayInputStream in = new ByteArrayInputStream(inpacket.getData());
				ObjectInputStream is = new ObjectInputStream(in);
				WorldPacket packet = (WorldPacket) is.readObject();
				is.close();
				in.close();
				Server.getInstance().worldPackets.add(packet);
			}
		} catch (Exception e) {
			if (session.clientSocket != null && !session.clientSocket.isClosed()) {
				LOGGER.log(Level.SEVERE, "An error has occured with client---- PRINTING STACK TRACE:", this);
				e.printStackTrace();
			}
		}
		Server.getInstance().removeSession(session.id);
	}


	public void sendWorldPacket(WorldPacket packet) {
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
			Server.getInstance().removeSession(session.id);
		}
		
		

	}

	public void disconnect() {

		if (socket != null)
			socket.close();
	}
}
