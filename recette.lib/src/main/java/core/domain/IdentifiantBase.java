package core.domain;

import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class IdentifiantBase implements Identifiant {

    private final String uuid;
    private Long version;

    protected IdentifiantBase(final Builder b) {
        this.uuid = b.uuid;
        this.version = b.version;
    }

    @Override
    public String getUUID() {
        return this.uuid;
    }

    @Override
    public Long getVersion() {
        return this.version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = HASH_CODE_SEED_1;
        hash = HASH_CODE_SEED_2 * hash + Objects.hashCode(this.uuid);
        return hash;
    }
    private static final int HASH_CODE_SEED_2 = 89;
    private static final int HASH_CODE_SEED_1 = 7;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Identifiant)) {
            return false;
        }
        final Identifiant other = (Identifiant) obj;
        if (!Objects.equals(this.uuid, other.getUUID())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "IdentifiantBase{" + "uuid=" + uuid
                + ", version=" + version + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String uuid;
        private Long version;

        protected Builder() {
            this.uuid = UUID.randomUUID().toString();
            this.version = 0L;

        }

        public Builder uuid(final String pUUID) {
            this.uuid = pUUID;
            return this;
        }

        public Builder version(final Long pVersion) {
            if (pVersion != null) {
                this.version = pVersion;
            }
            return this;
        }

        public Builder identifiant(final Identifiant identifiant) {
            if (identifiant == null) {
                throw new NullPointerException();
            }
            this.uuid = identifiant.getUUID();
            this.version = identifiant.getVersion();

            return this;
        }

        public Identifiant build() {
            return new IdentifiantBase(this);
        }

    }

}
