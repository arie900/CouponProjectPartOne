package e.customer;


import java.util.List;

import c.coupon.Coupon;
import c.coupon.CouponType;
import i.couponSystem.CouponSystemException;

/** All the methods relevant to customer bean.
 * These methods will be overwritten to allow interface with the relevant Database/file.
 * In this case we are interfacing with Derby DB.
 */
public interface CustomerDAO {
	
	
	public void createCustomer(Customer customer) throws CouponSystemException;
	
	public void deleteCustomer(Customer customer) throws CouponSystemException;
	
	public void updateCustomer(Customer customer) throws CouponSystemException;
	
	public Customer getCustomer(long id) throws CouponSystemException;
	
	public Customer getCustomerByName(String name) throws CouponSystemException;
	
	public List<Customer> getAllCustomer() throws CouponSystemException;
	
	public List<Coupon> getCoupons(Customer customer) throws CouponSystemException;
	
	public List<Coupon> getCouponByType(Customer customer,CouponType couponType) throws CouponSystemException;
	
	public List<Coupon> getCouponByPrice(Customer customer,double price) throws CouponSystemException;
	
	public void deleteCoupon_cust(Customer customer) throws CouponSystemException;
	
	public boolean login(String compName, String password) throws CouponSystemException;

}
