package recette.datasource.db;

import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.PersistenceException;
import core.domain.Audit;
import core.domain.Identifiant;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import recette.datasource.UniteMapper;
import recette.domain.Unite;
import recette.domain.UniteBase;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class UniteMapperImpl extends EntiteMapperImpl<Unite> implements UniteMapper {
//CHECKSTYLE.OFF: MagicNumber

    public UniteMapperImpl(final DbMapperManagerImpl mm) {
        super(mm,
                SQL.UNITES.SELECT_BY_FILTRE,
                SQL.UNITES.DELETE_BY_UUID);
    }

    @Override
    protected void createEntite(
            final Identifiant id,
            final Unite entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(SQL.UNITES.INSERT)) {
            ps.setString(1,
                    id.getUUID());
            if (entite.getCode() != null) {
                ps.setString(2,
                        entite.getCode());
            } else {
                ps.setNull(2,
                        Types.VARCHAR);
            }
            if (entite.getAudit() != null
                    && entite.getAudit().getUserCreation() != null) {
                ps.setString(3,
                        entite.getAudit().getUserCreation());
            } else {
                ps.setNull(3,
                        Types.VARCHAR);
            }
            ps.executeUpdate();
        }
    }

    @Override
    protected Unite retrieveEntite(
            final Identifiant id) throws SQLException, PersistenceException {

        Unite entite = null;
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(SQL.UNITES.SELECT_BY_UUID)) {
            ps.setString(1, id.getUUID());
            ps.setString(1,
                    id.getUUID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entite = readEntite(rs);
            }

        }
        return entite;
    }

    @Override
    protected void updateEntite(
            final Unite entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(
                        SQL.UNITES.UPDATE)) {
            if (entite.getCode() != null) {
                ps.setString(1,
                        entite.getCode());
            } else {
                ps.setNull(1,
                        Types.VARCHAR);
            }
            if (entite.getAudit() != null
                    && entite.getAudit().getUserModification() != null) {
                ps.setString(2,
                        entite.getAudit().getUserCreation());
            } else {
                ps.setNull(2,
                        Types.VARCHAR);
            }

            ps.setString(3,
                    entite.getIdentifiant().getUUID());
            ps.setLong(4,
                    entite.getIdentifiant().getVersion());

            int row = ps.executeUpdate();

            if (row == 0) {
                throw new EntiteTropAnciennePersistenceException(
                        entite.toString());
            }
        }
    }

    @Override
    protected Unite readEntite(final ResultSet rs)
            throws SQLException {
        Identifiant identifiant = readIdentifiant(rs);
        Audit audit = readAudit(rs);
        String code = rs.getString(SQL.UNITES.ATTRIBUTS.CODE);

        Unite entite = UniteBase.builder()
                .identifiant(identifiant)
                .audit(audit)
                .code(code)
                .build();

        return entite;
    }
//CHECKSTYLE.ON: MagicNumber
}
