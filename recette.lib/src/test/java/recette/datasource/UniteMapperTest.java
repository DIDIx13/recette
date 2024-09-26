package recette.datasource;

import core.datasource.ContrainteNotNullPersistenceException;
import core.datasource.ContrainteUniquePersistenceException;
import core.datasource.EntiteInconnuePersistenceException;
import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.EntiteUtiliseePersistenceException;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.datasource.TransactionManager.Operation;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import recette.domain.DemoData;
import recette.domain.Unite;
import recette.domain.UniteBase;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public abstract class UniteMapperTest {

    protected TransactionManager transactionManager;
    protected DemoData demoData;
    protected Identifiant identifiantCS;
    protected Unite uniteCS;
    protected Unite nouvelleUniteRef;

    @Before
    public void setUp() {
        identifiantCS = IdentifiantBase.builder()
                .uuid(DemoData.UNITES.C_S.UUID)
                .build();
        uniteCS = UniteBase.builder()
                .identifiant(identifiantCS)
                .code(DemoData.UNITES.C_S.CODE)
                .build();
        nouvelleUniteRef = UniteBase.builder()
                .code("nouvelle unité " + Instant.now().toString())
                .build();
    }

    @Test
    public void testRetrieve_Identifiant() throws Exception {
        Unite entite = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(identifiantCS);
            }
        });

        Assert.assertNotNull(entite);
        Assert.assertTrue(entite.getIdentifiant().getVersion() > 0);
        Assert.assertNotNull(entite.getAudit());
        Assert.assertEquals(uniteCS, entite);
        Assert.assertEquals(uniteCS.getCode(), entite.getCode());
    }

    @Test
    public void testRetrieve_IdentifiantNull() throws Exception {
        Identifiant identifiant = null;
        Unite entite = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(identifiant);
            }
        });
        Assert.assertNull(entite);
    }

    @Test
    public void testRetrieve_Identifiant_Detacher() throws Exception {
        Unite entite1 = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(identifiantCS);
            }
        });
        Unite entite2 = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(identifiantCS);
            }
        });
        Assert.assertEquals(entite1, entite2);
        Assert.assertNotSame(entite1, entite2);
        entite1.setCode(entite1.getCode() + " update entite1");
        Assert.assertNotEquals(entite1.getCode(), entite2.getCode());
    }

    @Test
    public void testRetrieve_String() throws Exception {
        String regex = "^.*(c.s|g|brins).*$";
        Pattern pattern = Pattern.compile(regex);
        List<Unite> entites1 = (List<Unite>) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(regex);
            }
        });
        Assert.assertEquals(3, entites1.size());
        for (Unite i : entites1) {
            Assert.assertTrue(pattern.matcher(i.getCode()).find());
        }
    }

    @Test
    public void testRetrieve_StringNull() throws Exception {
        String regex = null;
        List<Unite> entites1 = (List<Unite>) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(regex);
            }
        });
        Assert.assertEquals(0, entites1.size());
    }

    @Test
    public void testRetrieve_String_Detacher() throws Exception {
        String regex = "^.*(c.s|g|brins).*$";
        List<Unite> entites1 = (List<Unite>) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(regex);
            }
        });
        List<Unite> entites2 = (List<Unite>) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(regex);
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
    public void testCreate() throws Exception {
        Unite nouvelleEntite = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().create(nouvelleUniteRef);
            }
        });

        Assert.assertNotNull(nouvelleEntite.getIdentifiant());
        Assert.assertEquals(Long.valueOf(1),
                nouvelleEntite.getIdentifiant().getVersion());
        Assert.assertEquals(nouvelleUniteRef.getCode(), nouvelleEntite.getCode());

        Assert.assertNotNull(nouvelleEntite.getAudit());
        Assert.assertNull(nouvelleEntite.getAudit().getUserModification());
        Assert.assertNull(nouvelleEntite.getAudit().getDateSuppression());
        Assert.assertTrue(Instant.now()
                .isAfter(nouvelleEntite.getAudit()
                        .getDateCreation()));

        Unite entite = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(nouvelleEntite.getIdentifiant());
            }
        });

        Assert.assertNotNull(entite);
        Assert.assertEquals(nouvelleEntite, entite);
        Assert.assertNotSame(nouvelleEntite, entite);
        Assert.assertEquals(nouvelleEntite.getCode(), entite.getCode());

    }

    @Test(expected = ContrainteUniquePersistenceException.class)
    public void testCreateCodeNotUnique() throws Exception {
        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().create(nouvelleUniteRef);
            }
        });
        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().create(nouvelleUniteRef);
            }
        });
    }

    @Test(expected = ContrainteNotNullPersistenceException.class)
    public void testCreateCodeNull() throws Exception {
        this.nouvelleUniteRef.setCode(null);
        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().create(nouvelleUniteRef);
            }
        });
    }

    @Test
    public void testUpdate() throws Exception {
        Unite entite = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                Unite nouvelleEntite = mm.getUniteMapper().create(nouvelleUniteRef);
                return mm.getUniteMapper().retrieve(nouvelleEntite.getIdentifiant());
            }
        });

        Assert.assertNotNull(entite.getIdentifiant());
        Assert.assertEquals(Long.valueOf(1),
                entite.getIdentifiant().getVersion());
        Assert.assertEquals(nouvelleUniteRef.getCode(), entite.getCode());

        Unite entiteMod = UniteBase.builder().unite(entite).build();
        entiteMod.setCode(entite.getCode() + " update");

        Unite entiteModifie = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getUniteMapper().update(entiteMod);
                return mm.getUniteMapper().retrieve(entiteMod.getIdentifiant());
            }
        });

        Assert.assertEquals(Long.valueOf(2),
                entiteModifie.getIdentifiant().getVersion());
        Assert.assertEquals(entiteMod, entiteModifie);
        Assert.assertEquals(entiteMod.getCode(), entiteModifie.getCode());

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
        Unite entite = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                Unite nouvelleEntite = mm.getUniteMapper().create(nouvelleUniteRef);
                return mm.getUniteMapper().retrieve(nouvelleEntite.getIdentifiant());
            }
        });

        Assert.assertNotNull(entite.getIdentifiant());
        Assert.assertEquals(Long.valueOf(1),
                entite.getIdentifiant().getVersion());

        Assert.assertEquals(nouvelleUniteRef.getCode(),
                entite.getCode());
        Unite entiteMod
                = UniteBase.builder()
                        .unite(entite)
                        .build();
        entiteMod.setCode(entite.getCode() + " update");

        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getUniteMapper()
                        .update(entiteMod);
                return null;
            }
        });

        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getUniteMapper()
                        .update(entiteMod);
                return null;
            }
        });
    }

    /**
     *
     * @throws Exception
     */
    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testUpdateEntiteInconnu() throws Exception {
        Unite entiteMod = UniteBase.builder()
                .unite(this.nouvelleUniteRef)
                .identifiant(IdentifiantBase.builder()
                        .build())
                .build();
        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getUniteMapper()
                        .update(entiteMod);
                return null;
            }
        });
    }

    @Test(expected = ContrainteNotNullPersistenceException.class)
    public void testUpdateCodeNull() throws Exception {
        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                Unite entite = mm.getUniteMapper().retrieve(identifiantCS);
                entite.setCode(null);
                mm.getUniteMapper()
                        .update(entite);
                return null;
            }
        });
    }

    @Test
    public void testDelete() throws Exception {
        Unite entite = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                Unite nouvelleEntite = mm.getUniteMapper().create(nouvelleUniteRef);
                return mm.getUniteMapper().retrieve(nouvelleEntite.getIdentifiant());
            }
        });

        Assert.assertNotNull(entite);
        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getUniteMapper().delete(entite);
                return null;
            }
        });

        Unite entiteNull = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper().retrieve(entite.getIdentifiant());
            }
        });

        Assert.assertNull(entiteNull);
    }

    @Test(expected = EntiteTropAnciennePersistenceException.class)
    public void testDeleteEntiteTropAncienne() throws Exception {
        Unite entite = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                Unite nouvelleEntite = mm.getUniteMapper().create(nouvelleUniteRef);
                return mm.getUniteMapper().retrieve(nouvelleEntite.getIdentifiant());
            }
        });

        Assert.assertNotNull(entite);

        entite.setCode("nouveau code " + Instant.now().toString());

        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getUniteMapper()
                        .update(entite);
                return null;
            }
        });

        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getUniteMapper()
                        .delete(entite);
                return null;
            }
        });
    }

    @Test(expected = EntiteUtiliseePersistenceException.class)
    public void testDeleteEntiteUtilisee() throws Exception {
        Unite entite = (Unite) transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                return mm.getUniteMapper()
                        .retrieve(identifiantCS);
            }
        });

        Assert.assertNotNull(entite);

        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getUniteMapper().delete(entite);
                return null;
            }
        });

    }

    @Test(expected = EntiteInconnuePersistenceException.class)
    public void testDeleteEntiteInconnu() throws Exception {
        Unite entite = UniteBase.builder()
                .unite(this.nouvelleUniteRef)
                .identifiant(IdentifiantBase.builder()
                        .build())
                .build();
        transactionManager.executeTransaction(
                new Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm)
                    throws PersistenceException {
                mm.getUniteMapper().delete(entite);
                return null;
            }
        });
    }

}
