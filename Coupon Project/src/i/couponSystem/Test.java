package i.couponSystem;

import h.facade.ClientType;
import h.facade.CompanyFacade;

public class Test {
public static void main(String[] args) {
	try {
		CouponSystem cs = CouponSystem.getInstance();
		CompanyFacade cf = (CompanyFacade)cs.login("Oded", "2222", ClientType.COMPANY);
		System.out.println(cf.getCoupon(1));
		
		
		
		
	} catch (CouponSystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
