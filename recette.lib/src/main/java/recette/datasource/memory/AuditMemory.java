package recette.datasource.memory;

import core.domain.Audit;
import core.domain.AuditBase;
import java.time.Instant;

/**
 *
 * @author dominique huguenin (dominique.huguenin at rpn.ch)
 */
public class AuditMemory extends AuditBase implements Audit {

    public AuditMemory(final Audit audit) {
        super(AuditBase.builder()
                .audit(audit));

    }

    public void updateNow(final String user) {
        this.setDateModification(Instant.now());
        this.setUserModification(user);
        this.setDateSuppression(null);
        this.setUserSuppression(null);
    }

    public void deleteNow(final String user) {
        this.setDateSuppression(Instant.now());
        this.setUserSuppression(null);
    }

}
