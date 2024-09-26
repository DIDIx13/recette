package recette.datasource.db;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import recette.domain.Composant;
import recette.domain.DemoData;
import recette.domain.Ingredient;
import recette.domain.Recette;
import recette.domain.Unite;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class DatabaseSetupImpl implements DatabaseSetup {
//CHECKSTYLE.OFF: MagicNumber

    private final DbMapperManagerImpl mapperManager;
    private static final Logger LOG = Logger.getLogger(
            DatabaseSetupImpl.class.getName());

    public DatabaseSetupImpl(final DbMapperManagerImpl mm) {
        this.mapperManager = mm;
    }

    @Override
    public void createTables() throws PersistenceException {

        try (Statement requete = this.mapperManager.createStatement();) {
            requete.addBatch(SQL.UNITES.CREATE_TABLE);
            requete.addBatch(SQL.INGREDIENTS.CREATE_TABLE);
            requete.addBatch(SQL.COMPOSANTS.CREATE_TABLE);
            requete.addBatch(SQL.RECETTES.CREATE_TABLE);

            requete.addBatch(SQL.UNITES.ALTER_TABLE);
            requete.addBatch(SQL.INGREDIENTS.ALTER_TABLE);
            requete.addBatch(SQL.COMPOSANTS.ALTER_TABLE);
            requete.addBatch(SQL.RECETTES.ALTER_TABLE);

            requete.executeBatch();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }

    }

    @Override
    public void dropTables() throws PersistenceException {
        try (Statement requete = this.mapperManager.createStatement();) {
            requete.addBatch(SQL.COMPOSANTS.DROP_TABLE);
            requete.addBatch(SQL.INGREDIENTS.DROP_TABLE);
            requete.addBatch(SQL.UNITES.DROP_TABLE);
            requete.addBatch(SQL.RECETTES.DROP_TABLE);
            requete.executeBatch();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }

    }

    private static final String USER_CREATION_DEMO = "DEMO";

    @Override
    public void insertData() throws PersistenceException {
        DemoData data = new DemoData();
        data.initialisation();
        try {
            this.mapperManager.createStatement()
                    .execute("SET CONSTRAINTS ALL DEFERRED");

            try (PreparedStatement rc
                    = this.mapperManager.createPreparedStatement(SQL.UNITES.INSERT)) {

                for (Unite e : data.getUnites().values()) {

                    rc.setString(1, e.getIdentifiant().getUUID());
                    rc.setString(2, e.getCode());
                    rc.setString(3,
                            USER_CREATION_DEMO);

                    rc.addBatch();
                }
                rc.executeBatch();
            }

            try (PreparedStatement rc
                    = this.mapperManager.createPreparedStatement(SQL.INGREDIENTS.INSERT)) {

                for (Ingredient e : data.getIngredients().values()) {

                    rc.setString(1, e.getIdentifiant().getUUID());
                    rc.setString(2, e.getNom());
                    rc.setString(3, e.getDetail());
                    if (e.getRecette() != null) {
                        rc.setString(4, e.getRecette().getIdentifiant().
                                getUUID());
                    } else {
                        rc.setNull(4, java.sql.Types.VARCHAR);
                    }
                    rc.setString(5,
                            USER_CREATION_DEMO);

                    rc.addBatch();
                }
                rc.executeBatch();
            }

            try (PreparedStatement rc
                    = this.mapperManager.createPreparedStatement(SQL.RECETTES.INSERT);
                    PreparedStatement rt
                    = this.mapperManager.createPreparedStatement(SQL.COMPOSANTS.INSERT)) {

                for (Recette e : data.getRecettes().values()) {

                    rc.setString(1, e.getIdentifiant().getUUID());
                    rc.setString(2, e.getNom());
                    rc.setString(3, e.getDetail());
                    rc.setString(4, e.getPreparation());
                    rc.setInt(5, e.getNombrePersonnes());
                    rc.setString(6,
                            USER_CREATION_DEMO);

                    rc.addBatch();

                    for (int i = 0; i < e.getComposants().size(); i += 1) {
                        Composant t = e.getComposants().get(i);
                        rt.setString(1, e.getIdentifiant().getUUID());
                        rt.setInt(2, t.getNumero());
                        rt.setInt(3, i);
                        if (t.getQuantite() != null) {
                            rt.setDouble(4, t.getQuantite());
                        } else {
                            rt.setNull(4, java.sql.Types.DOUBLE);
                        }
                        rt.setString(5, t.getCommentaire());
                        rt.setString(6, t.getIngredient().getIdentifiant()
                                .getUUID());
                        if (t.getUnite() != null) {
                            rt.setString(7, t.getUnite().getIdentifiant()
                                    .getUUID());
                        } else {
                            rt.setNull(7, java.sql.Types.VARCHAR);
                        }
                        rt.addBatch();
                    }
                }
                rc.executeBatch();
                rt.executeBatch();

            }

        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }

    }
//CHECKSTYLE.ON: MagicNumber

}
