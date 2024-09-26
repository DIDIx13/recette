package recette.datasource.memory;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager.Operation;
import recette.datasource.MapperManager;
import recette.datasource.UniteMapperTest;
import recette.domain.DemoData;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class UniteMapperImplTest extends UniteMapperTest {

    public UniteMapperImplTest() throws PersistenceException {
        demoData = new DemoData();
        demoData.initialisation();
        transactionManager = MemoryTransactionManagerImpl.getInstance(demoData);

        this.transactionManager.executeTransaction(new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm) throws PersistenceException {
                DatabaseSetup setup = mm.getDatabaseSetup();
                setup.dropTables();
                setup.createTables();
                setup.insertData();
                return null;
            }
        });

    }

}
