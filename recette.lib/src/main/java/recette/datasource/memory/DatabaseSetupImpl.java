package recette.datasource.memory;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class DatabaseSetupImpl implements DatabaseSetup {

    private final MemoryMapperManagerImpl mapperManager;

    DatabaseSetupImpl(final MemoryMapperManagerImpl mm) {
        this.mapperManager = mm;
    }

    @Override
    public void createTables() throws PersistenceException {
        this.mapperManager.getData().getRecettes().clear();
        this.mapperManager.getData().getIngredients().clear();
        this.mapperManager.getData().getUnites().clear();
    }

    @Override
    public void dropTables() throws PersistenceException {
        this.mapperManager.getData().getRecettes().clear();
        this.mapperManager.getData().getIngredients().clear();
        this.mapperManager.getData().getUnites().clear();
    }

    @Override
    public void insertData() throws PersistenceException {
        this.mapperManager.getData().initialisation(1L, true);
    }

}
