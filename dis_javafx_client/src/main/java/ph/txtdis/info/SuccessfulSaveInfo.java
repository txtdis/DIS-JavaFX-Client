package ph.txtdis.info;

public class SuccessfulSaveInfo extends Information {

	private static final long serialVersionUID = 2106954595618622708L;

	public SuccessfulSaveInfo(Object o) {
		super("Successfully posted:\n" + o);
	}
}
