package recette.domain;

import core.domain.Audit;
import core.domain.Identifiant;
import java.util.List;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class RecetteRef implements Recette {

    private Identifiant identifiant;

    /**
     *
     * @param recette
     */
    public RecetteRef(final Recette recette) {
        if (recette != null) {
            this.identifiant = recette.getIdentifiant();
        }
    }

    @Override
    public Identifiant getIdentifiant() {
        return this.identifiant;
    }

    @Override
    public Audit getAudit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getNom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNom(final String nom) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDetail() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDetail(final String detail) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer getNombrePersonnes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNombrePersonnes(final Integer nombrePersonnes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPreparation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPreparation(final String preparation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Composant> getComposants() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addComposant(final Composant composant) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addComposant(final int position, final Composant composant) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeComposant(final int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void moveComposant(final int positionDepart,
            final int positionArrivee) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(final Recette unite) {
        throw new UnsupportedOperationException();
    }

}
