package b.connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import i.couponSystem.CouponSystemException;
import java.sql.Connection;

/** This Class creates a Connection pool to the Coupon System Database.
 * It includes methods for getting and returning connections to/from the pool.
 * These methods will be used by the DBDAO class to interface with the Database.
 */
public class ConnectionPool {

	private static ConnectionPool instance;
	private static Set<Connection> connections = new HashSet<>();
	private static final String url = "jdbc:derby://localhost:1527/CouponDB;";

	
	// Creates 10 connections for the Connection pool.
	public ConnectionPool() throws CouponSystemException {
		for (int i = 0; i < 10; i++) {

			try {
				Class.forName("org.apache.derby.jdbc.ClientDriver");
				connections.add(DriverManager.getConnection(url));
			} catch (SQLException | ClassNotFoundException e) {
				String m="Failed to make connections pool";
				throw new CouponSystemException(m);
			}

		}
	}

	// get connection for use by methods.
	public synchronized Connection getConnection() throws CouponSystemException {
		while (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				String m = "Failed to get Connection";
				throw new CouponSystemException(m);
			}
		}
		Iterator<Connection> iter = connections.iterator();
		Connection con = iter.next();
		iter.remove();

		return con;

	}

	// returns connection to connection pool.
	public synchronized void returnConnection(Connection con) throws CouponSystemException {
		try {
			connections.add(con);
			notify();
		} catch (Exception e) {
			String m = "Failed to return connection";
			throw new CouponSystemException(m);

		}

	}

	// Closes all Connections in the pool.
	public void closeAllConnection() throws CouponSystemException {
		for (Connection connection : connections) {
			try {
				connection.close();
			} catch (SQLException e) {
				String m = "Failed to close all conections";
				throw new CouponSystemException(m);
			}
		}
	}

	// Gets a new instance of connection pool for use.
	public static ConnectionPool getInstance() throws CouponSystemException {
		try {
			if(instance==null){
				instance=new ConnectionPool();
			}
			return instance;

		} catch (Exception e) {
			String m = "Something went wrong";
			throw new CouponSystemException(m);
		}
		
	}

}
