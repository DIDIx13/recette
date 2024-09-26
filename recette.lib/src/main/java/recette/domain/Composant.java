package recette.domain;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public interface Composant {

    Integer getNumero();

    Double getQuantite();

    void setQuantite(Double quantite);

    String getCommentaire();

    void setCommentaire(String commentaire);

    Ingredient getIngredient();

    Unite getUnite();

    void setUnite(Unite unite);

    void update(Composant composant);
}
