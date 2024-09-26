package core.domain;

import java.util.UUID;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class IdentifiantBaseTest {

    private String uuidRef;
    private Identifiant identifiantRef;
    private static final Logger LOG = Logger.getLogger(IdentifiantBaseTest.class.getName());
    private Long versionRef;
    
    public IdentifiantBaseTest() {
    }
    
    @Before
    public void setUp() {
        uuidRef = UUID.randomUUID().toString();
        versionRef = 12L;
        identifiantRef = IdentifiantBase.builder()
                .uuid(uuidRef)
                .version(versionRef)
                .build();
    }
    
    @Test
    public void testGet() {
        Assert.assertEquals(uuidRef, identifiantRef.getUUID());
        Assert.assertEquals(versionRef, identifiantRef.getVersion());
    }
    
    @Test
    public void testEquals() {
        Identifiant id2 = IdentifiantBase.builder()
                .uuid(uuidRef)
                .build();

        Assert.assertTrue(identifiantRef != id2);
        Assert.assertEquals(identifiantRef, id2);
        Assert.assertEquals(identifiantRef.hashCode(), id2.hashCode());

        Assert.assertTrue(identifiantRef.equals(id2));
        Assert.assertTrue(id2.equals(identifiantRef));
    }
    
    @Test
    public void testEqualsSame() {
        Assert.assertTrue(identifiantRef == identifiantRef);
        Assert.assertEquals(identifiantRef, identifiantRef);
        Assert.assertEquals(identifiantRef.hashCode(), identifiantRef.hashCode());

        Assert.assertTrue(identifiantRef.equals(identifiantRef));
        Assert.assertTrue(identifiantRef.equals(identifiantRef));
    }
    
    
    @Test
    public void testNotEquals() {
        Identifiant id2 = IdentifiantBase.builder()
                .build();

        Assert.assertTrue(identifiantRef != id2);
        Assert.assertNotEquals(identifiantRef, id2);
        Assert.assertNotEquals(identifiantRef.hashCode(), id2.hashCode());

        Assert.assertFalse(identifiantRef.equals(id2));
        Assert.assertFalse(id2.equals(identifiantRef));
    }
    
    
    
    @Test
    public void testEqualsIdentifiantNull() {
        Identifiant id2 = null;

        Assert.assertTrue(identifiantRef != id2);
        Assert.assertNotEquals(identifiantRef, id2);

        Assert.assertFalse(identifiantRef.equals(id2));
    }
    
    @Test
    public void testEqualsObject() {
        Object id2 = new Object();

        Assert.assertTrue(identifiantRef != id2);
        Assert.assertNotEquals(identifiantRef, id2);
        Assert.assertNotEquals(identifiantRef.hashCode(), id2.hashCode());

        Assert.assertFalse(identifiantRef.equals(id2));
        Assert.assertFalse(id2.equals(identifiantRef));
    }
    
    @Test
    public void callToString() {
        LOG.info(this.identifiantRef.toString());
    }
    
    @Test
    public void testClone() {
        Identifiant id = IdentifiantBase.builder()
                .identifiant(identifiantRef)
                .build();
        
        Assert.assertEquals(this.identifiantRef, id);
        Assert.assertNotSame(this.identifiantRef,id);
        
        Assert.assertEquals(identifiantRef.getUUID(),id.getUUID());
        Assert.assertEquals(identifiantRef.getVersion(),id.getVersion());
        
    }
    
    @Test(expected = NullPointerException.class )
    public void testCloneNullEntite() {
        Identifiant id = IdentifiantBase.builder()
                .identifiant(null)
                .build();
    }
    
}
