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
import recette.domain.Recette;
import recette.domain.RecetteBase;

/**
 *
 * @author dominique huguenin ( dominique.huguenin AT rpn.ch)
 */
@WebServlet(name = "RecettesRoutage",
        urlPatterns = {"/recettes.html"})

public class RecettesRoutage extends Routage {

    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(RecettesRoutage.class.getName());

    //CHECKSTYLE.OFF: MethodLength
    @Override
    public void init() throws ServletException {

        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.FILTRER.name(),
                new Action.Forward(RecetteUtils.PAGE_JSP_LISTE) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    List<Recette> liste = new ArrayList<>();
                    final String filtre = request.getParameter(
                            RecetteUtils.JSP_ATTRIBUT_FILTRE);
                    if (filtre != null) {
                        liste = (List<Recette>) transactionManager
                                .executeTransaction(
                                        new TransactionManager.Operation<MapperManager>() {
                                    @Override
                                    public Object execute(final MapperManager mm)
                                            throws PersistenceException {
                                        return mm.getRecetteMapper()
                                                .retrieve(filtre);
                                    }

                                });
                    }
                    request.setAttribute(RecetteUtils.JSP_ATTRIBUT_LISTE, liste);
                    request.getSession().setAttribute(
                            RecetteUtils.JSP_ATTRIBUT_ETAT_PAGE,
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
                new Action.Forward(RecetteUtils.PAGE_JSP_DETAIL) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                Recette detail = RecetteBase.builder().build();

                if (detail != null) {
                    request.setAttribute(RecetteUtils.JSP_ATTRIBUT_DETAIL,
                            detail);

                    request.getSession().setAttribute(
                            RecetteUtils.JSP_ATTRIBUT_ETAT_PAGE,
                            EtatPage.CREATION);
                    super.execute(request,
                            response);
                } else {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND);
                }
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
                    final Recette entite
                            = RecetteUtils.lireFormulaire(
                                    request,
                                    transactionManager);

                    Recette nouvelleEntite
                            = (Recette) transactionManager.executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getRecetteMapper().create(entite);
                                }
                            });

                    response.sendRedirect(String.format(
                            RecetteUtils.TEMPLATE_URL_DETAIL,
                            request.getContextPath(),
                            nouvelleEntite.getIdentifiant().getUUID()));
                } catch (PersistenceException ex) {
                    LOG.log(Level.SEVERE,
                            null,
                            ex);
                    response.sendError(
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ex.toString());
                }
            }
        });

        this.getActions().put(ActionPage.QUITTER.name(),
                new Action.Redirect(String.format(RecetteUtils.TEMPLATE_URL_LISTE,
                        this.getServletContext().getContextPath())));

        this.getActions().put(ActionPage.QUITTER_RECHERCHE.name(),
                new RecetteRoutage.ActionQuitterRecherche(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.RECHERCHER_UNITE.name(),
                new RecetteRoutage.ActionRechercherUnite(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SELECTIONNER_UNITE.name(),
                new RecetteRoutage.ActionSelectionneUnite(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SUPPRIMER_UNITE.name(),
                new RecetteRoutage.ActionSupprimerUnite(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.RECHERCHER_INGREDIENT.name(),
                new RecetteRoutage.ActionRechercherIngredient(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SELECTIONNER_INGREDIENT.name(),
                new RecetteRoutage.ActionSelectionnerIngredient(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SUPPRIMER_INGREDIENT.name(),
                new RecetteRoutage.ActionSupprimerIngredient(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.AJOUTER_COMPOSANT.name(),
                new RecetteRoutage.ActionAjouterComposant(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SUPPRIMER_COMPOSANT.name(),
                new RecetteRoutage.ActionSupprimerComposant(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.QUITTER.name(),
                new Action.Redirect(String.format(RecetteUtils.TEMPLATE_URL_LISTE,
                        this.getServletContext().getContextPath())));

        this.setActionNull(this.getActions().get(ActionPage.FILTRER.name()));
    }
    //CHECKSTYLE.ON: MethodLength

}
