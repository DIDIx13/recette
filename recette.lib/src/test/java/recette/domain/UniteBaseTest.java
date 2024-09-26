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
public class UniteBaseTest {

    private Identifiant identifiantRef;
    private Unite entiteRef;
    private String codeRef;
    private static final Logger LOG = Logger.getLogger(UniteBaseTest.class.getName());
    private Audit auditRef;

    public UniteBaseTest() {
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
        codeRef = "code référence";
        entiteRef = UniteBase.builder()
                .identifiant(identifiantRef)
                .audit(auditRef)
                .code(codeRef)
                .build();
    }

    @Test
    public void testGet() {
        Assert.assertEquals(identifiantRef, entiteRef.getIdentifiant());
        Assert.assertEquals(codeRef, entiteRef.getCode());
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
        String code = "c.s";
        this.entiteRef.setCode(code);

        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        Assert.assertEquals(code, this.entiteRef.getCode());
    }
    

    @Test
    public void testUpdate() {
        String code = "c.s";
        Unite entite = UniteBase.builder()
                .code(code)
                .build();
        this.entiteRef.update(entite);

        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        testAudit(this.auditRef, this.entiteRef.getAudit());
        Assert.assertEquals(code, this.entiteRef.getCode());
    }

    @Test
    public void testUpdateNull() {
        this.entiteRef.update(null);

        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        Assert.assertEquals(this.codeRef, this.entiteRef.getCode());
    }
    
    @Test
    public void testEquals() {
        Unite entite = UniteBase.builder()
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

        Unite entite = UniteBase.builder()
                .identifiant(id)
                .build();
        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
    }
    
    @Test
    public void testEqualsUniteNull() {
        Unite entite = null;

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
        Unite entite = UniteBase.builder()
                .unite(entiteRef)
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef.hashCode(), entite.hashCode());
        Assert.assertEquals(this.entiteRef.getIdentifiant(), entite.getIdentifiant());
        Assert.assertEquals(this.entiteRef.getCode(), entite.getCode());
        testAudit(this.auditRef, this.entiteRef.getAudit());
        
    }

    @Test
    public void testCloneNull() {
        Unite entite = UniteBase.builder()
                .unite(null)
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
        Assert.assertNull(entite.getIdentifiant());
        Assert.assertNull(entite.getCode());
    }

    
}
