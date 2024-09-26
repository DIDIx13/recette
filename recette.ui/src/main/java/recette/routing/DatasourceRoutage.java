package recette.routing;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.web.routing.Action;
import core.web.routing.Routage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import recette.datasource.MapperManager;
import recette.datasource.Utils;

/**
 *
 * @author dominique huguenin (dominique.huguenin at rpn.ch)
 */
@WebServlet(name = "DatasourceRoutage",
        urlPatterns = {"/datasource.html"})
public class DatasourceRoutage extends Routage {

    private TransactionManager transactionManager;
    public static final String PAGE_JSP_DATABASE = "/WEB-INF/database.jsp";

    public static final String MSG_ERREUR_SUPPRESSION_DATASOURCE = "La suppression n'a pas étre effectuée!";
    public static final String MSG_SUCCES_SUPPRESSION_DATASOURCE = "Les objets ont été supprimés!";
    public static final String MSG_ERREUR_ECHEC_CREATION = "La création n'a pas étre effectuée!";
    public static final String MSG_SUCCES_CREATION_DATASOURCE = "Les objets ont été créés!";

    public static final String MESSAGE_SUCCES_ATTR
            = "messageSucces";
    public static final String MESSAGE_ERREUR_ATTR
            = "messageErreur";
    private static final Logger LOG = Logger.getLogger(DatasourceRoutage.class.getName());
    private DataSource datasource;

    @Override
    public void init() throws ServletException {
        super.init();

        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.VALIDER_CREATION.name(),
                new Action.Forward(PAGE_JSP_DATABASE) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {
                try {
                    transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            mm.getDatabaseSetup().createTables();
                            return null;
                        }
                    });

                    request.setAttribute(MESSAGE_SUCCES_ATTR,
                            MSG_SUCCES_CREATION_DATASOURCE);

                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    request.setAttribute(MESSAGE_ERREUR_ATTR,
                            MSG_ERREUR_ECHEC_CREATION);
                }

                super.execute(request, response);
            }
        });

        this.getActions().put(ActionPage.VALIDER_SUPPRESSION.name(),
                new Action.Forward(PAGE_JSP_DATABASE) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {
                try {
                    transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            mm.getDatabaseSetup().dropTables();
                            return null;
                        }
                    });

                    request.setAttribute(MESSAGE_SUCCES_ATTR,
                            MSG_SUCCES_SUPPRESSION_DATASOURCE);

                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    request.setAttribute(MESSAGE_ERREUR_ATTR,
                            MSG_ERREUR_SUPPRESSION_DATASOURCE);
                }

                super.execute(request, response);
            }
        });

    }

}
