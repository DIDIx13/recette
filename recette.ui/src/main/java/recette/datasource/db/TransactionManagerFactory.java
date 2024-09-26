package recette.datasource.db;

import core.datasource.TransactionManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author dominique huguenin <dominique.huguenin at rpn.ch>
 */
public final class TransactionManagerFactory {

    private TransactionManagerFactory() {
    }

    public static TransactionManager getInstance() throws NamingException {
        InitialContext initCtx = new InitialContext();
        DataSource datasource = (DataSource) initCtx
                .lookup("java:/comp/env/jdbc/db");

        return DbTransactionManagerImpl.getInstance(datasource);

    }

}
