package database;

import java.util.HashMap;
import java.util.Map;

public class Account {

	public static Map<String,Account> accounts_map = new HashMap<String,Account>();
	static {
		accounts_map.put("adam", new Account("adam",0,130,30,true));
		accounts_map.put("eve", new Account("eve",1,200,100,true));
	}
	public String name = "";
	public int id = 0;
	public int x = 0;
	public int y = 0;
	public boolean facingright = true;
	public Account (String name,int id,int x,int y,boolean facingright) {
		this.name = name;
		this.id = id;
		this.x = x;
		this.y =y ;
		this.facingright =facingright;
	}
}
