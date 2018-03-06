package PACKET;

import java.io.Serializable;
import java.util.UUID;

public class CommandPacket implements Serializable{

	private static final long serialVersionUID = 2L;
	
	public static final int SPEECH =2;
	
	public static final int SPAWN =0;
	public static final int DESPAWN =1;
	
	public static final int REQUEST_HANDLE =3;
	public static final int HANDLE =4;
	
	public static final int REQUEST_UDP_PORT =5;
	public static final int UDP_PORT =6;
	
	public static final int ADD_SESSION =6;
	public static final int REMOVE_SESSION =7;
	public static final int REQUEST_SPAWN =8;
	
	public static final int HAND_SHAKE = 9;
	public static final int REQUEST_HAND_SHAKE = 10;
	
	public UUID session_id = null;
	public int packet_code = 0;
	public Object data = null;
	
	public CommandPacket(int packet_code, Object data)
	{
		this.packet_code = packet_code;
		this.data = data;
	}
}
