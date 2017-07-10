package d.company;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import b.connection.ConnectionPool;
import c.coupon.Coupon;
import c.coupon.CouponType;
import i.couponSystem.CouponSystemException;

/** This class connects the Java program to the Derby Database.
 *  Includes the CRUD methods and methods for getting coupons/coupon lists for a specific company.
 *  This DBDAO layer knows to translate the outcome to Java beans or to collections.
 */
public class CompanyDBDAO implements CompanyDAO {
	
	@Override
	public void createCompany(Company company) throws CouponSystemException {
		String sql = "INSERT INTO COMPANY VALUES(?,?,?,?)";
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			List<Company> companyList=new ArrayList<>();
			companyList=getAllCompanies();
			for (Company company2 : companyList) {
				
				if(company.getId()==company2.getId()){
					System.out.println("Company not created because ID in use");
					continue;
				}
			}
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, company.getId());
			pstmt.setString(2, company.getCompName());
			pstmt.setString(3, company.getPassword());
			pstmt.setString(4, company.getEmail());
			pstmt.executeUpdate();
			System.out.println("Company: "+company.getCompName()+" created");

		} catch (SQLException e) {
			String m = "Failed to create a company";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void deleteCompany(Company company) throws CouponSystemException {
		String sql = "DELETE FROM COMPANY WHERE ID=?";
		String sql2 = "DELETE FROM CUSTOMER_COUPON WHERE COUPON_ID=?";
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql2);
			List<Coupon> couponList = getCoupon(company);
			for (Coupon coupon : couponList) {
				pstmt.setLong(1, coupon.getId());
				pstmt.executeUpdate();
			}
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, company.getId());
			if(pstmt!=null){
				int rowDelted=pstmt.executeUpdate();
				if(rowDelted>0){
					System.out.println("Company with the company name " + company.getCompName() + " deleted");
				}
			}
			
		} catch (SQLException e) {
			String m = "The company where id" + company.getId() + " failed to delete";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	@Override
	public void updateCompany(Company company) throws CouponSystemException {
		String sql = "UPDATE COMPANY SET ID= "+company.getId() + " ,password='"+company.getPassword() +"' ,email='"+company.getEmail()+"' WHERE COMP_NAME='"+company.getCompName() +"'";
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			String m = "The company where id=" + company.getId() + "not updated";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	@Override
	public Company getCompany(long id) throws CouponSystemException {
		Company company = new Company();
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			String sql = "SELECT * FROM COMPANY WHERE ID=?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				company.setId(rs.getLong(1));
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));

			} else {
				System.out.println("Company with the id " + id + " not found");
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			String m = "Failed get company with the id " + id;
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return company;

	}

	@Override
	public Company getCompanyByName(String name) throws CouponSystemException {
		Company company = new Company();
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "SELECT * FROM COMPANY WHERE COMP_NAME like '" + name + "'";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				company.setId(rs.getLong(1));
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));

			} else {
				System.out.println("Company with the name: " + name + " not found");
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			String m = "Failed get company with the name: " + name;
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return company;

	}

	@Override
	public List<Company> getAllCompanies() throws CouponSystemException {
		List<Company> companyList = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			String sql = "SELECT * FROM COMPANY";
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Company company = new Company();
				company.setId(rs.getLong(1));
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));
				companyList.add(company);
			}

		} catch (SQLException e) {
			String m = "Failed to get al the companies";
			throw new CouponSystemException(m);

		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return companyList;
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
					continue;

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
	public List<Coupon> getCouponByType(Company company, CouponType coupontype) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> typeList = new ArrayList<>();
		try {
			List<Coupon> couponList = getCoupon(company);
			for (Coupon coupon : couponList) {
				if (coupon.getType().equals(coupontype)) {
					typeList.add(coupon);
				}

			}

			if (typeList.isEmpty()) {
				System.out.println("Coupon list by type is empty");
			}
		} catch (CouponSystemException e) {
			throw new CouponSystemException("Failed to get company coupon by type");
		} finally

		{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return typeList;
	}

	@Override
	public List<Coupon> getCouponByPrice(Company company, double price) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> priceList = new ArrayList<>();
		try {
			List<Coupon> couponList = getCoupon(company);
			for (Coupon coupon : couponList) {
				if (coupon.getPrice() <= price) {
					priceList.add(coupon);
				}

			}

			if (priceList.isEmpty()) {
				System.out.println("Coupon list up to price is empty");
			}
		} catch (CouponSystemException e) {
			throw new CouponSystemException("Failed to get company coupon by price");
		} finally

		{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return priceList;
	}

	@Override
	public List<Coupon> getCouponByDate(Company company, Date date) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		List<Coupon> dateList = new ArrayList<>();
		try {
			List<Coupon> couponList = getCoupon(company);
			for (Coupon coupon : couponList) {
				if (coupon.getEndDate().before(date) || coupon.getEndDate().equals(date)) {
					dateList.add(coupon);
				}

			}

			if (couponList.isEmpty()) {
				System.out.println("Coupon list by date is empty");
			}
		} catch (CouponSystemException e) {
			throw new CouponSystemException("Failed to get company coupon by date");
		} finally

		{
			ConnectionPool.getInstance().returnConnection(con);
		}
		return dateList;
	}

	@Override
	public void deleteCoupon_comp(Company company) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "DELETE * FROM COMPANY_COUPON WHERE COMPANY_ID LIKE ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, company.getId());
			pstmt.executeUpdate();
			System.out.println("Coupons of the company id=" + company.getId() + "deleted");
		} catch (SQLException e) {
			String m = "The coupons where company id" + company.getId() + "failed to delete";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

	@Override
	public boolean login(String compName, String password) throws CouponSystemException {
		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			String sql = "SELECT PASSWORD FROM COMPANY WHERE COMP_NAME like '" + compName +"'";
			PreparedStatement pstmt=con.prepareStatement(sql);
			ResultSet rs  = pstmt.executeQuery();
			rs.next();
			if(password.equals(rs.getString(1))){
				System.out.println("Logged in as " + compName);
				return true;
			} else {
				System.out.println("Wrong details");
				return false;
			}
			
			
		} catch (SQLException e) {
			String m = "Failed to get login";
			throw new CouponSystemException(m);
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

}
