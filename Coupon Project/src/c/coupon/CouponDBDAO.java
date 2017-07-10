package c.coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import b.connection.ConnectionPool;
import i.couponSystem.CouponSystemException;

/** This class connects the Java program to the Derby Database.
 *  Includes the CRUD methods and methods for getting relevant coupons/coupon lists.
 *  This DBDAO layer knows to translate the outcome to Java beans or to collections.
 */
public class CouponDBDAO implements CouponDAO {
	public CouponDBDAO() {

	}
	
	@Override
	public void createCoupon(Coupon coupon) throws CouponSystemException {
		String sql = "INSERT INTO coupon(id, title, start_Date, end_Date, amount, type, message, price, image)"
				+ "values(?,?,?,?,?,?,?,?,?)";

		Connection con = ConnectionPool.getInstance().getConnection();

		try {
			List<Coupon> couponList = new ArrayList<>();
			couponList = getAllCoupons();
			for (Coupon coupon2 : couponList) {
				if (coupon2.getId() == coupon.getId()) {
					System.out.println("Coupon not created because ID in use");
					return;
				}
			}
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, coupon.getId());
			pstmt.setString(2, coupon.getTitle());
			pstmt.setDate(3, new java.sql.Date(coupon.getStartDate().getTime()));
			pstmt.setDate(4, new java.sql.Date(coupon.getEndDate().getTime()));
			pstmt.setInt(5, coupon.getAmount());
			pstmt.setString(6, coupon.getType().toString());
			pstmt.setString(7, coupon.getMessage());
			pstmt.setDouble(8, coupon.getPrice());
			pstmt.setString(9, coupon.getImage());
			pstmt.executeUpdate();
			System.out.println("Coupon: "+coupon.getTitle()+" created");
		} catch (SQLException e) {

			String m = "Create coupon failed";
			throw new CouponSystemException(m, e);

		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void deleteCoupon(Coupon coupon) throws CouponSystemException {
		String sql = "DELETE FROM coupon WHERE id = " + coupon.getId();
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);

			if (pstmt != null) {
				int rowDelte = pstmt.executeUpdate();
				if (rowDelte > 0) {
					System.out.println("Coupon with the coupon id " + coupon.getId() + " deleted");
				}
			}

		} catch (SQLException e) {
			String m = "The coupon where coupon id " + coupon.getId() + " failed to delete";
			throw new CouponSystemException(m, e);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		PreparedStatement pstmt;

		String sql = "UPDATE coupon SET title=?, start_date=?, "
				+ "end_date=?, amount=?, type=?, message=?, price=?, image=? WHERE id=" + coupon.getId();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, coupon.getTitle());
			pstmt.setDate(2, new java.sql.Date(coupon.getStartDate().getTime()));
			pstmt.setDate(3, new java.sql.Date(coupon.getEndDate().getTime()));
			pstmt.setInt(4, coupon.getAmount());
			pstmt.setString(5, coupon.getType().toString());
			pstmt.setString(6, coupon.getMessage());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());
			pstmt.executeUpdate();
			System.out.println("Coupon with the id " + coupon.getId() + " updated");

		} catch (SQLException e) {
			String m = "The coupon where id " + coupon.getId() + " not updated";
			throw new CouponSystemException(m, e);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void compnayUpdateCoupon(Coupon coupon, Date enddate, double price) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		PreparedStatement pstmt;
		
		long mills = enddate.getTime();
		 
		String sql = "UPDATE coupon SET  end_date=?, price=? WHERE id=" + coupon.getId();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setDate(1, new java.sql.Date(mills));
			pstmt.setDouble(2, price);
			pstmt.executeUpdate();
			System.out.println("Coupon with the id " + coupon.getId() + " updated");

		} catch (SQLException e) {
			String m = "The coupon where id " + coupon.getId() + " not updated";
			throw new CouponSystemException(m, e);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public Coupon getCoupon(long id) throws CouponSystemException {
		String sql = "SELECT * FROM coupon WHERE id = " + id;
		Connection con = ConnectionPool.getInstance().getConnection();
		Coupon coup = new Coupon();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				coup.setId(rs.getLong(1));
				coup.setTitle(rs.getString(2));
				coup.setStartDate(rs.getDate(3));
				coup.setEndDate(rs.getDate(4));
				coup.setAmount(rs.getInt(5));
				coup.setType(CouponType.valueOf(rs.getString(6)));
				coup.setMessage(rs.getString(7));
				coup.setPrice(rs.getDouble(8));
				coup.setImage(rs.getString(9));
			} else {
				System.out.println("Coupon with the id " + id + " not found");
			}

		} catch (SQLException e) {
			String m = "Failed to get coupon with the id " + id;
			throw new CouponSystemException(m, e);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coup;
	}

	@Override
	public List<Coupon> getAllCoupons() throws CouponSystemException {
		String sql = "SELECT * FROM coupon";
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> coupons = new ArrayList<>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Coupon coup = new Coupon();
				coup.setId(rs.getLong(1));
				coup.setTitle(rs.getString(2));
				coup.setStartDate(rs.getDate(3));
				coup.setEndDate(rs.getDate(4));
				coup.setAmount(rs.getInt(5));
				coup.setType(CouponType.valueOf(rs.getString(6)));
				coup.setMessage(rs.getString(7));
				coup.setPrice(rs.getDouble(8));
				coup.setImage(rs.getString(9));
				coupons.add(coup);

			}
		} catch (SQLException e) {
			String m = "Get All coupons failed";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupons;
	}

	@Override
	public List<Coupon> getCouponByType(CouponType CouponType) throws CouponSystemException {
		String coupType = CouponType.toString();
		String sql = "SELECT * FROM coupon where type=" + "'" + coupType + "'";
		Connection con = ConnectionPool.getInstance().getConnection();
		Collection<Coupon> coupons = new ArrayList<>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Coupon coup = new Coupon();
				coup.setId(rs.getLong(1));
				coup.setTitle(rs.getString(2));
				coup.setStartDate(rs.getDate(3));
				coup.setEndDate(rs.getDate(4));
				coup.setAmount(rs.getInt(5));
				coup.setType(c.coupon.CouponType.valueOf(rs.getString(6)));
				coup.setMessage(rs.getString(7));
				coup.setPrice(rs.getDouble(8));
				coup.setImage(rs.getString(9));
				coupons.add(coup);

			}
		} catch (SQLException e) {
			String m = "Get All coupons of specific type " + CouponType + "failed";
			throw new CouponSystemException(m, e);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return (List<Coupon>) coupons;
	}
}
