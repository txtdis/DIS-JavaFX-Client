package ph.txtdis.exception;

import ph.txtdis.dto.Item;

public class NotSoldItemException extends Exception {

	private static final long serialVersionUID = -2318450396396173273L;

	public NotSoldItemException(Item item) {
		super(item + "\nCANNOT be sold.");
	}
}
