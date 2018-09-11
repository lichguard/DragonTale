package database;

import java.util.HashMap;
import java.util.Map;

public class Account {

	public static Map<String,Account> accounts_map = new HashMap<String,Account>();
	static {
		accounts_map.put("aa", new Account("me",0,130,30,true));
		accounts_map.put("bb", new Account("jesus",0,300,100,true));
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
		this.facingright =facingright ;
	}
}
