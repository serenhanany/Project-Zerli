package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * Class description: 
 * This is a class for the connection to the 
 * DB.
 * 
 * @author Obied
 *
 */
public class mysqlConnection {
	
	/**
	 * This is the var of the connection to the DB
	 */
	public static Connection conn;
	/**
	 * Create the connection to the DB
	 * @param DBName
	 * @param DBUserName
	 * @param DBPassword
	 * @return 
	 */
	public static boolean connectToDB(String DBName, String DBUserName, String DBPassword) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");

			return false;
		}

		try {
			conn = DriverManager.getConnection(DBName, DBUserName, DBPassword);

			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println(""
					+ "+SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;

		}
		return true;
	}


	/**
	 * This is the getter of the connection
	 * @return connection
	 */
	public Connection getConnection() {
		return conn;
	}

}
