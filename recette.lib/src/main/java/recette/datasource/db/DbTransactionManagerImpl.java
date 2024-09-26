package recette.datasource.db;

import core.datasource.ContrainteNotNullPersistenceException;
import core.datasource.ContrainteUniquePersistenceException;
import core.datasource.EntiteInconnuePersistenceException;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.datasource.TransactionManager.Operation;
import core.datasource.db.SQL_ERREUR_CODES;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import recette.datasource.MapperManager;

/**
 *
 * @author dominique huguenin <dominique.huguenin at rpn.ch>
 */
public final class DbTransactionManagerImpl
        implements TransactionManager<Operation<MapperManager>> {

    private static DbTransactionManagerImpl transactionManager;
    private static final Logger LOG
            = Logger.getLogger(DbTransactionManagerImpl.class.getName());

    public static TransactionManager getInstance(final DataSource datasource) {
        if (transactionManager == null) {
            transactionManager = new DbTransactionManagerImpl(datasource);
        }
        return transactionManager;
    }
    private final DataSource datasource;

    private DbTransactionManagerImpl(final DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public Object executeTransaction(final Operation<MapperManager> operation)
            throws PersistenceException {
        try (Connection conn = datasource.getConnection()) {
            conn.setAutoCommit(false);
            conn.createStatement()
                    .execute("SET CONSTRAINTS ALL DEFERRED");

            Object returnValue = operation.execute(
                    new DbMapperManagerImpl(conn));

            conn.commit();
            return returnValue;
        } catch (SQLException ex) {

            LOG.log(Level.SEVERE,
                    null,
                    ex);
            if (ex.getSQLState()
                    .equals(SQL_ERREUR_CODES.POSTGRESQL.CONSTAINT_NOT_NULL_VIOLATION)) {
                throw new ContrainteNotNullPersistenceException(ex);
            }
            if (ex.getSQLState()
                    .equals(SQL_ERREUR_CODES.POSTGRESQL.CONSTRAINT_UNIQUE_VIOLATION)) {
                throw new ContrainteUniquePersistenceException(ex);
            }
            if (ex.getSQLState()
                    .equals(SQL_ERREUR_CODES.POSTGRESQL.CONSTRAINT_FOREIGN_KEY_VIOLATION)) {
                throw new EntiteInconnuePersistenceException(ex);
            }

            throw new PersistenceException(ex);
        }
    }

}
