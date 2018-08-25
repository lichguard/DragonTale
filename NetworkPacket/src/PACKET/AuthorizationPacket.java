package PACKET;

import java.io.Serializable;

public class AuthorizationPacket implements Serializable {

	private static final long serialVersionUID = 1L;

	public String password = "";
	public String username = "";
	
	public AuthorizationPacket(String username,String password ) {
		this.username =username;
		this.password = password;
		
	}
}
