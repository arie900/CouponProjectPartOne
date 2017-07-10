package i.couponSystem;




import b.connection.ConnectionPool;
import h.facade.AdminFacade;
import h.facade.ClientType;
import h.facade.CompanyFacade;
import h.facade.CouponClientFacade;
import h.facade.CustomerFacade;

/**
 * this is the backbone of the coupon management system.
 * it is a singleton class.
 * creating an instance of this class will start the connection pool the DailyCouponExpirationTask.
 * also allow login and retrieve the relevant facade.
 */
public class CouponSystem {
	private DailyCouponExpirationTask dailey=new DailyCouponExpirationTask();
	private static CouponSystem instance=new CouponSystem();
	private Thread daileyThread=new Thread(dailey);
	
	private CouponSystem(){
		try {
			ConnectionPool.getInstance();
		} catch (CouponSystemException e) {
			e.getMessage();
		}
		daileyThread.setDaemon(true);
		daileyThread.start();
	}
	
	public static CouponSystem getInstance() throws CouponSystemException {
		return instance;
	}

	public CouponClientFacade login(String name, String password, ClientType clientType) throws CouponSystemException {
		switch (clientType) {
		case ADMIN:
			AdminFacade af=new AdminFacade();
				if(af.login(name, password, clientType)==true){
					return new AdminFacade();
				}
				else{
					System.out.println("Cannot login as admin");
				}
					break;
				

		case COMPANY:
			CompanyFacade cf = new CompanyFacade();
			if (cf.login(name, password, clientType) == true) {
				return new CompanyFacade();
			} else {
				System.out.println("Cannot login as company: '"+name);
			}
			break;

		case CUSTOMER:
			CustomerFacade customerfacade = new CustomerFacade();
			if (customerfacade.login(name, password, clientType) == true) {
				return new CustomerFacade();
			} else {
				System.out.println("Cannot login as customer: '"+name);
			}
			break;

		}
		return null;
	}
	public void shutdown() throws CouponSystemException{
		dailey.stopTask();
        ConnectionPool.getInstance().closeAllConnection();
        System.out.println("Coupon system stopped");
		}

		
	}


