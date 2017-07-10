package h.facade;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import c.coupon.Coupon;
import c.coupon.CouponDBDAO;
import c.coupon.CouponType;
import e.customer.Customer;
import e.customer.CustomerDBDAO;
import g.cust_coupon.Cust_CoupnDBDAO;
import i.couponSystem.CouponSystemException;

/**
 * this class has the Customer permissions. 
 * this class will be used by the Customers to purchase and manage coupons. 
 * the Customer methods allow manipulation of the coupons.
 *  the CRUD methods here are implemented on the coupon beans. 
 *  this class also updates Customer,Coupon,Customer_coupon Tables.
 */
public class CustomerFacade implements CouponClientFacade {

	private static Customer cust;

	private CustomerDBDAO customerdbdao = new CustomerDBDAO();
	private Cust_CoupnDBDAO custcoupdbdao = new Cust_CoupnDBDAO();
	private CouponDBDAO coupondbdao=new CouponDBDAO();

	public void PurchaseCoupon(Coupon coupon) throws CouponSystemException {
		try {
		Calendar cal = Calendar.getInstance();
		List<Coupon> couponList = new ArrayList<>();
		couponList = custcoupdbdao.getCoupon(cust);
		for (Coupon coupon2 : couponList) {
			if (coupon2.getTitle().equals(coupon.getTitle())) {
				System.out.println("Coupon: '"+coupon.getTitle()+"' already exists");
				return;
			}
		}
		if (coupon.getAmount() > 0 && coupon.getEndDate().after(cal.getTime())) {
			coupon.setAmount(coupon.getAmount() - 1);
				
				custcoupdbdao.createCustomerCoupon(cust, coupon);
				coupondbdao.updateCoupon(coupon);
			}
		else{
			System.out.println("Coupon out of stock or date");
		}
		} catch (CouponSystemException e) {
			throw new CouponSystemException("Failed to purchase coupon for customer");

		}

	}
	
	public List<Coupon> getAvailableCouppon() throws CouponSystemException{
		return coupondbdao.getAllCoupons();
	}

	public List<Coupon> getAllPurchasedCoupons() throws CouponSystemException {
		return custcoupdbdao.getCoupon(cust);
	}
	
	public List<Coupon> getCouponsByType(CouponType couponType) throws CouponSystemException {
		return customerdbdao.getCouponByType(cust, couponType);
	}
	
	public List<Coupon> getCouponsByPrice(double price) throws CouponSystemException {
		return customerdbdao.getCouponByPrice(cust, price);
	}
	

	@Override
	public boolean login(String name, String password, Enum<ClientType> clientType) throws CouponSystemException {
		if (customerdbdao.login(name, password) == true) {
			cust = customerdbdao.getCustomerByName(name);
			return true;
		} else {
			return false;
		}
	}

	public static Customer getCustmer() {
		return cust;
	}
	
}
