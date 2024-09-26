package recette.datasource.memory;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.datasource.TransactionManager.Operation;
import java.util.logging.Logger;
import recette.datasource.MapperManager;
import recette.domain.DemoData;

/**
 *
 * @author dominique huguenin <dominique.huguenin at rpn.ch>
 */
public final class MemoryTransactionManagerImpl
        implements TransactionManager<Operation<MapperManager>> {

    private static MemoryTransactionManagerImpl transactionManager;
    private static final Logger LOG
            = Logger.getLogger(MemoryTransactionManagerImpl.class.getName());

    public static TransactionManager getInstance(final DemoData data) {
        if (transactionManager == null) {
            transactionManager = new MemoryTransactionManagerImpl(data);
        }
        return transactionManager;
    }
    private final DemoData data;

    private MemoryTransactionManagerImpl(final DemoData data) {
        this.data = data;
    }

    @Override
    public Object executeTransaction(final Operation<MapperManager> operation)
            throws PersistenceException {

        return operation.execute(
                new MemoryMapperManagerImpl(data));
    }

}
