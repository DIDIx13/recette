package recette.domain;

import core.domain.Audit;
import core.domain.EntiteBase;
import core.domain.Identifiant;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public final class IngredientBase
        extends EntiteBase<Ingredient>
        implements Ingredient {

    private String nom;
    private String detail;
    private Recette recette;

    private IngredientBase(final Builder b) {
        super(b.identifiant, b.audit);
        this.nom = b.nom;
        this.detail = b.detail;
        this.recette = b.recette;
    }

    @Override
    public String getNom() {
        return this.nom;
    }

    @Override
    public void setNom(final String nom) {
        this.nom = nom;
    }

    @Override
    public String getDetail() {
        return this.detail;
    }

    @Override
    public void setDetail(final String detail) {
        this.detail = detail;
    }

    @Override
    public void update(final Ingredient entite) {
        if (entite == null) {
            return;
        }

        this.nom = entite.getNom();
        this.detail = entite.getDetail();
        this.recette = entite.getRecette();
    }

    @Override
    public Recette getRecette() {
        return this.recette;
    }

    @Override
    public void setRecette(final Recette recette) {
        this.recette = recette;
    }

    @Override
    public String toString() {
        return "IngredientBase{" + super.toString() + "nom=" + nom
                + ", detail=" + detail
                + ", recette=" + (recette != null
                        ? recette.getIdentifiant() : "") + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Identifiant identifiant = null;
        private Audit audit = null;
        private String nom;
        private String detail;
        private Recette recette;

        protected Builder() {
        }

        public Builder ingredient(final Ingredient pIngredient) {
            if (pIngredient != null) {
                this.identifiant = pIngredient.getIdentifiant();
                this.audit = pIngredient.getAudit();
                this.nom = pIngredient.getNom();
                this.detail = pIngredient.getDetail();
                this.recette = pIngredient.getRecette();
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

        public Ingredient build() {
            return new IngredientBase(this);

        }

        public Builder nom(final String pNom) {
            this.nom = pNom;
            return this;
        }

        public Builder detail(final String pDetail) {
            this.detail = pDetail;
            return this;
        }

        public Builder recette(final Recette pRecette) {
            this.recette = pRecette;
            return this;
        }
    }
}
