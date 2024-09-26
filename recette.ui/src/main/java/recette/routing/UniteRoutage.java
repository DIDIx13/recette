package recette.routing;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.datasource.TransactionManager.Operation;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import core.web.RequestUtils;
import core.web.routing.Action;
import core.web.routing.Routage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import recette.datasource.MapperManager;
import recette.datasource.Utils;
import recette.domain.Unite;

/**
 *
 * @author dominique huguenin ( dominique.huguenin AT rpn.ch)
 */
@WebServlet(name = "UniteRoutage",
        urlPatterns = {"/unites/*"})
public class UniteRoutage extends Routage {

    private final Pattern idPatternRegex;
    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(UniteRoutage.class.getName());

    public UniteRoutage() {
        idPatternRegex = Pattern.compile(UniteUtils.ID_PATTERN_REGEX);

    }

    //CHECKSTYLE.OFF: MethodLength
    @Override
    public void init() throws ServletException {
        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.VISUALISER.name(),
                new Action.Forward(
                        UniteUtils.PAGE_JSP_DETAIL
                ) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    String uuid = RequestUtils.extractId(request,
                            idPatternRegex);
                    final Identifiant id = IdentifiantBase.builder()
                            .uuid(uuid)
                            .build();

                    Unite detail = (Unite) transactionManager
                            .executeTransaction(
                                    new Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getUniteMapper()
                                            .retrieve(id);
                                }

                            });

                    if (detail != null) {
                        request.getSession().setAttribute(
                                UniteUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.VISUALISATION);
                        request.setAttribute(UniteUtils.JSP_ATTRIBUT_DETAIL,
                                detail);

                        super.execute(request, response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }

                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());

                }
            }
        });

        this.getActions().put(ActionPage.MODIFIER.name(),
                new Action.Forward(
                        UniteUtils.PAGE_JSP_DETAIL
                ) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    String uuid = RequestUtils.extractId(request,
                            idPatternRegex);
                    final Identifiant id = IdentifiantBase.builder()
                            .uuid(uuid)
                            .build();

                    Unite detail = (Unite) transactionManager
                            .executeTransaction(
                                    new Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getUniteMapper()
                                            .retrieve(id);
                                }

                            });

                    if (detail != null) {
                        request.getSession().setAttribute(
                                UniteUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.MODIFICATION);
                        request.setAttribute(UniteUtils.JSP_ATTRIBUT_DETAIL,
                                detail);

                        super.execute(request, response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }

                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());

                }
            }
        });

        this.getActions().put(ActionPage.VALIDER_MODIFICATION.name(),
                new Action() {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    final Unite entite = UniteUtils.lireFormulaire(request);

                    transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            mm.getUniteMapper().update(entite);
                            return null;
                        }
                    });

                    response.sendRedirect(String.format(
                            UniteUtils.TEMPLATE_URL_DETAIL,
                            request.getContextPath(),
                            entite.getIdentifiant().getUUID()));
                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());
                }
            }
        });

        this.getActions().put(ActionPage.SUPPRIMER.name(),
                new Action.Forward(
                        UniteUtils.PAGE_JSP_DETAIL
                ) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    String uuid = RequestUtils.extractId(request,
                            idPatternRegex);
                    final Identifiant id = IdentifiantBase.builder()
                            .uuid(uuid)
                            .build();

                    Unite detail = (Unite) transactionManager
                            .executeTransaction(
                                    new Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getUniteMapper()
                                            .retrieve(id);
                                }

                            });

                    if (detail != null) {

                        request.getSession().setAttribute(
                                UniteUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.SUPPRESSION);
                        request.setAttribute(UniteUtils.JSP_ATTRIBUT_DETAIL,
                                detail);

                        super.execute(request, response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }

                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());

                }
            }
        });

        this.getActions().put(ActionPage.VALIDER_SUPPRESSION.name(),
                new Action.Redirect(
                        String.format(UniteUtils.TEMPLATE_URL_LISTE,
                                this.getServletContext().getContextPath())
                ) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    final Unite entite = UniteUtils.lireFormulaire(request);

                    transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            mm.getUniteMapper().delete(entite);
                            return null;
                        }
                    });

                    super.execute(request, response);

                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());
                }

            }
        });

        this.getActions().put(ActionPage.QUITTER.name(),
                new Action() {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                EtatPage etatPage = (EtatPage) request.getSession()
                        .getAttribute(UniteUtils.JSP_ATTRIBUT_ETAT_PAGE);

                switch (etatPage) {
                    case SUPPRESSION:
                    case MODIFICATION:
                        String uuid = RequestUtils.extractId(request,
                                idPatternRegex);

                        response.sendRedirect(String.format(
                                UniteUtils.TEMPLATE_URL_DETAIL,
                                request.getContextPath(),
                                uuid));
                        break;

                    default:
                        response.sendRedirect(String.format(
                                UniteUtils.TEMPLATE_URL_LISTE,
                                request.getContextPath()));
                        break;

                }

            }
        });

        this.setActionNull(this.getActions().get(ActionPage.VISUALISER.name()));

    }
    //CHECKSTYLE.ON: MethodLength

}
