package h.facade;



import i.couponSystem.CouponSystemException;

/**
 * this interface is implemented by the facades.
 * the main faction is to allow different types of login according to the client type
 */

public interface CouponClientFacade {
	
	
	public boolean login(String name,String password,Enum<ClientType> clientType)throws CouponSystemException;
	
	

}
