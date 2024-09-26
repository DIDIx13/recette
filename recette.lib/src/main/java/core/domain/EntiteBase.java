package core.domain;

import java.util.Objects;

/**
 *
 * @author dom
 */
public abstract class EntiteBase<E extends Entite> implements Entite<E> {

    protected static final int HASH_CODE_SEED_2 = 29;
    protected static final int HASH_CODE_SEED_1 = 7;
    private Identifiant identifiant;
    private final Audit audit;

    protected EntiteBase(final Identifiant identifiant,
            final Audit audit) {
        this.identifiant = identifiant;
        this.audit = audit;
    }

    @Override
    public Identifiant getIdentifiant() {
        return this.identifiant;
    }

    @Override
    public Audit getAudit() {
        return audit;
    }

    protected void setIdentifiant(final Identifiant identifiant) {
        this.identifiant = identifiant;
    }

    @Override
    public int hashCode() {
        int hash = HASH_CODE_SEED_1;
        hash = HASH_CODE_SEED_2 * hash + Objects.hashCode(this.identifiant);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Entite)) {
            return false;
        }
        final Entite other = (Entite) obj;
        if (!Objects.equals(this.identifiant, other.getIdentifiant())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntiteBase{" + "identifiant=" + identifiant + '}';
    }

}
