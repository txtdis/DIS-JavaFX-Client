package ph.txtdis.exception;

public class DifferentDiscountException extends Exception {

	private static final long serialVersionUID = 3516715385558159044L;

	public DifferentDiscountException() {
		super("Discount for this item\ndiffers from previous");
	}
}
