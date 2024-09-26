package recette.datasource.db;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager.Operation;
import javax.sql.DataSource;
import recette.datasource.MapperManager;
import recette.datasource.UniteMapperTest;
import recette.domain.DemoData;

/**
 *
 * @author dom
 */
public class UniteMapperImplTest extends UniteMapperTest {

    public UniteMapperImplTest() throws PersistenceException {
        demoData = new DemoData();
        demoData.initialisation();

        DataSource datasource = TestDataSourceFactory.getInstance();
        this.transactionManager = DbTransactionManagerImpl.getInstance(datasource);

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
