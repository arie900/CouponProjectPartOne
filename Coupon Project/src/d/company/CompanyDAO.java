package d.company;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import c.coupon.Coupon;
import c.coupon.CouponType;
import i.couponSystem.CouponSystemException;

/** All the methods relevant to company bean.
 * These methods will be overwritten to allow interface with the relevant Database/file.
 * In this case we are interfacing with Derby DB.
 */
public interface CompanyDAO {
	
	
	public void createCompany(Company company) throws SQLException, CouponSystemException;
	
	public void deleteCompany(Company company) throws CouponSystemException;
	
	public void updateCompany(Company company) throws CouponSystemException;
	
	public Company getCompany(long id) throws CouponSystemException;
	
	public Company getCompanyByName(String name) throws CouponSystemException; 
	
	public List<Company> getAllCompanies() throws CouponSystemException;
	
	public List<Coupon> getCoupon(Company company) throws CouponSystemException;
	
	public List<Coupon> getCouponByType(Company company,CouponType coupontype) throws CouponSystemException;
	
	public List<Coupon> getCouponByPrice(Company company, double price) throws CouponSystemException;
	
	public List<Coupon> getCouponByDate(Company company, Date date) throws CouponSystemException ;
	
	public void deleteCoupon_comp(Company company) throws CouponSystemException;
	
	public boolean login(String compName,String password) throws CouponSystemException;
	
	
}
