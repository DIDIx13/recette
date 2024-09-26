package recette.datasource.db;

import core.datasource.ContrainteNotNullPersistenceException;
import core.datasource.ContrainteUniquePersistenceException;
import core.datasource.EntiteInconnuePersistenceException;
import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.EntiteUtiliseePersistenceException;
import core.datasource.PersistenceException;
import core.datasource.db.SQL_ERREUR_CODES;
import core.domain.Audit;
import core.domain.AuditBase;
import core.domain.Entite;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dom
 */
public abstract class EntiteMapperImpl<E extends Entite> {

    private DbMapperManagerImpl mapperManager;
    private final String querySelectByFiltre;
    private final String queryDeleteById;

    public EntiteMapperImpl(final DbMapperManagerImpl mm,
            final String querySelectByFiltre,
            final String queryDeleteById) {
        this.mapperManager = mm;
        this.querySelectByFiltre = querySelectByFiltre;
        this.queryDeleteById = queryDeleteById;
    }

    public DbMapperManagerImpl getMapperManager() {
        return mapperManager;
    }

    public E create(final E entite) throws PersistenceException {
        if (entite == null) {
            return null;
        }
        E nouvelleEntite = null;

        Identifiant id = entite.getIdentifiant();
        if (id == null) {
            id = IdentifiantBase.builder().build();
        }

        try {

            createEntite(id, entite);

            nouvelleEntite = retrieveEntite(id);

        } catch (SQLException ex) {
            if (ex.getSQLState()
                    .equals(SQL_ERREUR_CODES.POSTGRESQL.CONSTAINT_NOT_NULL_VIOLATION)) {
                throw new ContrainteNotNullPersistenceException(ex);
            }
            if (ex.getSQLState()
                    .equals(SQL_ERREUR_CODES.POSTGRESQL.CONSTRAINT_UNIQUE_VIOLATION)) {
                throw new ContrainteUniquePersistenceException(ex);
            }
            if (ex.getSQLState()
                    .equals(SQL_ERREUR_CODES.POSTGRESQL.CONSTRAINT_FOREIGN_KEY_VIOLATION)) {
                throw new EntiteInconnuePersistenceException(ex);
            }

            throw new PersistenceException(ex);
        }

        return nouvelleEntite;

    }

