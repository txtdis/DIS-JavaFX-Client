package ph.txtdis.exception;

public class NoServerConnectionException extends Exception {

    private static final long serialVersionUID = -919135936918539710L;

    public NoServerConnectionException(String server) {
        super("No server connection;\ncheck link to " + server);
    }
}
