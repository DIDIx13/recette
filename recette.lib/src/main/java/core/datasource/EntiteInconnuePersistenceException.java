package core.datasource;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class EntiteInconnuePersistenceException extends PersistenceException {

    public EntiteInconnuePersistenceException() {
    }

    public EntiteInconnuePersistenceException(
            final String message) {
        super(message);
    }

    public EntiteInconnuePersistenceException(
            final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntiteInconnuePersistenceException(
            final Throwable cause) {
        super(cause);
    }

    public EntiteInconnuePersistenceException(
            final String message,
            final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
