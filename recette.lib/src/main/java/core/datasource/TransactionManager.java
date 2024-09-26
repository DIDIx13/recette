package core.datasource;

/**
 *
 * @author dominique huguenin (dominique.huguenin@rpn.ch)
 */
public interface TransactionManager<O> {

    Object executeTransaction(O operation) throws PersistenceException;

    public interface Operation<M> {

        Object execute(M mm) throws PersistenceException;
    }

}
