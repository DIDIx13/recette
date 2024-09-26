package core.domain;

import java.time.Instant;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dominique huguenin (dominique.huguenin at rpn.ch)
 */
public class AuditBaseTest {

    private Audit auditRef;
    private Instant dateCreationRef;
    private String userCreationRef;
    private Instant dateModificationRef;
    private String userModificationRef;
    private Instant dateSuppressionRef;
    private String userSuppressionRef;
    private static final Logger LOG
            = Logger.getLogger(AuditBaseTest.class.getName());

    /**
     *
     */
    public AuditBaseTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
        dateCreationRef = Instant.MIN;
        userCreationRef = "user creation";
        dateModificationRef = Instant.now();
        userModificationRef = "user modification";
        dateSuppressionRef = Instant.MAX;
        userSuppressionRef = "user suppression";

        auditRef = AuditBase.builder()
                .dateCreation(this.dateCreationRef)
                .userCreation(this.userCreationRef)
                .dateModification(this.dateModificationRef)
                .userModification(this.userModificationRef)
                .dateSuppression(this.dateSuppressionRef)
                .userSuppression(this.userSuppressionRef)
                .build();
    }

    /**
     *
     */
    @Test
    public void testGet() {
        Assert.assertEquals(this.dateCreationRef,
                auditRef.getDateCreation());
        Assert.assertEquals(this.userCreationRef,
                auditRef.getUserCreation());
        Assert.assertEquals(this.dateModificationRef,
                auditRef.getDateModification());
        Assert.assertEquals(this.userModificationRef,
                auditRef.getUserModification());
        Assert.assertEquals(this.dateSuppressionRef,
                auditRef.getDateSuppression());
        Assert.assertEquals(this.userSuppressionRef,
                auditRef.getUserSuppression());

    }

    /**
     *
     */
    @Test
    public void testEquals() {
        Audit audit = AuditBase.builder()
                .dateCreation(this.dateCreationRef)
                .dateModification(this.dateModificationRef)
                .dateSuppression(this.dateSuppressionRef)
                .build();

        Assert.assertEquals(this.auditRef,
                audit);
        Assert.assertNotSame(this.auditRef,
                audit);
    }

    /**
     *
     */
    @Test
    public void testToString() {
        LOG.info(auditRef.toString());
    }

}
