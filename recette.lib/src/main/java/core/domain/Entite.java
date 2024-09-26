package core.domain;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public interface Entite<T> {

    Identifiant getIdentifiant();

    Audit getAudit();

    void update(T entite);

}
