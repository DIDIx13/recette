package core.datasource;

/**
 *
 * @author dominique huguenin (dominique.huguenin@rpn.ch)
 */
public class EntiteTropAnciennePersistenceException
        extends PersistenceException {

    public EntiteTropAnciennePersistenceException() {
    }

    public EntiteTropAnciennePersistenceException(final String message) {
        super(message);
    }

    public EntiteTropAnciennePersistenceException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }

    public EntiteTropAnciennePersistenceException(final Throwable cause) {
        super(cause);
    }

}
