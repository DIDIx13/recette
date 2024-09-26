package recette.datasource;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import org.junit.Test;
import recette.domain.DemoData;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public abstract class DatabaseSetupTest {

    protected TransactionManager transactionManager;
    protected DemoData demoData;

    @Test
    public void testDropCreateTables() throws Exception {
        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                DatabaseSetup setup = mm.getDatabaseSetup();
                setup.dropTables();
                setup.createTables();
                return null;
            }
        });
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testInsertData() throws Exception {
        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                DatabaseSetup setup = mm.getDatabaseSetup();
                setup.dropTables();
                setup.createTables();
                setup.insertData();
                return null;
            }
        });
    }
}
