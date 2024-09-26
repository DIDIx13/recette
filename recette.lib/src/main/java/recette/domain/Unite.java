package recette.domain;

import core.domain.Entite;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public interface Unite extends Entite<Unite> {


    String getCode();

    void setCode(String code);


}
