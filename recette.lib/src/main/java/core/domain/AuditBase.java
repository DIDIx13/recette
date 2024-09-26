package core.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author dominique huguenin (dominique.huguenin@rpn.ch)
 */
public class AuditBase implements Audit, Serializable {

    private Instant dateCreation;

    private String userCreation;

    private Instant dateModification;

    private String userModification;

    private Instant dateSuppression;

    private String userSuppression;

    private static final Logger LOGGER
            = Logger.getLogger(AuditBase.class.getName());

    protected void setDateCreation(final Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    protected void setUserCreation(final String userCreation) {
        this.userCreation = userCreation;
    }

    protected void setDateModification(final Instant dateModification) {
        this.dateModification = dateModification;
    }

    protected void setUserModification(final String userModification) {
        this.userModification = userModification;
    }

    protected void setDateSuppression(final Instant dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    protected void setUserSuppression(final String userSuppression) {
        this.userSuppression = userSuppression;
    }

    protected AuditBase(final Builder b) {
        this.dateCreation = b.dateCreation;
        this.userCreation = b.userCreation;
        this.dateModification = b.dateModification;
        this.userModification = b.userModification;
        this.dateSuppression = b.dateSuppression;
        this.userSuppression = b.userSuppression;
    }

    @Override
    public Instant getDateCreation() {
        return this.dateCreation;
    }

    @Override
    public String getUserCreation() {
        return this.userCreation;
    }

    @Override
    public Instant getDateModification() {
        return this.dateModification;
    }

    @Override
    public String getUserModification() {
        return this.userModification;
    }

    @Override
    public Instant getDateSuppression() {
        return this.dateSuppression;
    }

    @Override
    public String getUserSuppression() {
        return this.userSuppression;
    }

    @Override
    public int hashCode() {
        int hash = HASH_CODE;
        hash = HASH_CODE_2 * hash + Objects.hashCode(this.dateCreation);
        hash = HASH_CODE_2 * hash + Objects.hashCode(this.dateModification);
        hash = HASH_CODE_2 * hash + Objects.hashCode(this.dateSuppression);
        return hash;
    }
    private static final int HASH_CODE_2 = 29;
    private static final int HASH_CODE = 7;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Audit)) {
            return false;
        }

        final Audit other = (Audit) obj;
        return this.hashCode() == other.hashCode();

    }

    @Override
    public String toString() {
        return "AuditBase{" + "dateCreation=" + dateCreation
                + ", userCreation=" + userCreation
                + ", dateModification=" + dateModification
                + ", userModification=" + userModification
                + ", dateSuppression=" + dateSuppression
                + ", userSuppression=" + userSuppression + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Instant dateCreation;
        private String userCreation;
        private Instant dateModification;
        private String userModification;
        private Instant dateSuppression;
        private String userSuppression;

        private Builder() {
            this.dateCreation = Instant.now();
        }

        public Builder audit(final Audit pAudit) {
            this.dateCreation = pAudit.getDateCreation();
            this.userCreation = pAudit.getUserCreation();
            this.dateModification = pAudit.getDateModification();
            this.userModification = pAudit.getUserModification();
            this.dateSuppression = pAudit.getDateSuppression();
            this.userSuppression = pAudit.getUserSuppression();

            return this;
        }

        public Builder dateCreation(final Instant pDate) {
            this.dateCreation = pDate;
            return this;
        }

        public Builder userCreation(final String pUser) {
            this.userCreation = pUser;
            return this;
        }

        public Builder dateModification(final Instant pDate) {
            this.dateModification = pDate;
            return this;
        }

        public Builder userModification(final String pUser) {
            this.userModification = pUser;
            return this;
        }

        public Builder dateSuppression(final Instant pDate) {
            this.dateSuppression = pDate;
            return this;
        }

        public Builder userSuppression(final String pUser) {
            this.userSuppression = pUser;
            return this;
        }

        public Audit build() {
            return new AuditBase(this);
        }
    }

}
