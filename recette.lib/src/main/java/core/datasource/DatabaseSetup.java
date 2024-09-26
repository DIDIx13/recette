package core.datasource;

/**
 *
 * @author dominique huguenin <dominique.huguenin at rpn.ch>
 */
public interface DatabaseSetup {

    /**
     *
     * @throws PersistenceException
     */
    void createTables() throws PersistenceException;

    /**
     *
     * @throws PersistenceException
     */
    void dropTables() throws PersistenceException;

    /**
     *
     * @throws PersistenceException
     */
    void insertData() throws PersistenceException;
}
