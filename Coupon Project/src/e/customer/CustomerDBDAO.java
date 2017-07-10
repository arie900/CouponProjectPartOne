package e.customer;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import b.connection.ConnectionPool;
import c.coupon.Coupon;
import c.coupon.CouponType;
import i.couponSystem.CouponSystemException;

/** This class connects the Java program to the Derby Database.
 *  Includes the CRUD methods and methods for getting coupons/coupon lists for a specific customer.
 *  This DBDAO layer knows to translate the outcome to Java beans or to collections.
 */
public class CustomerDBDAO implements CustomerDAO {

	@Override
	public void createCustomer(Customer customer) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "INSERT INTO CUSTOMER VALUES(?,?,?)";
		try {
			List<Customer> customerList=new ArrayList<>();
			customerList=getAllCustomer();
			for (Customer customer2 : customerList) {
				if(customer.getId()==customer2.getId() ){
					System.out.println("Customer not created because ID in use");
					return;
				}
			}
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, customer.getId());
			pstmt.setString(2, customer.getCustName());
			pstmt.setString(3, customer.getPassword());
			pstmt.executeUpdate();
			System.out.println("Customer: "+customer.getCustName()+" created");
		} catch (SQLException e) {
			String m = "Fail to create customer";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void deleteCustomer(Customer customer) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "DELETE FROM CUSTOMER WHERE CUST_NAME=?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, customer.getCustName());
			if(pstmt!=null){
			int rowDeleted=pstmt.executeUpdate();
			if(rowDeleted>0){
			System.out.println("Customer with the customer name " + customer.getCustName() + " deleted");
			}
			}
		} catch (SQLException e) {
			String m = "The customer where customer name " + customer.getCustName() + " failed to delete";
			throw new CouponSystemException(m);
		}
		finally{
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	@Override
	public void updateCustomer(Customer customer) throws CouponSystemException {
		String sql = "UPDATE CUSTOMER SET password='"+customer.getPassword() + "' WHERE CUST_NAME='"+customer.getCustName() +"'";
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			int row=pstmt.executeUpdate();
			if(row>0){
				System.out.println("Customer with the customer name " + customer.getCustName() + " updated");				
			}
			else{
				System.out.println("Customer with name: " + customer.getCustName() + " not found");
			}
		} catch (SQLException e) {
			String m = "Failed to update customer where id " + customer.getId();
			throw new CouponSystemException(m);
		}
		finally{
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	@Override
	public Customer getCustomer(long id) throws CouponSystemException {
		Customer customer = new Customer();
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			String sql = "SELECT * FROM CUSTOMER WHERE ID=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				customer.setId(rs.getLong(1));
				customer.setCustName(rs.getString(2));
				customer.setPassword(rs.getString(3));
			}
			else{
				System.out.println("Customer with the id: " + id + " not found");
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			String m = "Failed to get customer with the id: " + id;
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return customer;
	}

	@Override
	public Customer getCustomerByName(String name) throws CouponSystemException {
		Customer customer = new Customer();
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			String sql = "SELECT * FROM CUSTOMER WHERE CUST_NAME like '" + name + "'";
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				customer.setId(rs.getLong(1));
				customer.setCustName(rs.getString(2));
				customer.setPassword(rs.getString(3));
			}
			else{
				System.out.println("Customer with the name: " + name + " not found");
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			String m = "Failed to get customer with the name: " + name;
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return customer;
		
	}

	@Override
	public List<Customer> getAllCustomer() throws CouponSystemException {
		List<Customer> customerList = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			String sql = "SELECT * FROM CUSTOMER";
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Customer customer = new Customer();
				customer.setId(rs.getLong(1));
				customer.setCustName(rs.getString(2));
				customer.setPassword(rs.getString(3));
				customerList.add(customer);
			}
		} catch (SQLException e) {
			String m = "Failed to get all the customers";
			throw new CouponSystemException(m);

		}
		finally{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return customerList;
	}

	@Override
	public List<Coupon> getCoupons(Customer customer) throws CouponSystemException {
		List<Coupon> couponList = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "SELECT * FROM CUSTOMER_COUPON WHERE CUSTOMER_ID=?";
		String sql2="SELECT * FROM COUPON WHERE ID=?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			PreparedStatement pstmt2=con.prepareStatement(sql2);
			pstmt.setLong(1, customer.getId());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Coupon coupon = new Coupon();
				coupon.setId(rs.getLong(2));
				pstmt2=con.prepareStatement(sql2);
				pstmt2.setLong(1, coupon.getId());
				ResultSet rs2=pstmt2.executeQuery();
				while(rs2.next()){
					coupon.setTitle(rs2.getString(2));
					coupon.setStartDate(rs2.getDate(3));
					coupon.setEndDate(rs2.getDate(4));
					coupon.setAmount(rs2.getInt(5));
					coupon.setType(CouponType.valueOf(rs2.getString(6)));
					coupon.setMessage(rs2.getString(7));
					coupon.setPrice(rs2.getDouble(8));
					coupon.setImage(rs2.getString(9));
					couponList.add(coupon);
					continue;
					
				}

			}

		} catch (SQLException e) {
			String m = "Failed to get all the coupons";
			throw new CouponSystemException(m);
		}
		finally{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return couponList;
	}
	
	@Override
	public List<Coupon> getCouponByType(Customer customer, CouponType couponType) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> typeList = new ArrayList<>();
		try {
			List<Coupon> couponList = getCoupons(customer);
			for (Coupon coupon : couponList) {
				if (coupon.getType().equals(couponType)) {
					typeList.add(coupon);
				}

			}

			if (typeList.isEmpty()) {
				System.out.println("Coupon list by type is empty");
			}
		} catch (CouponSystemException e) {
			throw new CouponSystemException("Failed to get customer coupon by type");
		} finally

		{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return typeList;
	}
	

	@Override
	public List<Coupon> getCouponByPrice(Customer customer, double price) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> priceList = new ArrayList<>();
		try {
			List<Coupon> couponList = getCoupons(customer);
			for (Coupon coupon : couponList) {
				if (coupon.getPrice() <= price) {
					priceList.add(coupon);
				}

			}

			if (priceList.isEmpty()) {
				System.out.println("Coupon list for customer up to price is empty");
			}
		} catch (CouponSystemException e) {
			throw new CouponSystemException("Failed to get customer coupon by price");
		} finally

		{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return priceList;

	}

	
	@Override
	public boolean login(String customer, String password) throws CouponSystemException{
		Connection con=ConnectionPool.getInstance().getConnection();
		try {
		if(getCustomerByName(customer)!=null){
		String sql="SELECT PASSWORD FROM CUSTOMER WHERE CUST_NAME LIKE '" + customer + "'";
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			rs.next();
			if(password.equals(rs.getString(1))){
				System.out.println("Logged in as " + customer);
				return true;
			}
			else{
				System.out.println("Wrong details");
				return false;
			}
		}
		else {
			System.out.println("wrong username");
			ConnectionPool.getInstance().returnConnection(con);
			return false;
		}
		} catch (SQLException e) {
			String m="Failed to login";
			throw new CouponSystemException(m);
		}
		finally {	ConnectionPool.getInstance().returnConnection(con);
		}
		
		
	}
	

	@Override
	public void deleteCoupon_cust(Customer customer) throws CouponSystemException {
	
			Connection con = ConnectionPool.getInstance().getConnection();
			String sql = "DELETE * FROM Customer_Coupon WHERE CUSTOMER_ID LIKE ?";
			
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setLong(1, customer.getId());
				pstmt.executeUpdate();
				System.out.println("Coupons of the customer id=" + customer.getId() + "deleted");
			} catch (SQLException e) {
				String m = "The coupons where customer id" + customer.getId() + "failed to delete";
				throw new CouponSystemException(m);
			} finally {
				ConnectionPool.getInstance().returnConnection(con);
			}
		}

		
	
}
