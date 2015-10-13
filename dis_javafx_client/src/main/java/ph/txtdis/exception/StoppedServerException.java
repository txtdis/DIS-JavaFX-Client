package ph.txtdis.exception;

public class StoppedServerException extends Exception {

    private static final long serialVersionUID = 7515581013872748074L;

    public StoppedServerException() {
        super("Database stopped;\nreboot server");
    }

}
