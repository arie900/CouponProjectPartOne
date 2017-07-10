package g.cust_coupon;

/** This class represents the methods relevant to the join table: Customer-coupon.
* These methods will be overwritten to allow interface with the relevant Database/file.
 * In this case we are interfacing with Derby DB.
 */

import java.util.List;

import c.coupon.Coupon;
import e.customer.Customer;
import i.couponSystem.CouponSystemException;

/** This class represents the methods relevant to the join table: Customer-coupon.
* These methods will be overwritten to allow interface with the relevant Database/file.
 * In this case we are interfacing with Derby DB.
 */
public interface Cust_couponDAO {
	
	
	public void createCustomerCoupon(Customer customer, Coupon coupon) throws  CouponSystemException;
	
	public void deleteCustomerAllCoupons(Customer customer) throws CouponSystemException;
	
	public void deleteByCoupon(Coupon coupon) throws CouponSystemException;
	
	public List<Coupon> getCoupon(Customer customer) throws CouponSystemException;
	
	public List<Coupon> getCouponByCoupon(Coupon coupon) throws CouponSystemException;
	
}
