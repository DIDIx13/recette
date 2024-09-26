package recette.datasource.memory;

import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import core.datasource.ContrainteNotNullPersistenceException;
import core.datasource.ContrainteUniquePersistenceException;
import core.datasource.EntiteInconnuePersistenceException;
import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.PersistenceException;
import core.domain.AuditBase;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import recette.domain.Composant;
import recette.domain.ComposantBase;
import recette.domain.Ingredient;
import recette.domain.Recette;
import recette.domain.RecetteBase;
import recette.domain.Unite;
import recette.datasource.RecetteMapper;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class RecetteMapperImpl implements RecetteMapper {

    private final MemoryMapperManagerImpl mapperManager;

    RecetteMapperImpl(final MemoryMapperManagerImpl mm) {
        this.mapperManager = mm;
    }

    @Override
    public Recette create(final Recette entite) throws PersistenceException {
        if (entite == null) {
            return null;
        }

        AuditBase.Builder auditBuilder = AuditBase.builder()
                .dateCreation(Instant.now());
        if (entite.getAudit() != null) {
            auditBuilder.userCreation(entite.getAudit().getUserCreation());
        }

        RecetteBase.Builder builder = RecetteBase.builder()
                .identifiant(new IdentifiantMemory(
                        IdentifiantBase.builder()
                                .version(1L)
                                .build()))
                .audit(new AuditMemory(auditBuilder.build()))
                .nom(entite.getNom())
                .detail(entite.getDetail())
                .preparation(entite.getPreparation())
                .nombrePersonnes(entite.getNombrePersonnes());

        int numero = 0;
        for (Composant c : entite.getComposants()) {
            Ingredient ingredient = this.mapperManager.getIngredientMapper()
                    .retrieve(c.getIngredient().getIdentifiant());
            checkIngredientInconnu(ingredient, c);

            Unite unite = null;
            if (c.getUnite() != null) {
                unite = this.mapperManager.getUniteMapper()
                        .retrieve(c.getUnite().getIdentifiant());

                checkUniteInconnue(unite, c);
            }

            numero += 1;
            Composant composant = ComposantBase.builder()
                    .composant(c)
                    .numero(numero)
                    .ingredient(ingredient)
                    .unite(unite)
                    .build();

            builder.composant(composant);
        }

        Recette nouvelleEntite = builder.build();

        this.checkContainteNomNotNull(nouvelleEntite);
        this.checkContrainteNomUnique(nouvelleEntite);

        this.mapperManager.getData().getRecettes()
                .put(nouvelleEntite.getIdentifiant(), nouvelleEntite);

        return this.retrieve(nouvelleEntite.getIdentifiant());

    }

    @Override
    public Recette retrieve(final Identifiant id)
            throws PersistenceException {
        if (id == null) {
            return null;
        }
        Recette entite = null;

        entite = this.mapperManager.getData().getRecettes().get(id);
        if (entite != null) {
            entite = deepClone(entite);
        }

        return entite;

    }

    @Override
    public List<Recette> retrieve(final String regex)
            throws PersistenceException {
        if (regex == null) {
            return new ArrayList<>();
        }

        Pattern pattern = Pattern.compile(regex);

        List<Recette> entites = new ArrayList<>();

        for (Recette i : mapperManager.getData()
                .getRecettes().values()) {
            Matcher matcher = pattern.matcher(i.getNom());
            if (matcher.find()) {
                entites.add(deepClone(i));
            }
        }

        return entites;
    }

    @Override
    public void update(final Recette entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }

        Recette e = this.mapperManager.getData()
                .getRecettes().get(entite.getIdentifiant());

        checkEntiteInconnue(e, entite);

        RecetteBase.Builder builder = RecetteBase.builder()
                .identifiant(entite.getIdentifiant())
                .nom(entite.getNom())
                .detail(entite.getDetail())
                .preparation(entite.getPreparation())
                .nombrePersonnes(entite.getNombrePersonnes());

        int numero = 0;
        for (Composant c : entite.getComposants()) {
            Ingredient ingredient = this.mapperManager.getIngredientMapper()
                    .retrieve(c.getIngredient().getIdentifiant());
            checkIngredientInconnu(ingredient, c);

            Unite unite = null;
            if (c.getUnite() != null) {
                unite = this.mapperManager.getUniteMapper()
                        .retrieve(c.getUnite().getIdentifiant());

                checkUniteInconnue(unite, c);
            }

            numero += 1;
            Composant composant = ComposantBase.builder()
                    .composant(c)
                    .numero(numero)
                    .ingredient(ingredient)
                    .unite(unite)
                    .build();

            builder.composant(composant);

        }

        Recette entiteModifie = builder
                .build();

        this.checkContainteNomNotNull(entiteModifie);
        this.checkContrainteNomUnique(entiteModifie);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }

        e.update(entiteModifie);

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
    public void delete(final Recette entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }

        Recette e = this.mapperManager.getData()
                .getRecettes().get(entite.getIdentifiant());

        checkEntiteInconnue(e, entite);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }

        this.mapperManager.getData()
                .getRecettes().remove(entite.getIdentifiant());
    }

    private Recette deepClone(final Recette entite)
            throws PersistenceException {
        RecetteBase.Builder builder = RecetteBase.builder()
                .identifiant(IdentifiantBase.builder()
                        .identifiant(entite.getIdentifiant())
                        .build())
                .audit(AuditBase.builder()
                        .audit(entite.getAudit())
                        .build())
                .nom(entite.getNom())
                .detail(entite.getDetail())
                .preparation(entite.getPreparation())
                .nombrePersonnes(entite.getNombrePersonnes());

        int numero = 0;
        for (Composant c : entite.getComposants()) {
            Ingredient ingredient = this.mapperManager.getIngredientMapper()
                    .retrieve(c.getIngredient().getIdentifiant());
            checkIngredientInconnu(ingredient, c);

            Unite unite = null;
            if (c.getUnite() != null) {
                unite = this.mapperManager.getUniteMapper()
                        .retrieve(c.getUnite().getIdentifiant());

                checkUniteInconnue(unite, c);
            }

            numero = numero + 1;
            Composant composant = ComposantBase.builder()
                    .composant(c)
                    .numero(numero)
                    .ingredient(ingredient)
                    .unite(unite)
                    .build();

            builder.composant(composant);
        }

        return builder.build();
    }

    private void checkEntiteInconnue(final Recette e, final Recette entite)
            throws EntiteInconnuePersistenceException {
        if (e == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: l'entité est inconnue! (%s)",
                            entite.toString()));
        }
    }

    private void checkUniteInconnue(final Unite unite, final Composant c)
            throws EntiteInconnuePersistenceException {
        if (unite == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: L'unité avec"
                            + " l'uuid %s est inconnue!",
                            c.getUnite().getIdentifiant().getUUID()));
        }
    }

    private void checkIngredientInconnu(final Ingredient ingredient,
            final Composant c)
            throws EntiteInconnuePersistenceException {
        if (ingredient == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: L'ingrédient avec "
                            + "l'uuid %s est inconnue!",
                            c.getIngredient().getIdentifiant().getUUID()));
        }
    }

    private void checkContainteNomNotNull(final Recette entite)
            throws ContrainteNotNullPersistenceException {
        if (entite.getNom() == null) {
            throw new ContrainteNotNullPersistenceException(
                    String.format("Erreur: le nom est null! (%s)",
                            entite));
        }
    }

    private void checkContrainteNomUnique(final Recette entite)
            throws ContrainteUniquePersistenceException {
        for (Recette e : this.mapperManager.getData()
                .getRecettes().values()) {
            if (!e.equals(entite) && e.getNom().equals(entite.getNom())) {
                throw new ContrainteUniquePersistenceException(
                        String.format(
                                "Erreur: Le nom n'est pas unique! (%s)",
                                e.toString())
                );
            }

        }
    }
}
