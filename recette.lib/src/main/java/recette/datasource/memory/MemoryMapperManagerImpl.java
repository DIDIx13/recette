package recette.datasource.memory;

import core.datasource.DatabaseSetup;
import recette.domain.DemoData;
import recette.datasource.IngredientMapper;
import recette.datasource.MapperManager;
import recette.datasource.RecetteMapper;
import recette.datasource.UniteMapper;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class MemoryMapperManagerImpl implements MapperManager {

    private final DemoData demoData;
    private UniteMapper uniteMapper;
    private IngredientMapper ingredientMapper;
    private RecetteMapper recetteMapper;
    private DatabaseSetup databaseSetup;

    MemoryMapperManagerImpl(final DemoData demoData) {
        this.demoData = demoData;

    }

    public DemoData getData() {
        return demoData;
    }

    @Override
    public UniteMapper getUniteMapper() {
        if (uniteMapper == null) {
            uniteMapper = new UniteMapperImpl(this);
        }
        return this.uniteMapper;
    }

    @Override
    public IngredientMapper getIngredientMapper() {
        if (ingredientMapper == null) {
            ingredientMapper = new IngredientMapperImpl(this);
        }
        return this.ingredientMapper;
    }

    @Override
    public RecetteMapper getRecetteMapper() {
        if (recetteMapper == null) {
            recetteMapper = new RecetteMapperImpl(this);
        }
        return this.recetteMapper;
    }

    public DatabaseSetup getDatabaseSetup() {
        if (databaseSetup == null) {
            databaseSetup = new DatabaseSetupImpl(this);
        }
        return this.databaseSetup;
    }

}
