package recette.domain;

import core.domain.Audit;
import core.domain.EntiteBase;
import core.domain.Identifiant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public final class RecetteBase extends EntiteBase<Recette> implements Recette {

    private String nom;
    private String detail;
    private String preparation;
    private Integer nombrePersonnes;
    private final List<Composant> composants;

    private RecetteBase(final Builder b) {
        super(b.identifiant, b.audit);
        this.nom = b.nom;
        this.detail = b.detail;
        this.preparation = b.preparation;
        this.nombrePersonnes = b.nombrePersonnes;
        this.composants = new ArrayList<>();
        if (composants != null) {
            for (Composant c : b.composants) {
                this.composants.add(
                        ComposantBase.builder()
                                .composant(c)
                                .numero(getProchainNumeroPourComposant())
                                .build());
            }
        }

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
    public String getPreparation() {
        return this.preparation;
    }

    @Override
    public void setPreparation(final String preparation) {
        this.preparation = preparation;
    }

    @Override
    public Integer getNombrePersonnes() {
        return this.nombrePersonnes;
    }

    @Override
    public void setNombrePersonnes(final Integer nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    @Override
    public void update(final Recette entite) {
        if (entite == null) {
            return;
        }

        this.nom = entite.getNom();
        this.detail = entite.getDetail();
        this.preparation = entite.getPreparation();
        this.nombrePersonnes = entite.getNombrePersonnes();

        this.composants.clear();
        for (Composant c : entite.getComposants()) {
            this.addComposant(c);
        }

    }

    @Override
    public List<Composant> getComposants() {
        return Collections.unmodifiableList(this.composants);

    }

    private Integer getProchainNumeroPourComposant() {
        Integer numero = 0;

        for (Composant c : this.composants) {
            if (numero < c.getNumero()) {
                numero = c.getNumero();
            }
        }

        return numero + 1;
    }

    @Override
    public void addComposant(final Composant composant) {
        if (composant == null) {
            return;
        }

        Composant c = ComposantBase.builder()
                .composant(composant)
                .numero(getProchainNumeroPourComposant())
                .build();

        this.composants.add(c);
    }

    @Override
    public void addComposant(final int position, final Composant composant) {
        if (composant == null) {
            return;
        }

        Composant c = ComposantBase.builder()
                .composant(composant)
                .numero(getProchainNumeroPourComposant())
                .build();

        this.composants.add(position, c);
    }

    @Override
    public void removeComposant(final int position) {
        this.composants.remove(position);
    }

    @Override
    public void moveComposant(final int positionDepart, final int positionArrivee) {
        Composant c = this.composants.get(positionDepart);
        this.composants.add(positionArrivee, c);
        this.composants.remove(positionDepart);
    }

    @Override
    public String toString() {
        return "RecetteBase{" + super.toString() + "nom=" + nom
                + ", detail=" + detail + ", preparation=" + preparation
                + ", nombrePersonnes=" + nombrePersonnes
                + ", composants=" + composants + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Identifiant identifiant = null;
        private Audit audit = null;
        private String nom;
        private String detail;
        private String preparation;
        private Integer nombrePersonnes;
        private List<Composant> composants;

        protected Builder() {
            composants = new ArrayList<>();
        }

        public Recette build() {
            return new RecetteBase(this);
        }

        public Builder identifiant(final Identifiant pIdentifiant) {
            this.identifiant = pIdentifiant;
            return this;
        }

        public Builder audit(final Audit pAudit) {
            this.audit = pAudit;
            return this;
        }

        public Builder nom(final String pNom) {
            this.nom = pNom;
            return this;
        }

        public Builder detail(final String pDetail) {
            this.detail = pDetail;
            return this;
        }

        public Builder preparation(final String pPreparation) {
            this.preparation = pPreparation;
            return this;
        }

        public Builder nombrePersonnes(final Integer pNombrePersonnes) {
            this.nombrePersonnes = pNombrePersonnes;
            return this;
        }

        public Builder composant(final Composant pComposant) {
            this.composants.add(pComposant);
            return this;
        }

        /**
         *
         * @param pRecette
         * @return le builder
         */
        public Builder recette(final Recette pRecette) {
            if (pRecette != null) {
                this.identifiant = pRecette.getIdentifiant();
                this.audit = pRecette.getAudit();
                this.nom = pRecette.getNom();
                this.detail = pRecette.getDetail();
                this.preparation = pRecette.getPreparation();
                this.nombrePersonnes = pRecette.getNombrePersonnes();

                for (Composant c : pRecette.getComposants()) {
                    this.composant(c);
                }

            }
            return this;
        }
    }

}
