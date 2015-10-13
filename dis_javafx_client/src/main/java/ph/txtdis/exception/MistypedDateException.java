package ph.txtdis.exception;

public class MistypedDateException extends Exception {

	private static final long serialVersionUID = -3092217465652312427L;

	public MistypedDateException() {
		super("Mistyped date;\ne.g., must be 5/1/02\nfor May 1, 2002");
	}
}
