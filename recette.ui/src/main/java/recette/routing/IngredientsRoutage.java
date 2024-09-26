package recette.routing;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.web.routing.Action;
import core.web.routing.Routage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import recette.datasource.MapperManager;
import recette.datasource.Utils;
import recette.domain.Ingredient;

/**
 *
 * @author dominique huguenin ( dominique.huguenin AT rpn.ch)
 */
@WebServlet(name = "IngredientsRoutage",
        urlPatterns = {"/ingredients.html"})

public class IngredientsRoutage extends Routage {

    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(IngredientsRoutage.class.getName());

    //CHECKSTYLE.OFF: MethodLength
    @Override
    public void init() throws ServletException {

        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.FILTRER.name(),
                new Action.Forward(IngredientUtils.PAGE_JSP_LISTE) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    List<Ingredient> liste = new ArrayList<>();
                    final String filtre = request.getParameter(
                            IngredientUtils.JSP_ATTRIBUT_FILTRE);
                    if (filtre != null) {
                        liste = (List<Ingredient>) transactionManager
                                .executeTransaction(
                                        new TransactionManager.Operation<MapperManager>() {
                                    @Override
                                    public Object execute(final MapperManager mm)
                                            throws PersistenceException {
                                        return mm.getIngredientMapper()
                                                .retrieve(filtre);
                                    }

                                });
                    }
                    request.setAttribute(IngredientUtils.JSP_ATTRIBUT_LISTE, liste);
                    request.getSession().setAttribute(
                            IngredientUtils.JSP_ATTRIBUT_ETAT_PAGE,
                            EtatPage.LISTE);

                    super.execute(request, response);
                } catch (java.util.regex.PatternSyntaxException ex) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            ex.getMessage());

                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());
                }
            }
        });

        this.getActions().put(ActionPage.CREER.name(),
                new Action.Forward(IngredientUtils.PAGE_JSP_DETAIL) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                request.getSession().setAttribute(
                        IngredientUtils.JSP_ATTRIBUT_ETAT_PAGE,
                        EtatPage.CREATION);
                super.execute(request, response);
            }
        });

        this.getActions().put(ActionPage.VALIDER_CREATION.name(),
                new Action() {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    final Ingredient entite = IngredientUtils.lireFormulaire(
                            request,
                            transactionManager);

                    Ingredient nouvelleEntite
                            = (Ingredient) transactionManager.executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getIngredientMapper().create(entite);
                                }
                            });

                    response.sendRedirect(String.format(
                            IngredientUtils.TEMPLATE_URL_DETAIL,
                            request.getContextPath(),
                            nouvelleEntite.getIdentifiant().getUUID()));
                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());
                }
            }
        });

        this.getActions().put(ActionPage.QUITTER_RECHERCHE.name(),
                new IngredientRoutage.ActionQuitterRecherche(
                        IngredientUtils.PAGE_JSP_DETAIL,
                        this.transactionManager));

        this.getActions().put(ActionPage.RECHERCHER_RECETTE.name(),
                new IngredientRoutage.ActionRechercherRecette(
                        IngredientUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SELECTIONNER_RECETTE.name(),
                new IngredientRoutage.ActionSelectionnerRecette(
                        IngredientUtils.PAGE_JSP_DETAIL,
                        this.transactionManager));

        this.getActions().put(ActionPage.SUPPRIMER_RECETTE.name(),
                new IngredientRoutage.ActionSupprimerRecette(
                        IngredientUtils.PAGE_JSP_DETAIL,
                        this.transactionManager));

        this.getActions().put(ActionPage.QUITTER.name(),
                new Action.Redirect(String.format(IngredientUtils.TEMPLATE_URL_LISTE,
                        this.getServletContext().getContextPath())));

        this.setActionNull(this.getActions().get(ActionPage.FILTRER.name()));

    }
    //CHECKSTYLE.ON: MethodLength

}
