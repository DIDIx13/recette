package recette.domain;

import core.domain.Entite;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public interface Ingredient extends Entite<Ingredient> {

    String getNom();

    void setNom(String nom);

    String getDetail();

    void setDetail(String detail);

    Recette getRecette();

    void setRecette(Recette recette);

}
