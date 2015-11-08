package ph.txtdis.exception;

import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Item;

public class NotAnItemToBeSoldToCustomerException extends Exception {

	private static final long serialVersionUID = 5868566649654122201L;

	public NotAnItemToBeSoldToCustomerException(Item item, Customer customer) {
		super(item + "\nis not to be sold to\n" + customer);
	}
}
