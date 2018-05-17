package database;

public interface IDB {
	
	public int GetUserfromDB(String username, String password);

	public int CreateAccountinDB(String username, String password);
}
