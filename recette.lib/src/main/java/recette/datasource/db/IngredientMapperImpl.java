package recette.datasource.db;

import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.PersistenceException;
import core.domain.Audit;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import recette.datasource.IngredientMapper;
import recette.domain.Ingredient;
import recette.domain.IngredientBase;
import recette.domain.RecetteBase;
import recette.domain.RecetteRef;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class IngredientMapperImpl
        extends EntiteMapperImpl<Ingredient>
        implements IngredientMapper {
//CHECKSTYLE.OFF: MagicNumber

    public IngredientMapperImpl(final DbMapperManagerImpl mm) {
        super(mm,
                SQL.INGREDIENTS.SELECT_BY_FILTRE,
                SQL.INGREDIENTS.DELETE_BY_UUID);
    }

    @Override
    protected void createEntite(
            final Identifiant id,
            final Ingredient entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(SQL.INGREDIENTS.INSERT)) {
            ps.setString(1,
                    id.getUUID());

            if (entite.getNom() != null) {
                ps.setString(2,
                        entite.getNom());
            } else {
                ps.setNull(2,
                        Types.VARCHAR);
            }
            if (entite.getDetail() != null) {
                ps.setString(3,
                        entite.getDetail());
            } else {
                ps.setNull(3,
                        Types.VARCHAR);
            }
            if (entite.getRecette() != null) {
                ps.setString(4,
                        entite.getRecette()
                                .getIdentifiant()
                                .getUUID());
            } else {
                ps.setNull(4,
                        Types.VARCHAR);
            }
            if (entite.getAudit() != null
                    && entite.getAudit().getUserCreation() != null) {
                ps.setString(5,
                        entite.getAudit().getUserCreation());
            } else {
                ps.setNull(5,
                        Types.VARCHAR);
            }

            ps.executeUpdate();
        }
    }

    @Override
    protected Ingredient retrieveEntite(
            final Identifiant id) throws SQLException, PersistenceException {

        Ingredient entite = null;
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(SQL.INGREDIENTS.SELECT_BY_UUID)) {
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
            final Ingredient entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(
                        SQL.INGREDIENTS.UPDATE)) {
            if (entite.getNom() != null) {
                ps.setString(1,
                        entite.getNom());
            } else {
                ps.setNull(1,
                        Types.VARCHAR);
            }
            if (entite.getDetail() != null) {
                ps.setString(2,
                        entite.getDetail());
            } else {
                ps.setNull(2,
                        Types.VARCHAR);
            }
            if (entite.getRecette() != null) {
                ps.setString(3,
                        entite.getRecette()
                                .getIdentifiant()
                                .getUUID());
            } else {
                ps.setNull(3,
                        Types.VARCHAR);
            }
            if (entite.getAudit() != null
                    && entite.getAudit().getUserModification() != null) {
                ps.setString(4,
                        entite.getAudit().getUserCreation());
            } else {
                ps.setNull(4,
                        Types.VARCHAR);
            }

            ps.setString(5,
                    entite.getIdentifiant().getUUID());
            ps.setLong(6,
                    entite.getIdentifiant().getVersion());

            int row = ps.executeUpdate();

            if (row == 0) {
                throw new EntiteTropAnciennePersistenceException(
                        entite.toString());
            }

        }

    }

    @Override
    protected Ingredient readEntite(
            final ResultSet rs)
            throws SQLException {
        Identifiant identifiant
                = readIdentifiant(rs);
        Audit audit = readAudit(rs);
        String nom
                = rs.getString(SQL.INGREDIENTS.ATTRIBUTS.NOM);
        String detail
                = rs.getString(SQL.INGREDIENTS.ATTRIBUTS.DETAIL);
        String recetteUUID
                = rs.getString(SQL.INGREDIENTS.ATTRIBUTS.RECETTES_UUID);

        IngredientBase.Builder builder
                = IngredientBase.builder()
                        .identifiant(identifiant)
                        .audit(audit)
                        .nom(nom)
                        .detail(detail);

        if (recetteUUID != null) {
            builder.recette(new RecetteRef(RecetteBase.builder()
                    .identifiant(IdentifiantBase.builder()
                            .uuid(recetteUUID)
                            .build())
                    .build()));
        }

        Ingredient entite = builder.build();

        return entite;

    }
//CHECKSTYLE.ON: MagicNumber
}
