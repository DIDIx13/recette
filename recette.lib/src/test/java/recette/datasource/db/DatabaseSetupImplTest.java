package recette.datasource.db;

import javax.sql.DataSource;
import recette.datasource.DatabaseSetupTest;
import recette.domain.DemoData;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class DatabaseSetupImplTest extends DatabaseSetupTest {

    public DatabaseSetupImplTest() {
        this.demoData = new DemoData();
        this.demoData.initialisation();

        DataSource datasource = TestDataSourceFactory.getInstance();
        this.transactionManager = DbTransactionManagerImpl.getInstance(datasource);

    }

}
