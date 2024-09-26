package recette.datasource.memory;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import recette.datasource.IngredientMapperTest;
import recette.datasource.MapperManager;
import recette.domain.DemoData;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class IngredientMapperImplTest extends IngredientMapperTest {

    public IngredientMapperImplTest() throws PersistenceException {
        demoData = new DemoData();
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
