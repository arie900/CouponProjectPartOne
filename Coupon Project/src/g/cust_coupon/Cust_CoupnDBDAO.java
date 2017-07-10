package g.cust_coupon;

/** This class connects the Java program to the Derby Database.
 *  Includes the Create, Delete and Get methods and methods for getting coupons/coupon lists for a specific customer and/or coupon.
 *  This DBDAO layer knows to translate the outcome to Java beans or to collections.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import b.connection.ConnectionPool;
import c.coupon.Coupon;
import c.coupon.CouponType;
import e.customer.Customer;
import i.couponSystem.CouponSystemException;

/** This class connects the Java program to the Derby Database.
 *  Includes the Create, Delete and Get methods and methods for getting coupons/coupon lists for a specific customer and/or coupon.
 *  This DBDAO layer knows to translate the outcome to Java beans or to collections.
 */
public class Cust_CoupnDBDAO implements Cust_couponDAO {

	@Override
	public void createCustomerCoupon(Customer customer, Coupon coupon) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
					String sql = "INSERT INTO Customer_Coupon values(?,?)";
					PreparedStatement pstmt;
					pstmt = con.prepareStatement(sql);
					pstmt.setLong(1, customer.getId());
					pstmt.setLong(2, coupon.getId());
					pstmt.executeUpdate();
					System.out.println("Coupons: "+coupon.getTitle()+" added to customer: "+customer.getCustName());
				
		}
		 catch (SQLException e) {
			String m = "Failed to update table";
			throw new CouponSystemException(m);
		}
		finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		
	}

	@Override
	public void deleteCustomerAllCoupons(Customer customer) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		
		String sql = "DELETE FROM Customer_Coupon WHERE CUSTOMER_ID = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, customer.getId());
			int rowDeleted = pstmt.executeUpdate();
			if (rowDeleted>0){
				System.out.println("All coupouns for "+ customer.getCustName() + " deleted");
			}
			else {
				System.out.println("No coupons found for customer: "+customer.getCustName());
			}
		} catch (SQLException e) {
			String m = "Failed to delete coupons for customer";
			throw new CouponSystemException(m);
		}
		finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}
	
	

	

	@Override
	public void deleteByCoupon(Coupon coupon) throws CouponSystemException {
Connection con = ConnectionPool.getInstance().getConnection();
		
		String sql = "DELETE FROM Customer_Coupon WHERE COUPON_ID = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, coupon.getId());
			int rowDeleted = pstmt.executeUpdate();
			if (rowDeleted>0){
				System.out.println("All coupouns: "+ coupon.getTitle() + " deleted");
			}
			else {
				System.out.println("No coupons: "+coupon.getTitle()+ " found in customer-coupon table");
			}
		} catch (SQLException e) {
			String m = "Failed to delete coupon for customer";
			throw new CouponSystemException(m);
		}
		finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	@Override
	public List<Coupon> getCoupon(Customer customer) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> couponList = new ArrayList<>();
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
	public List<Coupon> getCouponByCoupon(Coupon coupon) throws CouponSystemException {
		String sql = "SELECT * FROM Customer_Coupon WHERE COUPON_ID = ?";
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> coupons = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(2, coupon.getId());
			ResultSet rs = pstmt.executeQuery(sql);
			while (rs.next()) {
				Coupon coup = new Coupon();
				coup.setId(rs.getLong(1));
			}
		} catch (SQLException e) {
			String m = "Get All coupons failed";
			throw new CouponSystemException(m);
		}
		finally{
			ConnectionPool.getInstance().returnConnection(con);			
		}
		return coupons;
	}

	
	
	
	
	

}
