package recette.datasource.memory;

import core.datasource.TransactionManager;
import javax.naming.NamingException;
import recette.domain.DemoData;

/**
 *
 * @author dominique huguenin <dominique.huguenin at rpn.ch>
 */
public final class TransactionManagerFactory {

    private TransactionManagerFactory() {
    }

    public static TransactionManager getInstance() throws NamingException {

        DemoData data = new DemoData();
        data.initialisation(1L, true);

        return MemoryTransactionManagerImpl.getInstance(data);

    }

}
