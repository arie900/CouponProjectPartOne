package f.comp_coupon;

/** This class represents the methods relevant to the join table: Company-coupon.
* These methods will be overwritten to allow interface with the relevant Database/file.
 * In this case we are interfacing with Derby DB.
 */
import java.util.List;
import c.coupon.Coupon;
import d.company.Company;
import i.couponSystem.CouponSystemException;

/** This class represents the methods relevant to the join table: Company-coupon.
* These methods will be overwritten to allow interface with the relevant Database/file.
 * In this case we are interfacing with Derby DB.
 */
public interface Comp_couponDAO {
	
	
	public void createCompanyCoupon(Company company, Coupon coupon) throws CouponSystemException;
	
	public void deleteCompanyAllCoupons(Company company) throws CouponSystemException;
	
	public void deleteCoupon(Coupon coupon, Company company) throws CouponSystemException;
	
	public void deleteCouponByCoupon(Coupon coupon) throws CouponSystemException;
	
	public List<Coupon> getCoupon(Company company) throws CouponSystemException;
	
	public Coupon getCouponById(long id, Company company) throws CouponSystemException;
	
	public List<Coupon> getAllCoupons() throws CouponSystemException;
	
}
