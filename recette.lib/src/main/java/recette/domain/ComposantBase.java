package recette.domain;

import java.util.Objects;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class ComposantBase implements Composant {

    private final Integer numero;
    private final Ingredient ingredient;
    private Double quantite;
    private String commentaire;
    private Unite unite;

    ComposantBase(final Builder b) {
        this.numero = b.numero;
        this.ingredient = b.ingredient;
        this.quantite = b.quantite;
        this.unite = b.unite;
        this.commentaire = b.commentaire;
    }

    @Override
    public Integer getNumero() {
        return this.numero;
    }

    @Override
    public Double getQuantite() {
        return this.quantite;
    }

    @Override
    public void setQuantite(final Double quantite) {
        this.quantite = quantite;
    }

    @Override
    public String getCommentaire() {
        return this.commentaire;
    }

    @Override
    public void setCommentaire(final String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public Ingredient getIngredient() {
        return this.ingredient;
    }

    @Override
    public Unite getUnite() {
        return this.unite;
    }

    @Override
    public void setUnite(final Unite unite) {
        this.unite = unite;

    }

    @Override
    public void update(final Composant composant) {
        if (composant == null) {
            return;
        }
        this.quantite = composant.getQuantite();
        this.commentaire = composant.getCommentaire();
        this.unite = composant.getUnite();
    }

    @Override
    public int hashCode() {
        int hash = HASH_CODE_SEED_1;
        hash = HASH_CODE_SEED_2 * hash + Objects.hashCode(this.numero);
        hash = HASH_CODE_SEED_2 * hash + Objects.hashCode(this.ingredient);
        return hash;
    }
    private static final int HASH_CODE_SEED_2 = 53;
    private static final int HASH_CODE_SEED_1 = 7;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComposantBase other = (ComposantBase) obj;
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.ingredient, other.ingredient)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ComposantBase{" + "numero=" + numero
                + ", ingredient=" + ingredient
                + ", quantite=" + quantite
                + ", commentaire=" + commentaire
                + ", unite=" + unite + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Integer numero;
        private Ingredient ingredient;
        private String commentaire;
        private Double quantite;
        private Unite unite;

        protected Builder() {
        }

        public Composant build() {
            return new ComposantBase(this);
        }

        public Builder numero(final Integer pNumero) {
            this.numero = pNumero;
            return this;
        }

        public Builder ingredient(final Ingredient pIngredient) {
            this.ingredient = pIngredient;
            return this;
        }

        public Builder commentaire(final String pCommentaire) {
            this.commentaire = pCommentaire;
            return this;
        }

        public Builder quantite(final Double pQuantite) {
            this.quantite = pQuantite;
            return this;
        }

        public Builder unite(final Unite pUnite) {
            this.unite = pUnite;
            return this;
        }

        public Builder composant(final Composant pComposant) {
            if (pComposant == null) {
                throw new NullPointerException();
            }
            this.numero = pComposant.getNumero();
            this.ingredient = pComposant.getIngredient();
            this.commentaire = pComposant.getCommentaire();
            this.quantite = pComposant.getQuantite();
            this.unite = pComposant.getUnite();

            return this;
        }

    }
}
