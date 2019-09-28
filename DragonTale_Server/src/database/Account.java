package database;

import java.util.HashMap;
import java.util.Map;

public class Account {

	public static Map<String,Account> accounts_map = new HashMap<String,Account>();
	static {
		accounts_map.put("1", new Account("1",0,130,30,true));
		accounts_map.put("2", new Account("2",1,200,100,true));
		accounts_map.put("3", new Account("3",2,200,100,true));
	}
	public String name = "";
	public int id = 0;
	public float x = 0;
	public float y = 0;
	public boolean facingright = true;
	public Account (String name,int id,float x,float y,boolean facingright) {
		this.name = name;
		this.id = id;
		this.x = x;
		this.y =y ;
		this.facingright =facingright;
	}
}
