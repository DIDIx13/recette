package recette.datasource;

import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import core.datasource.ContrainteNotNullPersistenceException;
import core.datasource.ContrainteUniquePersistenceException;
import core.datasource.EntiteInconnuePersistenceException;
import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.EntiteUtiliseePersistenceException;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import java.time.Instant;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recette.domain.DemoData;
import recette.domain.Ingredient;
import recette.domain.IngredientBase;
import recette.domain.Recette;
import recette.domain.RecetteBase;
import recette.domain.RecetteRef;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public abstract class IngredientMapperTest {

    protected DemoData demoData;
    protected TransactionManager transactionManager;
    private Identifiant identifiantAubergine;
    private Ingredient ingredientAubergine;
    private Ingredient nouvelleIngredientRef2;
    private Ingredient nouvelleIngredientRef1;

    @Before
    public void setUp() {
        identifiantAubergine = IdentifiantBase.builder()
                .uuid(DemoData.INGREDIENTS.AUBERGINE.UUID)
                .build();
        ingredientAubergine = IngredientBase.builder()
                .identifiant(identifiantAubergine)
                .nom(DemoData.INGREDIENTS.AUBERGINE.NOM)
                .detail(DemoData.INGREDIENTS.AUBERGINE.DETAIL)
                .build();

        nouvelleIngredientRef2 = IngredientBase.builder()
                .nom("nouvelle ingredient " + Instant.now().toString())
                .detail("description du nouvelle ingrédient")
                .build();

        nouvelleIngredientRef1 = IngredientBase.builder()
                .nom("nouvelle ingredient " + Instant.now().toString())
                .detail("description du nouvelle ingrédient")
                .recette(RecetteBase.builder()
                        .identifiant(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.POIVRONS_AU_FOUR.UUID)
                                .build())
                        .build())
                .build();

    }

    @Test
    public void testRetrieve_Identifiant() throws Exception {
        Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(identifiantAubergine);
                    }
                });

        Assert.assertNotNull(entite);
        Assert.assertTrue(entite.getIdentifiant().getVersion() > 0);
        Assert.assertNotNull(entite.getAudit());

        Assert.assertEquals(ingredientAubergine, entite);
        Assert.assertEquals(ingredientAubergine.getNom(),
                entite.getNom());
        Assert.assertEquals(ingredientAubergine.getDetail(),
                entite.getDetail());
    }

    @Test
    public void testRetrieve_SauceTomate() throws Exception {
        Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(IdentifiantBase.builder()
                                        .uuid(DemoData.INGREDIENTS.SAUCE_TOMATES.UUID)
                                        .build());
                    }
                });
        Assert.assertNotNull(entite);
        Assert.assertTrue(entite.getIdentifiant().getVersion() > 0);
        Assert.assertNotNull(entite.getAudit());

        Assert.assertEquals(DemoData.INGREDIENTS.SAUCE_TOMATES.UUID,
                entite.getIdentifiant().getUUID());
        Assert.assertEquals(DemoData.INGREDIENTS.SAUCE_TOMATES.NOM,
                entite.getNom());
        Assert.assertEquals(DemoData.INGREDIENTS.SAUCE_TOMATES.DETAIL,
                entite.getDetail());
        Assert.assertEquals(DemoData.RECETTES.SAUCE_TOMATES.UUID,
                entite.getRecette().getIdentifiant().getUUID());

        Recette recette = (Recette) transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getRecetteMapper()
                        .retrieve(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.SAUCE_TOMATES.UUID)
                                .build());
            }
        });
        Assert.assertNotSame(recette, entite.getRecette());

        Assert.assertTrue("Expect RecetteRef",
                entite.getRecette() instanceof RecetteRef);

        Assert.assertTrue("Expect RecetteRef",
                entite.getRecette() instanceof RecetteRef);

    }

    @Test
    public void testRetrieve_IdentifiantNull() throws Exception {
        Identifiant identifiant = null;
        Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(identifiant);
                    }
                });

        Assert.assertNull(entite);
    }

    @Test
    public void testRetrieve_Identifiant_Detacher() throws Exception {
        Ingredient entite1
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(identifiantAubergine);
                    }
                });
        Ingredient entite2
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(identifiantAubergine);
                    }
                });

        Assert.assertEquals(entite1, entite2);
        Assert.assertNotSame(entite1, entite2);
        entite1.setNom(entite1.getNom() + " update entite1");
        Assert.assertNotEquals(entite1.getNom(), entite2.getNom());
    }

    @Test
    public void testRetrieve_String() throws Exception {
        String regex = "^.*(aubergine|tomate|thym).*$";
        List<Ingredient> entites
                = (List<Ingredient>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(regex);
                    }
                });

        Assert.assertEquals(4, entites.size());
        for (Ingredient i : entites) {
            Assert.assertTrue(i.getNom().matches(regex));
        }
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testRetrieve_String_RecetteRef() throws Exception {
        String regex = "^.*(aubergine|tomate|thym).*$";
        List<Ingredient> entites
                = (List<Ingredient>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(regex);
                    }
                });

        Assert.assertEquals(4, entites.size());
        for (Ingredient i : entites) {
            Assert.assertTrue(i.getNom().matches(regex));
            if (i.getRecette() != null) {
                Assert.assertTrue("Expect RecetteRef",
                        i.getRecette() instanceof RecetteRef);
            }

        }
    }

    @Test
    public void testRetrieve_String_Detacher() throws Exception {
        String regex = "^.*(aubergine|tomate|thym).*$";
        List<Ingredient> entites1
                = (List<Ingredient>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(regex);
                    }
                });

        List<Ingredient> entites2
                = (List<Ingredient>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(regex);
                    }
                });

        Assert.assertEquals(entites1, entites2);
        Assert.assertNotSame(entites1, entites2);
        Assert.assertEquals(entites1.size(), entites2.size());
        for (int i = 0; i < entites1.size(); i += 1) {
            Assert.assertEquals(entites1.get(i), entites2.get(i));
            Assert.assertNotSame(entites1.get(i), entites2.get(i));
        }
    }

    @Test
    public void testRetrieve_StringNull() throws Exception {
        String regex = null;
        List<Ingredient> entites1
                = (List<Ingredient>) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(regex);
                    }
                });

        Assert.assertEquals(0, entites1.size());
    }

    @Test
    public void testCreate() throws Exception {
        Ingredient nouvelleEntite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .create(nouvelleIngredientRef1);
                    }
                });

        Assert.assertNotNull(nouvelleEntite.getIdentifiant());
        Assert.assertEquals(Long.valueOf(1),
                nouvelleEntite.getIdentifiant().getVersion());
        Assert.assertEquals(nouvelleIngredientRef1.getNom(),
                nouvelleEntite.getNom());
        Assert.assertEquals(nouvelleIngredientRef1.getDetail(),
                nouvelleEntite.getDetail());

        Assert.assertNotNull(nouvelleEntite.getAudit());
        Assert.assertNull(nouvelleEntite.getAudit().getUserModification());
        Assert.assertNull(nouvelleEntite.getAudit().getDateSuppression());
        Assert.assertTrue(Instant.now()
                .isAfter(nouvelleEntite.getAudit()
                        .getDateCreation()));

        Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());
                    }
                });

        Assert.assertNotNull(entite);
        Assert.assertEquals(nouvelleEntite, entite);
        Assert.assertNotSame(nouvelleEntite, entite);
        Assert.assertEquals(nouvelleEntite.getNom(), entite.getNom());
        Assert.assertEquals(nouvelleEntite.getDetail(), entite.getDetail());
    }

    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testCreateRecetteInconnue() throws Exception {
        nouvelleIngredientRef1.setRecette(
                RecetteBase.builder()
                        .identifiant(IdentifiantBase
                                .builder()
                                .build())
                        .build());
        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {

                mm.getIngredientMapper()
                        .create(nouvelleIngredientRef1);

                return null;

            }
        });

    }

    @Test(expected = ContrainteUniquePersistenceException.class)
    public void testCreateNomNotUnique() throws Exception {
        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getIngredientMapper()
                        .create(ingredientAubergine);
            }
        });
    }

    @Test(expected = ContrainteNotNullPersistenceException.class)
    public void testCreateNomNull() throws Exception {

        nouvelleIngredientRef1.setNom(null);

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getIngredientMapper()
                        .create(nouvelleIngredientRef1);
            }
        });

    }

    @Test
    public void testUpdate() throws Exception {

        Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        Ingredient nouvelleEntite
                                = mm.getIngredientMapper()
                                        .create(nouvelleIngredientRef2);
                        return mm.getIngredientMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());
                    }
                });

        Assert.assertNotNull(entite.getIdentifiant());
        Assert.assertEquals(Long.valueOf(1),
                entite.getIdentifiant().getVersion());
        Assert.assertEquals(nouvelleIngredientRef2.getNom(),
                entite.getNom());
        Assert.assertEquals(nouvelleIngredientRef2.getDetail(),
                entite.getDetail());

        Ingredient entiteMod
                = IngredientBase.builder()
                        .ingredient(entite)
                        .build();
        entiteMod.setNom(entiteMod.getNom() + " update" + Instant.now().toString());
        entiteMod.setDetail(entiteMod.getDetail() + " update" + Instant.now().toString());
        entiteMod.setRecette(
                RecetteBase.builder()
                        .identifiant(IdentifiantBase.builder()
                                .uuid(DemoData.RECETTES.POIRES_AUX_AMANDES.UUID)
                                .build())
                        .build());

        Ingredient entiteModifie
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        mm.getIngredientMapper()
                                .update(entiteMod);
                        return mm.getIngredientMapper()
                                .retrieve(entiteMod
                                        .getIdentifiant());
                    }
                });

        Assert.assertEquals(entiteMod, entiteModifie);
        Assert.assertEquals(Long.valueOf(2),
                entiteModifie.getIdentifiant()
                        .getVersion());

        Assert.assertEquals(entiteMod.getNom(),
                entiteModifie.getNom());
        Assert.assertEquals(entiteMod.getDetail(),
                entiteModifie.getDetail());

        Assert.assertNotNull(entiteModifie.getAudit());
        Assert.assertNull(entiteModifie.getAudit().getDateSuppression());
        Assert.assertTrue(Instant.now()
                .isAfter(entiteModifie.getAudit()
                        .getDateCreation()));
        Assert.assertTrue(Instant.now()
                .isAfter(entiteModifie.getAudit()
                        .getDateModification()));

    }

    @Test(expected = EntiteTropAnciennePersistenceException.class)
    public void testUpdateEntiteTropAncienne() throws Exception {
        Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        Ingredient nouvelleEntite
                                = mm.getIngredientMapper()
                                        .create(nouvelleIngredientRef2);
                        return mm.getIngredientMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());
                    }
                });

        Assert.assertNotNull(entite.getIdentifiant());
        Assert.assertEquals(Long.valueOf(1),
                entite.getIdentifiant().getVersion());

        Assert.assertEquals(nouvelleIngredientRef2.getNom(),
                entite.getNom());
        Assert.assertEquals(nouvelleIngredientRef2.getDetail(),
                entite.getDetail());
        Ingredient entiteMod
                = IngredientBase.builder()
                        .ingredient(entite)
                        .build();
        entiteMod.setNom(entiteMod.getNom() + " update");
        entiteMod.setDetail(entiteMod.getDetail() + " update");
        entiteMod.setRecette(null);

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getIngredientMapper()
                        .update(entiteMod);
                return null;
            }
        });

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getIngredientMapper()
                        .update(entite);
                return null;
            }
        });
    }

    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testUpdateEntiteInconnu() throws Exception {
        Ingredient entiteMod
                = IngredientBase.builder()
                        .ingredient(this.nouvelleIngredientRef2)
                        .identifiant(IdentifiantBase.builder()
                                .build())
                        .build();

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getIngredientMapper()
                        .update(entiteMod);
                return null;
            }
        });
    }

    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testUpdateRecetteInconnu() throws Exception {

        Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        Ingredient nouvelleEntite
                                = mm.getIngredientMapper()
                                        .create(nouvelleIngredientRef2);
                        return mm.getIngredientMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());
                    }
                });

        Ingredient entiteMod
                = IngredientBase.builder()
                        .ingredient(entite)
                        .recette(
                                RecetteBase.builder()
                                        .identifiant(IdentifiantBase
                                                .builder()
                                                .build())
                                        .build())
                        .build();

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getIngredientMapper()
                        .update(entiteMod);
                return null;
            }
        });
    }

    @Test(expected = ContrainteNotNullPersistenceException.class)
    public void testUpdateNomNull() throws Exception {
        Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(identifiantAubergine);
                    }
                });

        entite.setNom(null);

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getIngredientMapper()
                        .update(entite);
                return null;
            }
        });
    }

    @Test
    public void testDelete() throws Exception {
        final Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        Ingredient nouvelleEntite
                                = mm.getIngredientMapper()
                                        .create(nouvelleIngredientRef2);
                        return mm.getIngredientMapper()
                                .retrieve(nouvelleEntite.getIdentifiant());

                    }
                });
        Assert.assertNotNull(entite);

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getIngredientMapper()
                        .delete(entite);
                return null;
            }
        });

        Ingredient entiteNull
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(entite.getIdentifiant());
                    }
                });

        Assert.assertNull(entiteNull);
    }

    @Test(expected = EntiteTropAnciennePersistenceException.class)
    public void testDeleteEntiteTropAncienne() throws Exception {
        final Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        Ingredient nouvelleEntite
                                = mm.getIngredientMapper()
                                        .create(nouvelleIngredientRef2);
                        return mm.getIngredientMapper()
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
                mm.getIngredientMapper()
                        .update(entite);
                return null;
            }
        });

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getIngredientMapper()
                        .delete(entite);
                return null;
            }
        });
    }

    @Test(expected = EntiteUtiliseePersistenceException.class)
    public void testDeleteEntiteUtilisee() throws Exception {
        Ingredient entite
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(MapperManager mm)
                            throws PersistenceException {
                        return mm.getIngredientMapper()
                                .retrieve(identifiantAubergine);
                    }
                });

        Assert.assertNotNull(entite);

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getIngredientMapper()
                        .delete(entite);
                return null;
            }
        });

    }

    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testDeleteEntiteInconnu() throws Exception {
        Ingredient entite
                = IngredientBase.builder()
                        .ingredient(this.nouvelleIngredientRef2)
                        .identifiant(IdentifiantBase.builder()
                                .build())
                        .build();

        transactionManager.executeTransaction(
                new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getIngredientMapper()
                        .delete(entite);
                return null;
            }
        });
    }

}