    public E retrieve(final Identifiant id) throws PersistenceException {
        if (id == null) {
            return null;
        }
        E entite = null;

        try {
            entite = retrieveEntite(id);

        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
        return entite;

    }

    public List<E> retrieve(final String filtre) throws PersistenceException {
        List<E> entites = new ArrayList<>();

        if (filtre == null) {
            return entites;
        }
        try {
            entites = retrieveEntites(filtre);
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
        return entites;

    }

    protected List<E> retrieveEntites(
            final String filtre)
            throws PersistenceException, SQLException {

        List<E> entites = new ArrayList<>();

        try (PreparedStatement ps
                = this.mapperManager.createPreparedStatement(this.querySelectByFiltre)) {
            ps.setString(1,
                    filtre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                E entite = readEntite(rs);
                if (entite != null) {
                    entites.add(entite);
                }
            }

        }

        return entites;
    }

    public void update(final E entite) throws PersistenceException {
        if (entite == null) {
            return;
        }
        if (entite.getIdentifiant() == null) {
            return;
        }
        try {

            E entiteTrouvee
                    = this.retrieveEntite(entite.getIdentifiant());
            if (entiteTrouvee == null) {
                throw new EntiteInconnuePersistenceException(
                        String.format("Erreur: l'entitée (%s) "
                                + "n'est pas connue!",
                                entite.toString()));
            }

            updateEntite(entite);

        } catch (SQLException ex) {
            if (ex.getSQLState()
                    .equals(SQL_ERREUR_CODES.POSTGRESQL.CONSTAINT_NOT_NULL_VIOLATION)) {
                throw new ContrainteNotNullPersistenceException(ex);
            }
            if (ex.getSQLState()
                    .equals(SQL_ERREUR_CODES.POSTGRESQL.CONSTRAINT_UNIQUE_VIOLATION)) {
                throw new ContrainteUniquePersistenceException(ex);
            }
            if (ex.getSQLState()
                    .equals(SQL_ERREUR_CODES.POSTGRESQL.CONSTRAINT_FOREIGN_KEY_VIOLATION)) {
                throw new EntiteInconnuePersistenceException(ex);
            }

            throw new PersistenceException(ex);
        }

    }

    public void delete(final E entite) throws PersistenceException {
        if (entite == null) {
            return;
        }
        if (entite.getIdentifiant() == null) {
            return;
        }
        try {
            E entiteTrouvee
                    = this.retrieveEntite(entite.getIdentifiant());
            if (entiteTrouvee == null) {
                throw new EntiteInconnuePersistenceException(
                        String.format("Erreur: l'entitée (%s) "
                                + "n'est pas connue!", entite.toString()));
            }
            try (PreparedStatement ps = this.mapperManager
                    .createPreparedStatement(this.queryDeleteById)) {
                ps.setString(1, entite.getIdentifiant().getUUID());
                ps.setLong(2,
                        entite.getIdentifiant().getVersion());
                int row = ps.executeUpdate();

                if (row == 0) {
                    throw new EntiteTropAnciennePersistenceException(
                            entite.toString());
                }
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().equals(
                    SQL_ERREUR_CODES.POSTGRESQL.CONSTRAINT_FOREIGN_KEY_VIOLATION)) {
                throw new EntiteUtiliseePersistenceException(ex);
            }
            throw new PersistenceException(ex);
        }
    }

    protected Identifiant readIdentifiant(final ResultSet rs)
            throws SQLException {
        String uuid = rs.getString(SQL.ENTITES.ATTRIBUTS.UUID);
        Long version = rs.getLong(SQL.ENTITES.ATTRIBUTS.VERSION);

        return IdentifiantBase.builder()
                .uuid(uuid)
                .version(version)
                .build();
    }

    protected Audit readAudit(final ResultSet rs)
            throws SQLException {
        Timestamp rsDateCreation
                = rs.getTimestamp(SQL.ENTITES.ATTRIBUTS.DATE_CREATION);
        String rsUserCreation
                = rs.getString(SQL.ENTITES.ATTRIBUTS.USER_CREATION);
        Timestamp rsDateModification
                = rs.getTimestamp(SQL.ENTITES.ATTRIBUTS.DATE_MODIFICATION);
        String rsUserModification
                = rs.getString(SQL.ENTITES.ATTRIBUTS.USER_MODIFICATION);
        Timestamp rsDateSuppression
                = rs.getTimestamp(SQL.ENTITES.ATTRIBUTS.DATE_SUPPRESSION);
        String rsUserSuppression
                = rs.getString(SQL.ENTITES.ATTRIBUTS.USER_SUPPRESSION);

        AuditBase.Builder builder = AuditBase.builder()
                .userCreation(rsUserCreation)
                .userModification(rsUserModification)
                .userSuppression(rsUserSuppression);

        if (rsDateCreation != null) {
            builder.dateCreation(rsDateCreation.toInstant());
        }

        if (rsDateModification != null) {
            builder.dateModification(rsDateModification.toInstant());
        }

        if (rsDateSuppression != null) {
            builder.dateSuppression(rsDateSuppression.toInstant());
        }

        return builder.build();
    }

    protected abstract void createEntite(
            Identifiant id, E entite)
            throws SQLException, PersistenceException;

    protected abstract E retrieveEntite(
            Identifiant identifiant)
            throws SQLException, PersistenceException;

    protected abstract void updateEntite(
            E entite)
            throws SQLException, PersistenceException;

    protected abstract E readEntite(ResultSet rs)
            throws SQLException, PersistenceException;

}
