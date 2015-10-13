package ph.txtdis.exception;

public class FailedFileWriteAccessException extends Exception {

    private static final long serialVersionUID = -5045178961218999208L;

    public FailedFileWriteAccessException(String file, String folder) {
        super("Failed to save " + file + " to\n" + folder
                + "\nchack file storage if external\n"
                + "or folder write access authorization ");
    }
}
