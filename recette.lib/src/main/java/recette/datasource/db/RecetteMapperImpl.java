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
import java.util.ArrayList;
import java.util.List;
import recette.datasource.RecetteMapper;
import recette.domain.Composant;
import recette.domain.ComposantBase;
import recette.domain.Ingredient;
import recette.domain.Recette;
import recette.domain.RecetteBase;
import recette.domain.Unite;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class RecetteMapperImpl
        extends EntiteMapperImpl<Recette>
        implements RecetteMapper {
//CHECKSTYLE.OFF: MagicNumber

    public RecetteMapperImpl(final DbMapperManagerImpl mm) {
        super(mm,
                SQL.RECETTES.SELECT_BY_FILTRE,
                SQL.RECETTES.DELETE_BY_UUID);
    }

    @Override
    protected void createEntite(
            final Identifiant id,
            final Recette entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(SQL.RECETTES.INSERT)) {
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

            if (entite.getPreparation() != null) {
                ps.setString(4,
                        entite.getPreparation());
            } else {
                ps.setNull(4,
                        Types.VARCHAR);
            }

            if (entite.getNombrePersonnes() != null) {
                ps.setInt(5,
                        entite.getNombrePersonnes());
            } else {
                ps.setNull(5,
                        Types.INTEGER);
            }
            if (entite.getAudit() != null
                    && entite.getAudit().getUserCreation() != null) {
                ps.setString(6,
                        entite.getAudit().getUserCreation());
            } else {
                ps.setNull(6,
                        Types.VARCHAR);
            }

            ps.executeUpdate();

            if (entite.getComposants().size() > 0) {
                insertComposants(
                        id,
                        entite.getComposants());
            }
        }
    }

    @Override
    protected Recette retrieveEntite(
            final Identifiant id)
            throws SQLException, PersistenceException {
        Recette entite = null;
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(SQL.RECETTES.SELECT_BY_UUID)) {
            ps.setString(1, id.getUUID());
            ps.setString(1,
                    id.getUUID());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entite = readEntite(rs);
                if (entite != null) {
                    List<Composant> composants
                            = retrieveComposantByUuidRecette(
                                    entite.getIdentifiant());
                    for (Composant c : composants) {
                        entite.addComposant(c);
                    }
                }

            }

        }
        return entite;
    }

    @Override
    protected List<Recette> retrieveEntites(
            final String filtre)
            throws PersistenceException, SQLException {
        List<Recette> entites = new ArrayList<>();

        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(SQL.RECETTES.SELECT_BY_FILTRE)) {
            ps.setString(1,
                    filtre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Recette entite = readEntite(rs);
                if (entite != null) {
                    List<Composant> composants
                            = this.retrieveComposantByUuidRecette(
                                    entite.getIdentifiant());
                    for (Composant c : composants) {
                        entite.addComposant(c);

                    }
                    entites.add(entite);
                }
            }

        }

        return entites;

    }

    @Override
    protected void updateEntite(
            final Recette entite)
            throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(
                        SQL.RECETTES.UPDATE)) {
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

            if (entite.getPreparation() != null) {
                ps.setString(3,
                        entite.getPreparation());
            } else {
                ps.setNull(3,
                        Types.VARCHAR);
            }

            if (entite.getNombrePersonnes() != null) {
                ps.setInt(4,
                        entite.getNombrePersonnes());
            } else {
                ps.setNull(4,
                        Types.INTEGER);
            }

            if (entite.getAudit() != null
                    && entite.getAudit().getUserModification() != null) {
                ps.setString(5,
                        entite.getAudit().getUserCreation());
            } else {
                ps.setNull(5,
                        Types.VARCHAR);
            }

            ps.setString(6,
                    entite.getIdentifiant().getUUID());
            ps.setLong(7,
                    entite.getIdentifiant().getVersion());

            int row = ps.executeUpdate();

            if (row == 0) {
                throw new EntiteTropAnciennePersistenceException(
                        entite.toString());
            }

            if (entite.getComposants().size() > 0) {
                updateComposants(
                        entite.getIdentifiant(),
                        entite.getComposants());
            }
        }
    }

    @Override
    protected Recette readEntite(final ResultSet rs) throws SQLException {
        Identifiant identifiant = readIdentifiant(rs);
        Audit audit = readAudit(rs);

        String nom = rs.getString(
                SQL.RECETTES.ATTRIBUTS.NOM);
        String detail = rs.getString(
                SQL.RECETTES.ATTRIBUTS.DETAIL);
        String preparation = rs.getString(
                SQL.RECETTES.ATTRIBUTS.PREPARATION);
        int nombrePersonnes = rs.getInt(
                SQL.RECETTES.ATTRIBUTS.NOMBRE_PERSONNES);

        Recette entite = RecetteBase.builder()
                .identifiant(identifiant)
                .audit(audit)
                .nom(nom)
                .detail(detail)
                .preparation(preparation)
                .nombrePersonnes(nombrePersonnes)
                .build();

        return entite;

    }

    private List<Composant> retrieveComposantByUuidRecette(
            final Identifiant id)
            throws SQLException, PersistenceException {
        List<Composant> list = new ArrayList<>();
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(
                        SQL.COMPOSANTS.SELECT_BY_UUID_RECETTE)) {
            ps.setString(1,
                    id.getUUID());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Composant composant = readComposant(rs);
                list.add(composant);
            }

        }

        return list;

    }

    private Composant readComposant(final ResultSet rs)
            throws SQLException, PersistenceException {
        Integer numero = rs.getInt(
                SQL.COMPOSANTS.ATTRIBUTS.NUMERO);
        Double quantite = rs.getDouble(
                SQL.COMPOSANTS.ATTRIBUTS.QUANTITE);
        if (rs.wasNull()) {
            quantite = null;
        }
        String commentaire = rs.getString(
                SQL.COMPOSANTS.ATTRIBUTS.COMMENTAIRE);
        String ingredientUUID = rs.getString(
                SQL.COMPOSANTS.ATTRIBUTS.INGREDIENTS_UUID);
        String uniteUUID = rs.getString(
                SQL.COMPOSANTS.ATTRIBUTS.UNITES_UUID);

        Ingredient ingredient = null;
        if (ingredientUUID != null) {
            ingredient = this.getMapperManager().getIngredientMapper()
                    .retrieve(IdentifiantBase.builder()
                            .uuid(ingredientUUID)
                            .build());

        }

        Unite unite = null;
        if (ingredientUUID != null) {
            unite = this.getMapperManager().getUniteMapper()
                    .retrieve(IdentifiantBase.builder()
                            .uuid(uniteUUID)
                            .build());

        }

        Composant entite = ComposantBase.builder()
                .numero(numero)
                .quantite(quantite)
                .commentaire(commentaire)
                .ingredient(ingredient)
                .unite(unite)
                .build();

        return entite;
    }

    private void insertComposants(
            final Identifiant id,
            final List<Composant> composants) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(SQL.COMPOSANTS.INSERT)) {
            for (int i = 0; i < composants.size(); i += 1) {
                Composant c = composants.get(i);
                ps.setString(1,
                        id.getUUID());
                ps.setInt(2,
                        i);
                ps.setInt(3,
                        i);
                if (c.getQuantite() != null) {
                    ps.setDouble(4,
                            c.getQuantite());
                } else {
                    ps.setNull(4,
                            java.sql.Types.DOUBLE);
                }
                ps.setString(5,
                        c.getCommentaire());
                ps.setString(6,
                        c.getIngredient().getIdentifiant().getUUID());
                if (c.getUnite() != null) {
                    ps.setString(7,
                            c.getUnite().getIdentifiant().getUUID());
                } else {
                    ps.setNull(7,
                            java.sql.Types.VARCHAR);
                }
                ps.addBatch();
            }

            ps.executeBatch();

        }
    }

    private void updateComposants(
            final Identifiant identifiant,
            final List<Composant> composants) throws SQLException, PersistenceException {
        deleteComposants(identifiant);
        insertComposants(identifiant,
                composants);
    }

    private void deleteComposants(
            final Identifiant id) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(SQL.COMPOSANTS.DELETE_BY_UUID_RECETTES)) {
            ps.setString(1,
                    id.getUUID());

            ps.executeUpdate();

        }
    }
//CHECKSTYLE.ON: MagicNumber

}
