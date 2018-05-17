package database;


public class TextDB {
	private static TextDB instance = null;

	public static TextDB getInstance() {
	 if (instance == null) {
	        instance = new TextDB();
	    }
	    return instance;
	}
	
	public int GetUserfromDB(String username, String password) {

		return 0;
	}
}
