package recette.datasource;

import core.datasource.DatabaseSetup;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public interface MapperManager {

    UniteMapper getUniteMapper();

    IngredientMapper getIngredientMapper();

    DatabaseSetup getDatabaseSetup();

    RecetteMapper getRecetteMapper();

}
