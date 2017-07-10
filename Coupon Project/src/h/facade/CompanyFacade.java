package h.facade;

import java.util.Date;
import java.util.List;

import c.coupon.Coupon;
import c.coupon.CouponDBDAO;
import c.coupon.CouponType;
import d.company.Company;
import d.company.CompanyDBDAO;
import f.comp_coupon.Comp_coupnDBDAO;
import g.cust_coupon.Cust_CoupnDBDAO;
import i.couponSystem.CouponSystemException;

/**
 * this class is the Company facade it has company permissions. this class will
 * be used by the companies to manage their account and coupons. the Company
 * methods allow manipulation of the companies and the coupons. the CRUD methods
 * here are implemented on the Company,Coupon,Company coupon,Costumer coupon
 * Tables.
 */
public class CompanyFacade implements CouponClientFacade {

	private static Company comp;

	private CompanyDBDAO companydbdao = new CompanyDBDAO();
	private CouponDBDAO coupondbdao = new CouponDBDAO();
	private Comp_coupnDBDAO compcoupdbdao = new Comp_coupnDBDAO();
	private Cust_CoupnDBDAO custcoupondbdao = new Cust_CoupnDBDAO();


	public void createCoupon(Coupon coupon) throws CouponSystemException {
		try {
			List<Coupon> couponList = compcoupdbdao.getAllCoupons();
			for (Coupon coupon2 : couponList) {
				if (coupon2.getTitle().equals(coupon.getTitle())) {
					System.out.println("Coupon with the title: '" + coupon.getTitle() + "' already exists");
					return;
				}
			}
			coupondbdao.createCoupon(coupon);
			compcoupdbdao.createCompanyCoupon(comp, coupon);

		} catch (CouponSystemException e) {
			String m = "Failed to create coupon for company";
			throw new CouponSystemException(m);
		}
	}

	public void removeCoupon(Coupon coupon) throws CouponSystemException {
		List<Coupon> listcomp = compcoupdbdao.getCoupon(comp);
		for (Coupon coupon2 : listcomp)  {
			if (coupon.getId() == coupon2.getId()) {
				custcoupondbdao.deleteByCoupon(coupon);
				compcoupdbdao.deleteCoupon(coupon, comp);
				coupondbdao.deleteCoupon(coupon);

			}
			else {
				System.out.println("Coupon: "+coupon.getTitle()+ " does not exist for "+comp.getCompName()+ " company.");
			}
		}

	}

	public void updateCoupon(Coupon coupon, Date enddate, double price) throws CouponSystemException {
		coupondbdao.compnayUpdateCoupon(coupon, enddate, price);

	}

	public Coupon getCoupon(long id) throws CouponSystemException {
		return compcoupdbdao.getCouponById(id, comp);
	}

	public List<Coupon> getAllCoupons() throws CouponSystemException {
		return companydbdao.getCoupon(comp);
	}

	public List<Coupon> getCouponByType(CouponType couponType) throws CouponSystemException {
		return companydbdao.getCouponByType(comp, couponType);
	}

	public List<Coupon> getCouponByPrice(double price) throws CouponSystemException {
		return companydbdao.getCouponByPrice(comp, price);
	}

	public List<Coupon> getCouponByDate(Date date) throws CouponSystemException {
		return companydbdao.getCouponByDate(comp, date);
	}

	@Override
	public boolean login(String name, String password, Enum<ClientType> clientType) throws CouponSystemException {
		if (companydbdao.login(name, password) == true) {
			comp = companydbdao.getCompanyByName(name);
			return true;
		} else {

			return false;
		}
	}

	public static Company getCompany() {
		return comp;
	}
}
