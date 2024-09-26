package recette.datasource.memory;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import recette.datasource.MapperManager;
import recette.domain.DemoData;
import recette.datasource.RecetteMapperTest;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class RecetteMapperImplTest extends RecetteMapperTest {

    public RecetteMapperImplTest() throws PersistenceException {
        demoData = new DemoData();
        demoData.initialisation();
        transactionManager = MemoryTransactionManagerImpl.getInstance(demoData);

        this.transactionManager.executeTransaction(new TransactionManager.Operation<MapperManager>() {
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
