package recette.datasource;

import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import core.datasource.ContrainteNotNullPersistenceException;
import core.datasource.ContrainteUniquePersistenceException;
import core.datasource.EntiteInconnuePersistenceException;
import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import java.time.Instant;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recette.domain.ComposantBase;
import recette.domain.DemoData;
import recette.domain.IngredientBase;
import recette.domain.Recette;
import recette.domain.RecetteBase;
import recette.domain.UniteBase;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public abstract class RecetteMapperTest {

    protected DemoData demoData;
    protected TransactionManager transactionManager;
    private Identifiant identifiantAubergineRef;
    private Recette recetteAubergineRef;
    private Recette nouvelleEntiteRef;

    @Before
    public void setUp() {
        identifiantAubergineRef = IdentifiantBase.builder()
                .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.UUID)
                .build();
        this.recetteAubergineRef = RecetteBase.builder()
                .identifiant(identifiantAubergineRef)
                .nom(DemoData.RECETTES.AUBERGINES_AU_FOUR.NOM)
                .detail(DemoData.RECETTES.AUBERGINES_AU_FOUR.DETAIL)
                .preparation(DemoData.RECETTES.AUBERGINES_AU_FOUR.PREPARATION)
                .nombrePersonnes(DemoData.RECETTES.AUBERGINES_AU_FOUR.NOMBRE_PERSONNES)
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_1.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_1.INGREDIENT_UUID)
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_1.QUANTITE)
                        .commentaire(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_1.COMMENTAIRE)
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_2.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_2.INGREDIENT_UUID)
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_2.QUANTITE)
                        .unite(demoData.getUnites().get(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_2.UNITE_UUID) //c.s
                                .build()))
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_3.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_3.INGREDIENT_UUID) //sel
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_3.QUANTITE)
                        .unite(demoData.getUnites().get(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_3.UNITE_UUID) //c.c
                                .build()))
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_4.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_4.INGREDIENT_UUID) //poivre
                                        .build()))
                        .commentaire(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_4.COMMENTAIRE)
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_5.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_5.INGREDIENT_UUID) //citron bio
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_5.QUANTITE)
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_6.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_6.INGREDIENT_UUID) //huilde
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_6.QUANTITE)
                        .unite(demoData.getUnites().get(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_6.UNITE_UUID) //c.s
                                .build()))
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_7.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_7.INGREDIENT_UUID) //sel
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_7.QUANTITE)
                        .unite(demoData.getUnites().get(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_7.UNITE_UUID) //c.c
                                .build()))
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_8.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_8.INGREDIENT_UUID) //poivre
                                        .build()))
                        .commentaire("un peu")
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_9.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_9.INGREDIENT_UUID) //tomate cerise
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_9.QUANTITE)
                        .unite(demoData.getUnites().get(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_9.UNITE_UUID) //g
                                .build()))
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_10.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_10.INGREDIENT_UUID) //feta
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_10.QUANTITE)
                        .unite(demoData.getUnites().get(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_10.UNITE_UUID) //g
                                .build()))
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_11.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_11.INGREDIENT_UUID) //thym
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_11.QUANTITE)
                        .unite(demoData.getUnites().get(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_11.UNITE_UUID) //brin
                                .build()))
                        .build())
                .composant(ComposantBase.builder()
                        .numero(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_12.ORDRE)
                        .ingredient(demoData.getIngredients()
                                .get(IdentifiantBase.builder()
                                        .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_12.INGREDIENT_UUID) //mûre
                                        .build()))
                        .quantite(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_12.QUANTITE)
                        .unite(demoData.getUnites().get(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.AUBERGINES_AU_FOUR.C_12.UNITE_UUID) //g
                                .build()))
                        .build())
                .build();

        nouvelleEntiteRef = RecetteBase.builder()
                .nom("nouvelle recette " + Instant.now().toString())
                .detail("description du nouvelle recette")
                .preparation("préparation de la nouvelle recette")
                .nombrePersonnes(100)
                .composant(ComposantBase.builder()
                        .ingredient(IngredientBase.builder()
                                .identifiant(IdentifiantBase.builder()
                                        .uuid("DEMO0000-0000-0000-0001-000000000001")//aubergine
                                        .build())
                                .build())
                        .quantite(10.0)
                        .build())
                .composant(ComposantBase.builder()
                        .ingredient(IngredientBase.builder()
                                .identifiant(IdentifiantBase.builder()
                                        .uuid("DEMO0000-0000-0000-0001-000000000007")//Feta
                                        .build())
                                .build())
                        .quantite(250.0)
                        .unite(UniteBase.builder()
                                .identifiant(IdentifiantBase.builder()
                                        .uuid("DEMO0000-0000-0000-0002-000000000003")//g
                                        .build())
                                .build())
                        .build())
                .composant(ComposantBase.builder()
                        .ingredient(IngredientBase.builder()
                                .identifiant(IdentifiantBase.builder()
                                        .uuid("DEMO0000-0000-0000-0001-000000000004") //poivre
                                        .build())
                                .build())
                        .commentaire("un peu")
                        .build())
                .build();

    }

    @Test
    public void testRetrieve_Identifiant() throws Exception {
        Recette entite = (Recette) transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getRecetteMapper()
                        .retrieve(identifiantAubergineRef);
            }
        });

        Assert.assertNotNull(entite);
        Assert.assertTrue(entite.getIdentifiant().getVersion() > 0);
        Assert.assertNotNull(entite.getAudit());

        Assert.assertEquals(recetteAubergineRef, entite);
        Assert.assertEquals(recetteAubergineRef.getNom(), entite.getNom());
        Assert.assertEquals(recetteAubergineRef.getDetail(),
                entite.getDetail());
        Assert.assertEquals(recetteAubergineRef.getPreparation(),
                entite.getPreparation());
        Assert.assertEquals(recetteAubergineRef.getNombrePersonnes(),
                entite.getNombrePersonnes());

        Assert.assertEquals(recetteAubergineRef.getComposants().size(),
                entite.getComposants().size());

        for (int i = 0; i < recetteAubergineRef.getComposants().size(); i += 1) {
            Assert.assertEquals(recetteAubergineRef.getComposants().get(i).getIngredient(),
                    entite.getComposants().get(i).getIngredient());
            Assert.assertNotNull(entite.getComposants().get(i).getIngredient().getNom());

            Assert.assertEquals(recetteAubergineRef.getComposants().get(i).getQuantite(),
                    entite.getComposants().get(i).getQuantite());
            Assert.assertEquals(recetteAubergineRef.getComposants().get(i).getUnite(),
                    entite.getComposants().get(i).getUnite());
            Assert.assertEquals(recetteAubergineRef.getComposants().get(i).getCommentaire(),
                    entite.getComposants().get(i).getCommentaire());
        }
    }

    @Test
    public void testRetrieve_Identifiant_Detacher() throws Exception {
        Recette entite1 = (Recette) transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getRecetteMapper()
                        .retrieve(identifiantAubergineRef);
            }
        });

        Recette entite2 = (Recette) transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getRecetteMapper()
                        .retrieve(identifiantAubergineRef);
            }
        });

        Assert.assertEquals(entite1, entite2);
        Assert.assertNotSame(entite1, entite2);

        Assert.assertEquals(entite1.getComposants().size(), entite2.getComposants().size());
        for (int i = 0; i < entite1.getComposants().size(); i += 1) {
            Assert.assertNotSame(entite1.getComposants().get(i),
                    entite2.getComposants().get(i));

            Assert.assertEquals(entite1.getComposants().get(i).getIngredient(),
                    entite2.getComposants().get(i).getIngredient());
            Assert.assertNotSame(entite1.getComposants().get(i).getIngredient(),
                    entite2.getComposants().get(i).getIngredient());
            Assert.assertEquals(entite1.getComposants().get(i).getUnite(),
                    entite2.getComposants().get(i).getUnite());
            if (entite1.getComposants().get(i).getUnite() != null) {
                Assert.assertNotSame(entite1.getComposants().get(i).getUnite(),
                        entite2.getComposants().get(i).getUnite());
            }

        }
    }

    @Test
    public void testRetrieve_IdentifiantNull() throws Exception {
        Identifiant identifiant = null;
        Recette entite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(identifiant);
                    }
                });
        Assert.assertNull(entite);
    }

    @Test
    public void testRetrieve_String() throws Exception {
        String regex = "^.*(Poires|Poivrons|tomates).*$";

        List<Recette> entites
                = (List<Recette>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(regex);
                    }
                });

        Assert.assertEquals(3, entites.size());
        for (Recette i : entites) {
            Assert.assertTrue(i.getNom().matches(regex));
        }
    }

    @Test
    public void testRetrieve_String_Aubergine() throws Exception {
        String regex = "^.*(Aubergine).*$";

        List<Recette> entites
                = (List<Recette>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(regex);
                    }
                });
        Assert.assertEquals(1, entites.size());
        for (Recette i : entites) {
            Assert.assertTrue(i.getNom().matches(regex));
            Assert.assertEquals(12, i.getComposants().size());
        }
    }

    @Test
    public void testRetrieve_String_Detacher() throws Exception {
        String regex = "^.*(Poires|Poivrons|tomates).*$";

        List<Recette> entites1
                = (List<Recette>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(regex);
                    }
                });

        List<Recette> entites2
                = (List<Recette>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(regex);
                    }
                });

        Assert.assertEquals(entites1, entites2);
        Assert.assertNotSame(entites1, entites2);
        Assert.assertEquals(entites1.size(), entites2.size());
        for (int j = 0; j < entites1.size(); j += 1) {
            Recette entite1 = entites1.get(j);
            Recette entite2 = entites2.get(j);

            Assert.assertEquals(entite1, entite2);
            Assert.assertNotSame(entite1, entite2);

            Assert.assertEquals(entite1.getComposants().size(),
                    entite2.getComposants().size());
            for (int i = 0; i < entite1.getComposants().size(); i += 1) {
                Assert.assertNotSame(entite1.getComposants().get(i),
                        entite2.getComposants().get(i));

                Assert.assertEquals(entite1.getComposants().get(i).getIngredient(),
                        entite2.getComposants().get(i).getIngredient());
                Assert.assertNotSame(entite1.getComposants().get(i).getIngredient(),
                        entite2.getComposants().get(i).getIngredient());
                Assert.assertEquals(entite1.getComposants().get(i).getUnite(),
                        entite2.getComposants().get(i).getUnite());
                if (entite1.getComposants().get(i).getUnite() != null) {
                    Assert.assertNotSame(entite1.getComposants().get(i).getUnite(),
                            entite2.getComposants().get(i).getUnite());
                }

            }

        }
    }

    @Test
    public void testRetrieve_StringNull() throws Exception {
        String regex = null;

        List<Recette> entites1
                = (List<Recette>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(regex);
                    }
                });
        Assert.assertEquals(0, entites1.size());
    }

    @Test
    public void testCreate() throws Exception {
        Recette nouvelleEntite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .create(nouvelleEntiteRef);
                    }
                });

        Assert.assertNotNull(nouvelleEntite.getIdentifiant());
        Assert.assertEquals(Long.valueOf(1),
                nouvelleEntite.getIdentifiant().getVersion());
        Assert.assertNotNull(nouvelleEntite.getAudit());
        Assert.assertNull(nouvelleEntite.getAudit().getUserModification());
        Assert.assertNull(nouvelleEntite.getAudit().getDateSuppression());
        Assert.assertTrue(Instant.now()
                .isAfter(nouvelleEntite.getAudit()
                        .getDateCreation()));

        Assert.assertEquals(nouvelleEntiteRef.getNom(),
                nouvelleEntite.getNom());
        Assert.assertEquals(nouvelleEntiteRef.getDetail(),
                nouvelleEntite.getDetail());
        Assert.assertEquals(nouvelleEntiteRef.getPreparation(),
                nouvelleEntite.getPreparation());
        Assert.assertEquals(nouvelleEntiteRef.getNombrePersonnes(),
                nouvelleEntite.getNombrePersonnes());

        Assert.assertEquals(nouvelleEntiteRef.getComposants().size(),
                nouvelleEntite.getComposants().size());

        for (int i = 0; i < nouvelleEntiteRef.getComposants().size(); i += 1) {
            Assert.assertNotSame(nouvelleEntiteRef.getComposants().get(i),
                    nouvelleEntite.getComposants().get(i));

            Assert.assertEquals(nouvelleEntiteRef.getComposants().get(i).getIngredient(),
                    nouvelleEntite.getComposants().get(i).getIngredient());
            Assert.assertNotNull(nouvelleEntite.getComposants().get(i).getIngredient().getNom());

            Assert.assertEquals(nouvelleEntiteRef.getComposants().get(i).getQuantite(),
                    nouvelleEntite.getComposants().get(i).getQuantite());
            Assert.assertEquals(nouvelleEntiteRef.getComposants().get(i).getUnite(),
                    nouvelleEntite.getComposants().get(i).getUnite());
            Assert.assertEquals(nouvelleEntiteRef.getComposants().get(i).getCommentaire(),
                    nouvelleEntite.getComposants().get(i).getCommentaire());
        }

        Recette entite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());
                    }
                });

        Assert.assertEquals(nouvelleEntite, entite);
        Assert.assertEquals(nouvelleEntite.getDetail(),
                entite.getDetail());
        Assert.assertEquals(nouvelleEntite.getPreparation(),
                entite.getPreparation());
        Assert.assertEquals(nouvelleEntite.getNombrePersonnes(),
                entite.getNombrePersonnes());

        Assert.assertEquals(nouvelleEntite.getComposants().size(),
                entite.getComposants().size());

        for (int i = 0; i < nouvelleEntite.getComposants().size(); i += 1) {
            Assert.assertEquals(nouvelleEntite.getComposants().get(i).getIngredient(),
                    entite.getComposants().get(i).getIngredient());

            Assert.assertNotNull(entite.getComposants().get(i).getIngredient().getNom());

            Assert.assertEquals(nouvelleEntite.getComposants().get(i).getQuantite(),
                    entite.getComposants().get(i).getQuantite());
            Assert.assertEquals(nouvelleEntite.getComposants().get(i).getUnite(),
                    entite.getComposants().get(i).getUnite());
            if (entite.getComposants().get(i).getUnite() != null) {
                Assert.assertNotNull(entite.getComposants().get(i).getUnite().getCode());
            }
            Assert.assertEquals(nouvelleEntite.getComposants().get(i).getCommentaire(),
                    entite.getComposants().get(i).getCommentaire());
        }

    }

    @Test(expected = ContrainteUniquePersistenceException.class)
    public void testCreateNomNotUnique() throws Exception {
        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getRecetteMapper()
                        .create(recetteAubergineRef);
            }
        });
    }

    @Test(expected = ContrainteNotNullPersistenceException.class)
    public void testCreateNomNull() throws Exception {
        this.nouvelleEntiteRef.setNom(null);

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getRecetteMapper()
                        .create(nouvelleEntiteRef);
            }
        });
    }

    @Test
    public void testUpdate() throws Exception {

        Recette entite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        Recette nouvelleEntite
                                = mm.getRecetteMapper()
                                        .create(nouvelleEntiteRef);
                        return mm.getRecetteMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());
                    }
                });

        Assert.assertEquals(Long.valueOf(1),
                entite.getIdentifiant().getVersion());

        Recette entiteModifie = RecetteBase.builder()
                .identifiant(entite.getIdentifiant())
                .nom(entite.getNom() + " update")
                .detail(entite.getDetail() + " update")
                .preparation(entite.getPreparation() + " update")
                .nombrePersonnes(entite.getNombrePersonnes() * 10)
                .composant(entite.getComposants().get(2))
                .composant(ComposantBase.builder()
                        .ingredient(IngredientBase.builder()
                                .identifiant(IdentifiantBase.builder()
                                        .uuid(DemoData.INGREDIENTS.THYM.UUID) //thym
                                        .build())
                                .build())
                        .quantite(1.0)
                        .unite(UniteBase.builder()
                                .identifiant(IdentifiantBase.builder()
                                        .uuid(DemoData.UNITES.BRINS.UUID) //brin
                                        .build())
                                .build())
                        .commentaire("commentaire update")
                        .build())
                .build();

        Recette entiteMod
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        mm.getRecetteMapper()
                                .update(entiteModifie);
                        return mm.getRecetteMapper()
                                .retrieve(entiteModifie.getIdentifiant());
                    }
                });

        Assert.assertEquals(Long.valueOf(2L),
                entiteMod.getIdentifiant()
                        .getVersion());

        Assert.assertEquals(entiteModifie, entiteMod);
        Assert.assertEquals(entiteModifie.getDetail(),
                entiteMod.getDetail());
        Assert.assertEquals(entiteModifie.getPreparation(),
                entiteMod.getPreparation());
        Assert.assertEquals(entiteModifie.getNombrePersonnes(),
                entiteMod.getNombrePersonnes());

        Assert.assertEquals(entiteModifie.getComposants().size(),
                entiteMod.getComposants().size());

        for (int i = 0; i < entiteModifie.getComposants().size(); i += 1) {
            Assert.assertEquals(entiteModifie.getComposants().get(i).getIngredient(),
                    entiteMod.getComposants().get(i).getIngredient());

            Assert.assertNotNull(entiteMod.getComposants().get(i).getIngredient().getNom());

            Assert.assertEquals(entiteModifie.getComposants().get(i).getQuantite(),
                    entiteMod.getComposants().get(i).getQuantite());
            Assert.assertEquals(entiteModifie.getComposants().get(i).getUnite(),
                    entiteMod.getComposants().get(i).getUnite());
            if (entiteMod.getComposants().get(i).getUnite() != null) {
                Assert.assertNotNull(entiteMod.getComposants().get(i).getUnite().getCode());
            }
            Assert.assertEquals(entiteModifie.getComposants().get(i).getCommentaire(),
                    entiteMod.getComposants().get(i).getCommentaire());
        }

        Assert.assertNotNull(entiteMod.getAudit());
        Assert.assertNull(entiteMod.getAudit().getDateSuppression());
        Assert.assertTrue(Instant.now()
                .isAfter(entiteMod.getAudit()
                        .getDateCreation()));
        Assert.assertTrue(Instant.now()
                .isAfter(entiteMod.getAudit()
                        .getDateModification()));

    }

    @Test(expected = EntiteTropAnciennePersistenceException.class)
    public void testUpdateEntiteTropAncienne() throws Exception {
        Recette entite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        Recette nouvelleEntite
                                = mm.getRecetteMapper()
                                        .create(nouvelleEntiteRef);
                        return mm.getRecetteMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());
                    }
                });

        Assert.assertEquals(Long.valueOf(1),
                entite.getIdentifiant().getVersion());

        Recette entiteModifie = RecetteBase.builder()
                .identifiant(entite.getIdentifiant())
                .nom(entite.getNom() + " update")
                .detail(entite.getDetail() + " update")
                .preparation(entite.getPreparation() + " update")
                .nombrePersonnes(entite.getNombrePersonnes() * 10)
                .composant(entite.getComposants().get(2))
                .composant(ComposantBase.builder()
                        .ingredient(IngredientBase.builder()
                                .identifiant(IdentifiantBase.builder()
                                        .uuid(DemoData.INGREDIENTS.THYM.UUID) //thym
                                        .build())
                                .build())
                        .quantite(1.0)
                        .unite(UniteBase.builder()
                                .identifiant(IdentifiantBase.builder()
                                        .uuid(DemoData.UNITES.BRINS.UUID) //brin
                                        .build())
                                .build())
                        .commentaire("commentaire update")
                        .build())
                .build();

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .update(entiteModifie);
                return null;
            }
        });

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .update(entite);
                return null;
            }
        });
    }

    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testUpdateEntiteInconnu() throws Exception {
        Recette entiteMod = RecetteBase.builder()
                .recette(this.nouvelleEntiteRef)
                .identifiant(IdentifiantBase.builder().build())
                .build();

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .update(entiteMod);
                return null;
            }
        });
    }

    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testUpdateIngredientInconnu() throws Exception {
        Recette entite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(identifiantAubergineRef);
                    }
                });

        entite.addComposant(ComposantBase.builder()
                .ingredient(IngredientBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build());

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .update(entite);
                return null;
            }
        });

    }

    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testUpdateUniteInconnu() throws Exception {
        Recette entite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(identifiantAubergineRef);
                    }
                });

        entite.addComposant(ComposantBase.builder()
                .ingredient(IngredientBase.builder()
                        .identifiant(IdentifiantBase.builder()
                                .uuid(DemoData.INGREDIENTS.SEL.UUID)
                                .build())
                        .build())
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build());

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .update(entite);
                return null;
            }
        });
    }

    @Test(expected = ContrainteNotNullPersistenceException.class)
    public void testUpdateNomNull() throws Exception {
        Recette entite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(identifiantAubergineRef);
                    }
                });

        entite.setNom(null);

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .update(entite);
                return null;
            }
        });
    }

    @Test
    public void testDelete() throws Exception {
        Recette entite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        Recette nouvelleEntite
                                = mm.getRecetteMapper()
                                        .create(nouvelleEntiteRef);
                        return mm.getRecetteMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());
                    }
                });

        Assert.assertNotNull(entite);

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .delete(entite);
                return null;
            }
        });

        Recette entiteDel
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getRecetteMapper()
                                .retrieve(entite.getIdentifiant());
                    }
                });
        Assert.assertNull(entiteDel);

    }

    @Test(expected = EntiteTropAnciennePersistenceException.class)
    public void testDeleteEntiteTropAncienne() throws Exception {
        Recette entite
                = (Recette) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        Recette nouvelleEntite
                                = mm.getRecetteMapper()
                                        .create(nouvelleEntiteRef);
                        return mm.getRecetteMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());
                    }
                });

        Assert.assertNotNull(entite);

        entite.setDetail("nouveau détail");

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .update(entite);
                return null;
            }
        });

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .delete(entite);
                return null;
            }
        });

    }

    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testDeleteEntiteInconnu() throws Exception {
        Recette entite = RecetteBase.builder()
                .recette(this.nouvelleEntiteRef)
                .identifiant(IdentifiantBase.builder().build())
                .build();

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getRecetteMapper()
                        .delete(entite);
                return null;
            }
        });
    }

}
