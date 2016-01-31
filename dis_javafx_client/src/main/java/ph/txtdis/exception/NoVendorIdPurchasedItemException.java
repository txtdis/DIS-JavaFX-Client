package ph.txtdis.exception;

import ph.txtdis.dto.Item;

public class NoVendorIdPurchasedItemException extends Exception {

	private static final long serialVersionUID = 710144457878059025L;

	public NoVendorIdPurchasedItemException(Item item) {
		super(item + "\nis purchased\nbut has no vendor ID");
	}

}
