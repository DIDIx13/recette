package core.datasource;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class EntiteUtiliseePersistenceException extends PersistenceException {

    public EntiteUtiliseePersistenceException() {
    }

    public EntiteUtiliseePersistenceException(
            final String message) {
        super(message);
    }

    public EntiteUtiliseePersistenceException(
            final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntiteUtiliseePersistenceException(
            final Throwable cause) {
        super(cause);
    }

    public EntiteUtiliseePersistenceException(
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
