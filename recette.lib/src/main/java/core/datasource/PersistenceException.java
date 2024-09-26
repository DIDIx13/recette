package core.datasource;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class PersistenceException extends Exception {

    public PersistenceException() {
    }

    public PersistenceException(
            final String message) {
        super(message);
    }

    public PersistenceException(
            final String message, final Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(
            final Throwable cause) {
        super(cause);
    }

    public PersistenceException(
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
