package recette.datasource.memory;

import core.datasource.ContrainteUniquePersistenceException;
import core.datasource.ContrainteNotNullPersistenceException;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
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
import recette.domain.Recette;
import recette.domain.Unite;
import recette.domain.UniteBase;
import recette.datasource.UniteMapper;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class UniteMapperImpl implements UniteMapper {

    private final MemoryMapperManagerImpl mapperManager;

    UniteMapperImpl(final MemoryMapperManagerImpl mm) {
        this.mapperManager = mm;
    }

    @Override
    public Unite create(final Unite entite) throws PersistenceException {
        if (entite == null) {
            return null;
        }

        AuditBase.Builder auditBuilder = AuditBase.builder()
                .dateCreation(Instant.now());
        if (entite.getAudit() != null) {
            auditBuilder.userCreation(entite.getAudit().getUserCreation());
        }

        Unite nouvelleEntite = UniteBase.builder()
                .unite(entite)
                .identifiant(new IdentifiantMemory(
                        IdentifiantBase.builder()
                                .version(1L)
                                .build()))
                .audit(new AuditMemory(auditBuilder.build()))
                .build();

        checkContainteCodeNotNull(nouvelleEntite);
        checkContrainteCodeUnique(nouvelleEntite);

        this.mapperManager.getData().getUnites()
                .put(nouvelleEntite.getIdentifiant(), nouvelleEntite);

        return this.retrieve(nouvelleEntite.getIdentifiant());

    }

    @Override
    public Unite retrieve(final Identifiant id) throws PersistenceException {
        if (id == null) {
            return null;
        }

        Unite entite = mapperManager.getData().getUnites().get(id);
        if (entite != null) {
            entite = UniteBase.builder()
                    .unite(entite)
                    .identifiant(IdentifiantBase.builder()
                            .identifiant(entite.getIdentifiant())
                            .build())
                    .build();
        }
        return entite;
    }

    @Override
    public List<Unite> retrieve(final String regex)
            throws PersistenceException {
        if (regex == null) {
            return new ArrayList<>();
        }

        Pattern pattern = Pattern.compile(regex);

        List<Unite> entites = new ArrayList<>();

        for (Unite e : mapperManager.getData()
                .getUnites().values()) {
            Matcher matcher = pattern.matcher(e.getCode());
            if (matcher.find()) {
                entites.add(UniteBase.builder()
                        .unite(e)
                        .identifiant(IdentifiantBase.builder()
                                .identifiant(e.getIdentifiant())
                                .build())
                        .build());
            }
        }

        return entites;
    }

    @Override
    public void update(final Unite entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }

        Unite e = this.mapperManager.getData()
                .getUnites().get(entite.getIdentifiant());

        checkEntiteInconnue(e, entite);
        checkContainteCodeNotNull(entite);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }

        e.update(entite);

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
    public void delete(final Unite entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }
        Unite e = this.mapperManager.getData()
                .getUnites().get(entite.getIdentifiant());

        checkEntiteInconnue(e, entite);
        checkContrainteEntiteUtilisee(e);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }

        this.mapperManager.getData()
                .getUnites().remove(e.getIdentifiant());

    }

    private void checkContrainteEntiteUtilisee(final Unite e)
            throws EntiteUtiliseePersistenceException {
        //Vérifie si l'unité est utilisée dans un composant de recette
        for (Recette recette : this.mapperManager.getData()
                .getRecettes().values()) {
            for (Composant c : recette.getComposants()) {
                if (e.equals(c.getUnite())) {
                    throw new EntiteUtiliseePersistenceException(
                            String.format("Erreur: L'unité est utilisée! (%s)",
                                    e.toString()));
                }
            }
        }
    }

    private void checkEntiteInconnue(final Unite e, final Unite entite)
            throws EntiteInconnuePersistenceException {
        if (e == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: l'entité est inconnue! (%s)",
                            entite.toString()));
        }
    }

    private void checkContrainteCodeUnique(final Unite entite)
            throws ContrainteUniquePersistenceException {
        for (Unite e : this.mapperManager.getData().getUnites().values()) {
            if (!e.equals(entite) && e.getCode().equals(entite.getCode())) {
                throw new ContrainteUniquePersistenceException(
                        String.format(
                                "Erreur: Le code de l'unité "
                                + "n'est pas unique! (%s)",
                                e.toString())
                );
            }

        }
    }

    private void checkContainteCodeNotNull(final Unite entite)
            throws ContrainteNotNullPersistenceException {
        if (entite.getCode() == null) {
            throw new ContrainteNotNullPersistenceException(
                    String.format("Erreur: le code est null! (%s)",
                            entite));
        }
    }

}
