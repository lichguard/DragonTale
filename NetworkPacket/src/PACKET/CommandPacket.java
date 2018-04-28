package PACKET;

import java.io.Serializable;
import java.util.UUID;

public class CommandPacket implements Serializable{

	private static final long serialVersionUID = 2L;
	
	private static final String[] COMMANDS_STRING = {"SPAWN","DESPAWN","SPEECH","REQUEST_HANDLE","HANDLE","REQUEST_UDP_PORT","UDP_PORT","ADD_SESSION","REMOVE_SESSION","REQUEST_SPAWN","HAND_SHAKE","REQUEST_HAND_SHAKE","MELEE_ATTACK"};

	public static final int SPAWN =0;
	public static final int DESPAWN =1;
	public static final int SPEECH =2;
	public static final int REQUEST_HANDLE =3;
	public static final int HANDLE =4;
	public static final int REQUEST_UDP_PORT =5;
	public static final int UDP_PORT =6;
	public static final int ADD_SESSION =7;
	public static final int REMOVE_SESSION =8;
	public static final int REQUEST_SPAWN =9;
	public static final int HAND_SHAKE = 10;
	public static final int REQUEST_HAND_SHAKE = 11;
	public static final int MELEE_ATTACK = 12;
	
	public UUID session_id = null;
	public int packet_code = 0;
	public Object data = null;
	
	public CommandPacket(int packet_code, Object data)
	{
		this.packet_code = packet_code;
		this.data = data;
	}
	
	public String getCommandName()
	{
		return COMMANDS_STRING[packet_code];
	}
}
