package i.couponSystem;

/**
 * Exception class for all the coupon management system.
 * this class will be used to handle all the exceptions.
 * this allows for custom exception messages and ensures the unwanted messages (for example stack-trace) are not displayed. 
 */


public class CouponSystemException extends Exception {

	private static final long serialVersionUID = 1L;

	public CouponSystemException() {
		super();
	}

	public CouponSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponSystemException(String message) {
		super(message);
	}

}
