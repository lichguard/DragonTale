package Network;

import PACKET.WorldPacket;
import main.LOGGER;

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

public class WorkerRunnableUDP implements Runnable {

	protected Session session = null;
	protected InputStream inputStream = null;
	protected ObjectInputStream objectInput = null;
	protected DatagramSocket socket = null;
	protected DatagramPacket outpacket = null;
	protected DatagramPacket inpacket = null;
	protected byte[] outbuf = null;
	protected byte[] inbuf = null;
	protected long last_packet_received_time = 0;

	public WorkerRunnableUDP(Session session) {
		this.session = session;
	}

	public boolean init() {
		outbuf = new byte[session.packet_size];
		inbuf = new byte[session.packet_size];
		outpacket = new DatagramPacket(outbuf, outbuf.length, session.clientSocket.getInetAddress(),
				session.clientSocket.getLocalPort() + session.udp_port);
		inpacket = new DatagramPacket(inbuf, inbuf.length);
		try {
			socket = new DatagramSocket(session.clientSocket.getPort() + session.udp_port);
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void run() {
		try {
			while (session.connected) {
				socket.receive(inpacket);
				dispatchPacket();
			}
		} catch (IOException e) {
			if (session.connected) {
				e.printStackTrace();
				LOGGER.log(Level.SEVERE,"An error has occured with client..", this);
			}
		}
		session.disconnect();
	}

	public void dispatchPacket() {
		ByteArrayInputStream in = new ByteArrayInputStream(inpacket.getData());
		ObjectInputStream is;
		try {
			is = new ObjectInputStream(in);
			WorldPacket packet = (WorldPacket) is.readObject();
			//if (packet.timeframe > last_packet_received_time) {
				synchronized (session.worldPackets) {
					session.worldPackets.add(packet);
				}
				//last_packet_received_time = packet.timeframe;
			//}
		} catch (ClassNotFoundException | IOException e) {
			if (session.connected) {
				e.printStackTrace();
				session.disconnect();
				LOGGER.log(Level.SEVERE,"An error has occured with client..", this);
			}
		}

	}

	public void sendWorldPacket(WorldPacket packet) {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		try {
			ObjectOutput oo = new ObjectOutputStream(bStream);
			oo.writeObject(packet);
			outpacket.setData(bStream.toByteArray());
			socket.send(outpacket);
		} catch (IOException e) {
			e.printStackTrace();
			session.disconnect();
		}
	}

	public void disconnect() {

		if (socket != null)
			socket.close();
	}
}