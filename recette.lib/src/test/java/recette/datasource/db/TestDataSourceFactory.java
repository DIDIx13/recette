package recette.datasource.db;

import core.datasource.db.DataSourceFactory;
import javax.sql.DataSource;

/**
 *
 * @author dominique huguenin <dominique.huguenin at rpn.ch>
 */
public class TestDataSourceFactory {

    /**
     *
     * @return
     */
    public static DataSource getInstance() {
        return DataSourceFactory.getPostgreSQLDataSource(
                "postgres", 5432,
                "recetteTestDB",
                "recette", "recettepass");
    }

}
