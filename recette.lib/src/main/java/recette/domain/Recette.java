package recette.domain;

import core.domain.Entite;
import java.util.List;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public interface Recette extends Entite<Recette> {

    String getNom();

    void setNom(String nom);

    String getDetail();

    void setDetail(String detail);

    String getPreparation();

    void setPreparation(String preparation);

    Integer getNombrePersonnes();

    void setNombrePersonnes(Integer nombrePersonnes);

    List<Composant> getComposants();

    void addComposant(Composant composant);

    void addComposant(int position, Composant composant);

    void removeComposant(int position);

    void moveComposant(int positionDepart, int positionArrivee);

}
