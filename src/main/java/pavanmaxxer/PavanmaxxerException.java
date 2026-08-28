package pavanmaxxer;

/**
 * Represents an input error that Pavanmaxxer can explain to the user.
 */
public class PavanmaxxerException extends Exception {
    /**
     * Creates an exception with a user-facing explanation.
     *
     * @param message explanation of the input error
     */
    public PavanmaxxerException(String message) {
        super(message);
    }
}
