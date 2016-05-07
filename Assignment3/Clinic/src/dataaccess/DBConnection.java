package dataaccess;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	
	private static String name = "jdbc:mysql://127.0.0.1:3306/clinic?autoReconnect=true&useSSL=false";
	private static String username = "root";
	private static String password = "admin";
	

	public static Connection connect(){
		Connection connection = null;
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(name, username, password);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return connection;
	}
}
