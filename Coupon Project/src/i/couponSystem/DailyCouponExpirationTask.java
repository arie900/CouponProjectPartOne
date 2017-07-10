package i.couponSystem;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import c.coupon.Coupon;
import c.coupon.CouponDBDAO;
import f.comp_coupon.Comp_coupnDBDAO;
import g.cust_coupon.Cust_CoupnDBDAO;

/**
 * this class is used to create a thread that checks for expired coupons and deletes them.
 * this is a daily task, the thread uses the sleep method to wait 24 h between each run.
 */

public class DailyCouponExpirationTask implements Runnable {

	private CouponDBDAO coupondbdao = new CouponDBDAO();
	private Comp_coupnDBDAO compcoupondbdao = new Comp_coupnDBDAO();
	private Cust_CoupnDBDAO custcoupondbdao = new Cust_CoupnDBDAO();
	private boolean quit = false;

	public DailyCouponExpirationTask() {
	}

	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		System.out.println("Dailey coupon expiration thread started");
		while (!quit) {
			try {
				System.out.println("checking for expired coupons");
				List<Coupon> couponlist = new ArrayList<>();
				couponlist = coupondbdao.getAllCoupons();
				for (Coupon coupon : couponlist) {
					if (coupon.getEndDate().before(today)) {
						custcoupondbdao.deleteByCoupon(coupon);
						compcoupondbdao.deleteCouponByCoupon(coupon);
						coupondbdao.deleteCoupon(coupon);
					}
				}

			} catch (Exception e) {
			}

			try {
//				Thread.sleep(1000);
				Thread.sleep(24*60*60*1000); //this will check once every day. but for the test we will not use this.
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public  void stopTask() {
		quit = true;
		Thread.currentThread().interrupt();
		
	}
}
