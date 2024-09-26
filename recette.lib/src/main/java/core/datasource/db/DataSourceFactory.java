package core.datasource.db;

import javax.sql.DataSource;
import org.postgresql.ds.PGPoolingDataSource;

/**
 *
 * @author dominique huguenin (dominique.huguenin@rpn.ch)
 */
public final class DataSourceFactory {

    private DataSourceFactory() {
    }

    public static DataSource getPostgreSQLDataSource(
            final String serverName,
            final int portNumber,
            final String databaseName,
            final String user,
            final String password) {

        PGPoolingDataSource ds = new PGPoolingDataSource();
        ds.setServerName(serverName);
        ds.setPortNumber(portNumber);
        ds.setDatabaseName(databaseName);
        ds.setUser(user);
        ds.setPassword(password);
        ds.setMaxConnections(MAX_CONNECTION);

        return ds;
    }
    private static final int MAX_CONNECTION = 10;
}
