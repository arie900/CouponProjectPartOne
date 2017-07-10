package c.coupon;

import java.util.Date;
import java.util.List;

import i.couponSystem.CouponSystemException;

/** All the methods relevant to coupon bean.
 * These methods will be overwritten to allow interface with the relevant Database/file.
 * In this case we are interfacing with Derby DB.
 */
public interface CouponDAO {
	
	public void createCoupon(Coupon coupon) throws CouponSystemException;
	
	public void deleteCoupon(Coupon coupon) throws CouponSystemException;
	
	public void updateCoupon(Coupon coupon) throws CouponSystemException;
	
	public void compnayUpdateCoupon(Coupon coupon, Date enddate, double price) throws CouponSystemException;
	
	public Coupon getCoupon(long id) throws CouponSystemException;
	
	public List<Coupon> getAllCoupons() throws CouponSystemException;
	
	List<Coupon> getCouponByType(CouponType CouponType) throws CouponSystemException;

}
