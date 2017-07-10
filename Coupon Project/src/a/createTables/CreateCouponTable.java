package a.createTables;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;

import i.couponSystem.CouponSystemException;

import java.sql.Connection;
/** This is the class that builds the Coupon Database tables.
 *  We have created 5 tables, 3 for Company, Customer and Coupon.
 *  2 for join tables.
 *  The Join tables are not joined in SQL logic, methods that require the use of 2 tables will use JAVA logic.
 *  This class is for one use only. We have not allowed to drop or create tables more than once.
 *  This is to secure the Database*/
public class CreateCouponTable {

	
		public static void main(String[] args) throws CouponSystemException {
		String driverClassName = "org.apache.derby.jdbc.ClientDriver";
		try {
			// load driver class to memory
			Class.forName(driverClassName);

		} catch (ClassNotFoundException e) {
			String m = "driver was not found";
			throw new CouponSystemException(m);
		}

		// get an enumeration (collection) of all loaded drivers
		Enumeration<Driver> enumeration = DriverManager.getDrivers();
		System.out.println("drivers:");

		while (enumeration.hasMoreElements()) {
			Driver driver = enumeration.nextElement();
			System.out.println(driver); // print driver name
		}
		
		// url to the database we want to connect
				String url = "jdbc:derby://localhost:1527/CouponDB;create=true";

				// get a connection object
				
				try (Connection con = DriverManager.getConnection(url);) {

					System.out.println("connected to " + con);

				
				// Create 3 main tables
				
				String coupontable = "CREATE TABLE Coupon (ID BIGINT PRIMARY KEY, TITLE VARCHAR(20), START_DATE DATE, END_DATE DATE, AMOUNT INTEGER, TYPE VARCHAR(20),MESSAGE VARCHAR(20), PRICE DOUBLE, IMAGE VARCHAR(20))";
				String companytable = "CREATE TABLE Company (ID BIGINT PRIMARY KEY, COMP_NAME VARCHAR(20), PASSWORD VARCHAR(20), EMAIL VARCHAR(20))";
				String customertable = "CREATE TABLE Customer (ID BIGINT PRIMARY KEY, CUST_NAME VARCHAR(20), PASSWORD VARCHAR(20))";
				
				PreparedStatement pstmt1 = con.prepareStatement(coupontable);
				pstmt1.executeUpdate();
				System.out.println("success: " + coupontable);
				
				PreparedStatement pstmt2 = con.prepareStatement(companytable);
				pstmt2.executeUpdate();
				System.out.println("success: " + companytable);
				
				PreparedStatement pstmt3 = con.prepareStatement(customertable);
				pstmt3.executeUpdate();
				System.out.println("success: " + customertable);
				
				// Create 2 join tables
				
				String company_coupon = "CREATE TABLE Company_Coupon(COMPANY_ID BIGINT NOT NULL,COUPON_ID BIGINT NOT NULL,PRIMARY KEY(COMPANY_ID,COUPON_ID))";
				String customer_coupon = "CREATE TABLE Customer_Coupon(CUSTOMER_ID BIGINT NOT NULL,COUPON_ID BIGINT NOT NULL,PRIMARY KEY(CUSTOMER_ID,COUPON_ID))";
				
				PreparedStatement pstmt4 = con.prepareStatement(company_coupon);
				pstmt4.executeUpdate();
				System.out.println("success: " + company_coupon);
				
				PreparedStatement pstmt5 = con.prepareStatement(customer_coupon);
				pstmt5.executeUpdate();
				System.out.println("success: " + customer_coupon);
				
	} catch (SQLException e) {
		String m = "server not connected";
		throw new CouponSystemException(m);
	}
				// disconnect
				System.out.println("disconnected");
				
				
			}
		
		
	}


