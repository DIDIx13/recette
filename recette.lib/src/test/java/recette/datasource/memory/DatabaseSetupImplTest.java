package recette.datasource.memory;

import recette.datasource.DatabaseSetupTest;
import recette.domain.DemoData;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class DatabaseSetupImplTest extends DatabaseSetupTest {

    public DatabaseSetupImplTest() {
        demoData = new DemoData();
        demoData.initialisation();
        this.transactionManager = MemoryTransactionManagerImpl.getInstance(demoData);
        

    }

}
