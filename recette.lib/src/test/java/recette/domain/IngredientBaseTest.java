package recette.domain;

import core.domain.Audit;
import core.domain.AuditBase;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import java.time.Instant;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class IngredientBaseTest {

    private Identifiant identifiantRef;
    private Ingredient entiteRef;
    private String nomRef;
    private String detailRef;
    private static final Logger LOG = Logger.getLogger(IngredientBaseTest.class.getName());
    private Recette recetteRef;
    private Audit auditRef;

    public IngredientBaseTest() {
    }

    @Before
    public void setUp() {
        identifiantRef = IdentifiantBase.builder().build();
        auditRef = AuditBase.builder()
                .dateCreation(Instant.now())
                .userCreation("user creation")
                .dateModification(Instant.now().plusSeconds(60))
                .userModification("user modification")
                .dateSuppression(Instant.now().plusSeconds(3600))
                .build();
        
        nomRef = "nom de référence";
        detailRef = "détail de référence";
        
        recetteRef = RecetteBase.builder()
                .identifiant(IdentifiantBase.builder().build())
                .build();
        
        entiteRef = IngredientBase.builder()
                .identifiant(identifiantRef)
                .audit(auditRef)
                .nom(nomRef)
                .detail(detailRef)
                .recette(recetteRef)
                .build();

    }

    @Test
    public void testGet() {
        Assert.assertEquals(identifiantRef, entiteRef.getIdentifiant());
        Assert.assertEquals(nomRef, entiteRef.getNom());
        Assert.assertEquals(detailRef, entiteRef.getDetail());
        Assert.assertEquals(recetteRef, entiteRef.getRecette());
        testAudit(this.auditRef, this.entiteRef.getAudit());
        
    }

    private void testAudit(Audit ref, Audit audit) {
        Assert.assertEquals(ref, audit);
        Assert.assertEquals(ref.getUserCreation(), 
                audit.getUserCreation());
        Assert.assertEquals(ref.getUserModification(), 
                audit.getUserModification());
        Assert.assertEquals(ref.getUserSuppression(), 
                audit.getUserSuppression());
    }
    
    
    @Test
    public void testSet() {
        String nom = "nom";
        String detail = "detail";
        
        Recette recette =  RecetteBase.builder()
                .identifiant(IdentifiantBase.builder().build())
                .build();
        
        this.entiteRef.setNom(nom);
        this.entiteRef.setDetail(detail);
        this.entiteRef.setRecette(recette);
                

        Assert.assertEquals(identifiantRef, entiteRef.getIdentifiant());
        Assert.assertEquals(nom, entiteRef.getNom());
        Assert.assertEquals(detail, entiteRef.getDetail());
        Assert.assertEquals(recette, entiteRef.getRecette());
    }
    
   @Test
    public void testUpdate() {
        String nom = "nom";
        String detail = "detail";
        
        Recette recette =  RecetteBase.builder()
                .identifiant(IdentifiantBase.builder().build())
                .build();
        
        Ingredient entite = IngredientBase.builder()
                .nom(nom)
                .detail(detail)
                .recette(recette)
                .build();
        
        this.entiteRef.update(entite);

        Assert.assertEquals(identifiantRef, entiteRef.getIdentifiant());
        testAudit(this.auditRef, this.entiteRef.getAudit());
        Assert.assertEquals(nom, entiteRef.getNom());
        Assert.assertEquals(detail, entiteRef.getDetail());
        Assert.assertEquals(recette, entiteRef.getRecette());
        
    }    
    
 @Test
    public void testUpdateNull() {
        this.entiteRef.update(null);

        Assert.assertEquals(identifiantRef, entiteRef.getIdentifiant());
        Assert.assertEquals(nomRef, entiteRef.getNom());
        Assert.assertEquals(detailRef, entiteRef.getDetail());
        Assert.assertEquals(recetteRef, entiteRef.getRecette());
        
    }
        
    @Test
    public void testEquals() {
        Ingredient entite = IngredientBase.builder()
                .identifiant(this.entiteRef.getIdentifiant())
                .build();
        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef.hashCode(), entite.hashCode());
    }
    
    @Test
    public void testEqualsUniteSame() {
        Assert.assertSame(this.entiteRef, this.entiteRef);
        Assert.assertEquals(this.entiteRef, this.entiteRef);
        Assert.assertEquals(this.entiteRef.hashCode(), entiteRef.hashCode());
    }
    
    @Test
    public void testNotEquals() {
        Identifiant id = IdentifiantBase.builder().build();

        Ingredient entite = IngredientBase.builder()
                .identifiant(id)
                .build();
                
        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
    }
    
    @Test
    public void testEqualsEntiteNull() {
        Ingredient entite = null;

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
    }
    
    @Test
    public void testEqualsObject() {
        Object entite = new Object();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
        
    }
    
    @Test
    public void testToString() {
        LOG.info(this.entiteRef.toString());
    }
    
    @Test
    public void testClone() {
        Ingredient entite = IngredientBase.builder()
                .ingredient(entiteRef)
                .build();
        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef.hashCode(), entite.hashCode());

        Assert.assertEquals(this.entiteRef.getIdentifiant(), entite.getIdentifiant());
        Assert.assertEquals(this.entiteRef.getNom(), entite.getNom());
        Assert.assertEquals(this.entiteRef.getDetail(), entite.getDetail());
        Assert.assertEquals(this.entiteRef.getRecette(), entite.getRecette());
        testAudit(this.auditRef, this.entiteRef.getAudit());

    }

    @Test
    public void testCloneNull() {
        Ingredient entite = IngredientBase.builder()
                .ingredient(null)
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
        Assert.assertNull(entite.getIdentifiant());
        Assert.assertNull(entite.getNom());
        Assert.assertNull(entite.getDetail());
        Assert.assertNull(entite.getRecette());
    }
    

}
