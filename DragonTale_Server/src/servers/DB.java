package servers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class DB {
	public static boolean enableSQL = true;
	public static Connection getMySQLConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useSSL=false", "gameserver", "gameserver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public static int GetUserfromDB(String username, String password) {

		if (!enableSQL) //0 will let anyone in
			return 0;
		
		int id = -1;
		try {
			Connection con = getMySQLConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select `id`, `password`,`salt` from `game_world`.`accounts` where `username` = '" + username + "'");
			while (rs.next()) {
				if (rs.getString("password").compareTo(password) == 0) {
					id = rs.getInt("id");
				}
			}

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static int CreateAccountinDB(String username, String password) {
		
		if (!enableSQL) //0 will let anyone in
			return 0;
		
		try {
			Connection con = getMySQLConnection();
			PreparedStatement stmt = null;
			String statementstring = "INSERT INTO `game_world`.`accounts`(`username`,`password`,`salt`) VALUES (?,?,?)";
			stmt = con.prepareStatement(statementstring);
			String salt = Float.toString(new Random().nextFloat()).substring(2);
			//password = MessageDigest (password + salt);
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, salt);
			stmt.execute();
			con.close();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
