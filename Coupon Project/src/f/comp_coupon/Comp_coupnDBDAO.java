package f.comp_coupon;

/** This class connects the Java program to the Derby Database.
 *  Includes the Create, Delete and Get methods and methods for getting coupons/coupon lists for a specific company and/or coupon.
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
import d.company.Company;
import i.couponSystem.CouponSystemException;

/**
 * This class connects the Java program to the Derby Database. Includes the
 * Create, Delete and Get methods and methods for getting coupons/coupon lists
 * for a specific company and/or coupon. This DBDAO layer knows to translate the
 * outcome to Java beans or to collections.
 */
public class Comp_coupnDBDAO implements Comp_couponDAO {

	@Override
	public void createCompanyCoupon(Company company, Coupon coupon) throws CouponSystemException {

		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "INSERT INTO Company_Coupon values(?,?)";
		try {
			List<Coupon> couponList = new ArrayList<>();
			couponList = getAllCoupons();
			for (Coupon coupon2 : couponList) {
				if (coupon.getId() == coupon2.getId()) {
					System.out.println("Coupon: " + coupon.getTitle() + " already exists");
					return;
				}
			}
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, company.getId());
			pstmt.setLong(2, coupon.getId());
			pstmt.executeUpdate();
			System.out.println("Coupons " + coupon.getTitle() + " added to company " + company.getCompName());
		} catch (SQLException e) {
			String m = "Failed to create coupon for company";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void deleteCompanyAllCoupons(Company company) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();

		String sql = "DELETE FROM Company_Coupon WHERE COMPANY_ID = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, company.getId());
			int rowDeleted = pstmt.executeUpdate();
			if (rowDeleted > 0) {
				System.out.println("All coupouns for " + company.getCompName() + " deleted");
			} else {
				System.out.println("No coupons found for company: " + company.getCompName());
			}
		} catch (SQLException e) {
			String m = "Failed to delete coupons for company";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void deleteCoupon(Coupon coupon, Company company) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();

		String sql = "DELETE FROM Company_Coupon WHERE COMPANY_ID =" + company.getId() + "and  COUPON_ID="
				+ coupon.getId();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			int rowDeleted = pstmt.executeUpdate();
			if (rowDeleted > 0) {
				System.out.println("Coupon: " + coupon.getTitle() + " deleted for company " + company.getCompName());
			} else {
				System.out.println("No coupon " + coupon.getTitle() + " found for company " + company.getCompName());
			}
		} catch (SQLException e) {
			String m = "Failed to delete coupon from company";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void deleteCouponByCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();

		String sql = "DELETE FROM Company_Coupon WHERE  COUPON_ID=?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, coupon.getId());
			int rowDeleted = pstmt.executeUpdate();
			if (rowDeleted > 0) {
				System.out.println("Coupon: " + coupon.getTitle() + " deleted");
			} else {
				System.out.println("No coupon " + coupon.getTitle() + " found in company coupon table");
			}
		} catch (SQLException e) {
			String m = "Failed to delete coupon from company";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public List<Coupon> getCoupon(Company company) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> couponList = new ArrayList<>();
		String sql = "SELECT * FROM COMPANY_COUPON WHERE COMPANY_ID=?";
		String sql2 = "SELECT * FROM COUPON WHERE ID=?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			PreparedStatement pstmt2 = con.prepareStatement(sql2);
			pstmt.setLong(1, company.getId());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Coupon coupon = new Coupon();
				coupon.setId(rs.getLong(2));
				pstmt2 = con.prepareStatement(sql2);
				pstmt2.setLong(1, coupon.getId());
				ResultSet rs2 = pstmt2.executeQuery();
				while (rs2.next()) {
					coupon.setTitle(rs2.getString(2));
					coupon.setStartDate(rs2.getDate(3));
					coupon.setEndDate(rs2.getDate(4));
					coupon.setAmount(rs2.getInt(5));
					coupon.setType(CouponType.valueOf(rs2.getString(6)));
					coupon.setMessage(rs2.getString(7));
					coupon.setPrice(rs2.getDouble(8));
					coupon.setImage(rs2.getString(9));
					couponList.add(coupon);

				}

			}

		} catch (SQLException e) {
			String m = "Failed to get all the coupons";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return couponList;

	}

	@Override
	public Coupon getCouponById(long id, Company company) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "SELECT * FROM COMPANY_COUPON WHERE COMPANY_ID=? AND COUPON_ID=?";
		String sql2 = "SELECT * FROM COUPON WHERE ID=?";
		Coupon coupon = new Coupon();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			PreparedStatement pstmt2 = con.prepareStatement(sql2);
			
			pstmt.setLong(1, company.getId());
			pstmt.setLong(2, id);

			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				coupon.setId(rs.getLong(2));
				pstmt2.setLong(1,coupon.getId());
				ResultSet rs2 = pstmt2.executeQuery();
				while (rs2.next()){
					coupon.setTitle(rs2.getString(2));
					coupon.setStartDate(rs2.getDate(3));
					coupon.setEndDate(rs2.getDate(4));
					coupon.setAmount(rs2.getInt(5));
					coupon.setType(CouponType.valueOf(rs2.getString(6)));
					coupon.setMessage(rs2.getString(7));
					coupon.setPrice(rs2.getDouble(8));
					coupon.setImage(rs2.getString(9));
				}
			
			}
		} catch (SQLException e) {
			throw new CouponSystemException("failed to get coupon by id and company");
			
		}
		finally {
			ConnectionPool.getInstance().returnConnection(con);
	}
		return coupon;
	}
	
	
	@Override
	public List<Coupon> getAllCoupons() throws CouponSystemException {
		String sql = "SELECT * FROM COMPANY_COUPON";
		String sql2 = "SELECT * FROM COUPON WHERE ID=?";
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> coupons = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Coupon coup = new Coupon();
				coup.setId(rs.getLong(2));
				PreparedStatement pstmt2 = con.prepareStatement(sql2);
				pstmt2.setLong(1, coup.getId());
				ResultSet rs2 = pstmt2.executeQuery();
				while (rs2.next()) {
					coup.setTitle(rs2.getString(2));
					coupons.add(coup);

				}
			}
		} catch (SQLException e) {
			String m = "Get All coupons failed";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return coupons;
	}


}
