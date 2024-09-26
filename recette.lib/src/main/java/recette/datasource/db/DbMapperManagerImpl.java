package recette.datasource.db;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import recette.datasource.IngredientMapper;
import recette.datasource.MapperManager;
import recette.datasource.RecetteMapper;
import recette.datasource.UniteMapper;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class DbMapperManagerImpl implements MapperManager {

    private final Connection connection;
    private DatabaseSetup databaseSetup;
    private UniteMapper uniteMapper;
    private IngredientMapper ingredientMapper;
    private RecetteMapper recetteMapper;

    public DbMapperManagerImpl(final Connection conn) {
        this.connection = conn;
    }

    PreparedStatement createPreparedStatement(final String query)
            throws PersistenceException {
        try {
            return this.connection.prepareStatement(query);
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }

    Statement createStatement()
            throws PersistenceException {
        try {
            return this.connection.createStatement();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public UniteMapper getUniteMapper() {
        if (this.uniteMapper == null) {
            this.uniteMapper = new UniteMapperImpl(this);
        }
        return this.uniteMapper;
    }

    @Override
    public RecetteMapper getRecetteMapper() {
        if (this.recetteMapper == null) {
            this.recetteMapper = new RecetteMapperImpl(this);
        }
        return this.recetteMapper;
    }

    @Override
    public IngredientMapper getIngredientMapper() {
        if (this.ingredientMapper == null) {
            this.ingredientMapper = new IngredientMapperImpl(this);
        }
        return this.ingredientMapper;
    }

    @Override
    public DatabaseSetup getDatabaseSetup() {
        if (this.databaseSetup == null) {
            this.databaseSetup = new DatabaseSetupImpl(this);
        }
        return this.databaseSetup;

    }

}
