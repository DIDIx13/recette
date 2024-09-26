package recette.domain;

import core.domain.Audit;
import core.domain.EntiteBase;
import core.domain.Identifiant;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public final class UniteBase extends EntiteBase<Unite> implements Unite {

    private String code;

    private UniteBase(final Builder b) {

        super(b.identifiant, b.audit);
        this.code = b.code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    public void update(final Unite entite) {
        if (entite == null) {
            return;
        }
        this.code = entite.getCode();
    }

    @Override
    public String toString() {
        return "UniteBase{" + super.toString() + "code=" + code + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Identifiant identifiant = null;
        private Audit audit = null;
        private String code = null;

        protected Builder() {
        }

        public Builder unite(final Unite pUnite) {
            if (pUnite != null) {
                this.identifiant = pUnite.getIdentifiant();
                this.audit = pUnite.getAudit();
                this.code = pUnite.getCode();
            }
            return this;
        }

        public Builder identifiant(final Identifiant pIdentifiant) {
            this.identifiant = pIdentifiant;
            return this;
        }

        public Builder audit(final Audit pAudit) {
            this.audit = pAudit;
            return this;
        }

        public Builder code(final String pCode) {
            this.code = pCode;
            return this;
        }

        public Unite build() {
            return new UniteBase(this);

        }
    }

}
