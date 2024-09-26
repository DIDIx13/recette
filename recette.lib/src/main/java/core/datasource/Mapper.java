package core.datasource;

import core.domain.Entite;
import core.domain.Identifiant;
import java.util.List;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public interface Mapper<E extends Entite> {

    E create(E entite) throws PersistenceException;

    E retrieve(Identifiant id) throws PersistenceException;

    List<E> retrieve(String filtre) throws PersistenceException;

    void update(E entite) throws PersistenceException;

    void delete(E entite) throws PersistenceException;
}
