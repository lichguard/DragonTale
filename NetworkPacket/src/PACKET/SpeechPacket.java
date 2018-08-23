package PACKET;

import java.io.Serializable;

public class SpeechPacket implements Serializable {
	private static final long serialVersionUID = 1L;
	public int handle = 0;
	public String text = "";
	
	public SpeechPacket(int handle,String text)
	{
		this.handle = handle;
		this.text = text;
	}
	
	@Override
	public String toString()
	{
		return "SpeechPacket handle:" + handle + " txt: " + text;
	}
}
