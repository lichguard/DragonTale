package database;

import java.util.HashMap;
import java.util.Map;

public class Account {

	public static Map<String,Account> accounts_map = new HashMap<String,Account>();
	static {
		accounts_map.put("aa", new Account("me",0,130,30));
		accounts_map.put("bb", new Account("jesus",0,100,30));
	}
	public String name = "";
	public int id = 0;
	public int x = 0;
	public int y = 0;
	public Account (String name,int id,int x,int y) {
		this.name = name;
		this.id = id;
		this.x = x;
		this.y =y ;
	}
}
