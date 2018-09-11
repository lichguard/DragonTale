package network;

import PACKET.MovementData;
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
		//LOGGER.error("PORT: " + session.udp_port, this);
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
		LOGGER.log(Level.INFO, "UDP Thread running...", this);
		try {
			while (!Thread.interrupted()) {
				socket.receive(inpacket);
				dispatchPacket();
			}
		} catch (IOException e) {
				//e.printStackTrace();
				//LOGGER.log(Level.SEVERE,"An error has occured with client..", this);
			LOGGER.log(Level.INFO,"UDP socket closed", this);
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
				//synchronized (session.worldPackets) {
				//	session.worldPackets.add((MovementData)packet.data);
				//}
			//LOGGER.debug( ((MovementData)packet.data).handle + " x: " + ((MovementData)packet.data).x , this);
			session.ProcessIncomingData(packet);
				//last_packet_received_time = packet.timeframe;
			//}
		} catch (ClassNotFoundException | IOException e) {

				e.printStackTrace();
				LOGGER.log(Level.SEVERE,"An error has occured with client..", this);
				session.disconnect();
		}

	}

	public void sendWorldPacket(MovementData data) {
		
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		try {
			ObjectOutput oo = new ObjectOutputStream(bStream);
			oo.writeObject(new WorldPacket(WorldPacket.MOVEMENT_DATA,data));
			outpacket.setData(bStream.toByteArray());
			socket.send(outpacket);
			oo.close();
			bStream.close();
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