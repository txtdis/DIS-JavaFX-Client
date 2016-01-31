package ph.txtdis.exception;

import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Item;

public class ToBeReturnedItemNotPurchasedWithinTheLastSixMonths extends Exception {

	private static final long serialVersionUID = 1145267869617171227L;

	public ToBeReturnedItemNotPurchasedWithinTheLastSixMonths(Item i, Customer c) {
		super(c + " did not buy\n" + i + " in the last six months");
	}
}
