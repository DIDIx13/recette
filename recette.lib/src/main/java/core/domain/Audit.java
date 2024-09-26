package core.domain;

import java.time.Instant;

/**
 *
 * @author dominique huguenin (dominique.huguenin@rpn.ch)
 */
public interface Audit {

    Instant getDateCreation();

    String getUserCreation();

    Instant getDateModification();

    String getUserModification();

    Instant getDateSuppression();

    String getUserSuppression();

}
