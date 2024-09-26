package recette.datasource.memory;

import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import core.datasource.ContrainteNotNullPersistenceException;
import core.datasource.ContrainteUniquePersistenceException;
import core.datasource.EntiteInconnuePersistenceException;
import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.EntiteUtiliseePersistenceException;
import core.datasource.PersistenceException;
import core.domain.AuditBase;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import recette.domain.Composant;
import recette.domain.Ingredient;
import recette.domain.IngredientBase;
import recette.domain.Recette;
import recette.domain.RecetteRef;
import recette.datasource.IngredientMapper;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class IngredientMapperImpl implements IngredientMapper {

    private final MemoryMapperManagerImpl mapperManager;

    IngredientMapperImpl(final MemoryMapperManagerImpl mm) {
        this.mapperManager = mm;
    }

    @Override
    public Ingredient create(final Ingredient entite) throws PersistenceException {
        if (entite == null) {
            return null;
        }

        Recette recette = null;
        if (entite.getRecette() != null) {
            recette = this.mapperManager.getRecetteMapper()
                    .retrieve(entite.getRecette().getIdentifiant());

            checkRecetteInconnue(recette, entite.getRecette());
        }

        AuditBase.Builder auditBuilder = AuditBase.builder()
                .dateCreation(Instant.now());
        if (entite.getAudit() != null) {
            auditBuilder.userCreation(entite.getAudit().getUserCreation());
        }

        Ingredient nouvelleEntite = IngredientBase.builder()
                .ingredient(entite)
                .identifiant(IdentifiantBase.builder()
                        .build())
                .recette(recette)
                .identifiant(new IdentifiantMemory(
                        IdentifiantBase.builder()
                                .version(1L)
                                .build()))
                .audit(new AuditMemory(auditBuilder.build()))
                .build();

        checkContainteNomNotNull(nouvelleEntite);
        checkContrainteNomUnique(nouvelleEntite);

        this.mapperManager.getData().getIngredients()
                .put(nouvelleEntite.getIdentifiant(), nouvelleEntite);

        return this.retrieve(nouvelleEntite.getIdentifiant());
    }

    @Override
    public Ingredient retrieve(final Identifiant id) throws PersistenceException {
        if (id == null) {
            return null;
        }

        Ingredient entite = mapperManager.getData().getIngredients().get(id);
        if (entite != null) {
            entite = IngredientBase.builder()
                    .ingredient(entite)
                    .identifiant(IdentifiantBase.builder()
                            .identifiant(entite.getIdentifiant())
                            .build())
                    .build();

            if (entite.getRecette() != null) {
                entite.setRecette(new RecetteRef(entite.getRecette()));
            }
        }
        return entite;
    }

    @Override
    public List<Ingredient> retrieve(final String regex)
            throws PersistenceException {
        if (regex == null) {
            return new ArrayList<>();
        }

        Pattern pattern = Pattern.compile(regex);

        List<Ingredient> entites = new ArrayList<>();

        for (Ingredient e : mapperManager.getData()
                .getIngredients().values()) {
            Matcher matcher = pattern.matcher(e.getNom());
            if (matcher.find()) {
                Ingredient entite = IngredientBase.builder()
                        .ingredient(e)
                        .identifiant(IdentifiantBase.builder()
                                .identifiant(e.getIdentifiant())
                                .build())
                        .build();

                if (entite.getRecette() != null) {
                    entite.setRecette(new RecetteRef(entite.getRecette()));
                }

                entites.add(entite);
            }
        }

        return entites;
    }

    @Override
    public void update(final Ingredient entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }

        Recette recette = null;
        if (entite.getRecette() != null) {
            recette = this.mapperManager.getRecetteMapper()
                    .retrieve(entite.getRecette().getIdentifiant());

            checkRecetteInconnue(recette, entite.getRecette());
        }

        Ingredient e = this.mapperManager.getData()
                .getIngredients().get(entite.getIdentifiant());

        checkEntiteInconnue(e, entite);
        checkContainteNomNotNull(entite);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }

        e.update(entite);
        e.setRecette(recette);

        if (e.getAudit() instanceof AuditMemory) {
            String user = null;
            if (entite.getAudit() != null) {
                user = entite.getAudit().getUserModification();
            }
            ((AuditMemory) e.getAudit()).updateNow(user);
        }

        if (e.getIdentifiant() instanceof IdentifiantMemory) {
            ((IdentifiantMemory) e.getIdentifiant()).incrementVersion();
        }

    }

    @Override
    public void delete(final Ingredient entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }

        Ingredient e = this.mapperManager.getData()
                .getIngredients().get(entite.getIdentifiant());

        checkEntiteInconnue(e, entite);
        checkContrainteEntiteUtilisee(e);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }

        this.mapperManager.getData()
                .getIngredients().remove(e.getIdentifiant());
    }

    private void checkContrainteEntiteUtilisee(final Ingredient e)
            throws EntiteUtiliseePersistenceException {
        //Vérifie si l'unité est utilisée dans un composant de recette
        for (Recette recette : this.mapperManager.getData()
                .getRecettes().values()) {
            for (Composant c : recette.getComposants()) {
                if (e.equals(c.getIngredient())) {
                    throw new EntiteUtiliseePersistenceException(
                            String.format("Erreur: L'ingrédient "
                                    + "est utilisé! (%s)",
                                    e.toString()));
                }
            }
        }

    }

    private void checkEntiteInconnue(final Ingredient e,
            final Ingredient entite)
            throws EntiteInconnuePersistenceException {
        if (e == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: l'entité est inconnue! (%s)",
                            entite.toString()));
        }
    }

    private void checkRecetteInconnue(final Recette entite, final Recette recette)
            throws EntiteInconnuePersistenceException {
        if (entite == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: La recette avec"
                            + " l'uuid %s est inconnue!",
                            recette.getIdentifiant().getUUID()));
        }
    }

    private void checkContainteNomNotNull(final Ingredient entite)
            throws ContrainteNotNullPersistenceException {
        if (entite.getNom() == null) {
            throw new ContrainteNotNullPersistenceException(
                    String.format("Erreur: le nom est null! (%s)",
                            entite));
        }
    }

    private void checkContrainteNomUnique(final Ingredient entite)
            throws ContrainteUniquePersistenceException {
        for (Ingredient e : this.mapperManager.getData()
                .getIngredients().values()) {
            if (!e.equals(entite) && e.getNom().equals(entite.getNom())) {
                throw new ContrainteUniquePersistenceException(
                        String.format(
                                "Erreur: Le nom de l'ingrédient"
                                + "n'est pas unique! (%s)",
                                e.toString())
                );
            }

        }
    }

}
