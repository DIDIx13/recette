package recette.routing;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import core.web.RequestUtils;
import core.web.routing.Action;
import core.web.routing.Routage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import recette.datasource.MapperManager;
import recette.datasource.Utils;
import recette.domain.Ingredient;
import recette.domain.Recette;

/**
 *
 * @author dominique huguenin ( dominique.huguenin AT rpn.ch)
 */
@WebServlet(name = "IngredientRoutage",
        urlPatterns = {"/ingredients/*"})
public class IngredientRoutage extends Routage {

    private final Pattern idPatternRegex;
    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(IngredientRoutage.class.getName());

    public IngredientRoutage() {
        idPatternRegex = Pattern.compile(IngredientUtils.ID_PATTERN_REGEX);

    }

    //CHECKSTYLE.OFF: MethodLength
    @Override
    public void init() throws ServletException {

        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.VISUALISER.name(),
                new Action.Forward(
                        IngredientUtils.PAGE_JSP_DETAIL
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

                    Ingredient detail = (Ingredient) transactionManager
                            .executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    Ingredient ingredient = mm.getIngredientMapper()
                                            .retrieve(id);

                                    if (ingredient.getRecette() != null) {

                                        Recette recette = mm.getRecetteMapper()
                                                .retrieve(
                                                        ingredient.getRecette().getIdentifiant());

                                        ingredient.setRecette(recette);

                                    }

                                    return ingredient;
                                }

                            });

                    if (detail != null) {
                        request.getSession().setAttribute(
                                IngredientUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.VISUALISATION);
                        request.setAttribute(IngredientUtils.JSP_ATTRIBUT_DETAIL,
                                detail);

                        super.execute(request,
                                response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }

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

        this.getActions().put(ActionPage.MODIFIER.name(),
                new Action.Forward(
                        IngredientUtils.PAGE_JSP_DETAIL
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

                    Ingredient detail = (Ingredient) transactionManager
                            .executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    Ingredient ingredient = mm.getIngredientMapper()
                                            .retrieve(id);

                                    if (ingredient.getRecette() != null) {

                                        Recette recette = mm.getRecetteMapper()
                                                .retrieve(
                                                        ingredient.getRecette().getIdentifiant());

                                        ingredient.setRecette(recette);

                                    }

                                    return ingredient;
                                }

                            });

                    if (detail != null) {
                        request.getSession().setAttribute(
                                IngredientUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.MODIFICATION);
                        request.setAttribute(IngredientUtils.JSP_ATTRIBUT_DETAIL,
                                detail);

                        super.execute(request,
                                response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }
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

        this.getActions().put(ActionPage.VALIDER_MODIFICATION.name(),
                new Action() {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    final Ingredient entite
                            = IngredientUtils.lireFormulaire(
                                    request,
                                    transactionManager);

                    transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            if (entite.getRecette() != null) {
                                Recette recette
                                        = mm.getRecetteMapper()
                                                .retrieve(entite.getRecette()
                                                        .getIdentifiant());
                                entite.setRecette(recette);
                            }

                            mm.getIngredientMapper().update(entite);
                            return null;
                        }
                    });
                    response.sendRedirect(String.format(
                            IngredientUtils.TEMPLATE_URL_DETAIL,
                            request.getContextPath(),
                            entite.getIdentifiant().getUUID()));

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

        this.getActions().put(ActionPage.SUPPRIMER.name(),
                new Action.Forward(
                        IngredientUtils.PAGE_JSP_DETAIL
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

                    Ingredient detail = (Ingredient) transactionManager
                            .executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    Ingredient ingredient = mm.getIngredientMapper()
                                            .retrieve(id);

                                    if (ingredient.getRecette() != null) {

                                        Recette recette = mm.getRecetteMapper()
                                                .retrieve(
                                                        ingredient.getRecette().getIdentifiant());

                                        ingredient.setRecette(recette);

                                    }

                                    return ingredient;
                                }

                            });

                    if (detail != null) {
                        request.getSession().setAttribute(
                                IngredientUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.SUPPRESSION);
                        request.setAttribute(IngredientUtils.JSP_ATTRIBUT_DETAIL,
                                detail);

                        super.execute(request,
                                response);
                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND);
                    }

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

        this.getActions().put(ActionPage.VALIDER_SUPPRESSION.name(),
                new Action.Redirect(
                        String.format(IngredientUtils.TEMPLATE_URL_LISTE,
                                this.getServletContext().getContextPath())
                ) {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                try {
                    final Ingredient entite
                            = IngredientUtils.lireFormulaire(
                                    request,
                                    transactionManager);

                    transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            mm.getIngredientMapper().delete(entite);
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

        this.getActions().put(ActionPage.QUITTER_RECHERCHE.name(),
                new ActionQuitterRecherche(
                        IngredientUtils.PAGE_JSP_DETAIL,
                        this.transactionManager));

        this.getActions().put(ActionPage.RECHERCHER_RECETTE.name(),
                new ActionRechercherRecette(
                        IngredientUtils.PAGE_JSP_DETAIL,
                        this.transactionManager));

        this.getActions().put(ActionPage.SELECTIONNER_RECETTE.name(),
                new ActionSelectionnerRecette(
                        IngredientUtils.PAGE_JSP_DETAIL,
                        this.transactionManager));

        this.getActions().put(ActionPage.SUPPRIMER_RECETTE.name(),
                new ActionSupprimerRecette(
                        IngredientUtils.PAGE_JSP_DETAIL,
                        this.transactionManager));

        this.getActions().put(ActionPage.QUITTER.name(),
                new Action() {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                EtatPage etatPage = (EtatPage) request.getSession()
                        .getAttribute(IngredientUtils.JSP_ATTRIBUT_ETAT_PAGE);

                switch (etatPage) {
                    case SUPPRESSION:
                    case MODIFICATION:
                        String uuid = RequestUtils.extractId(request,
                                idPatternRegex);

                        response.sendRedirect(String.format(
                                IngredientUtils.TEMPLATE_URL_DETAIL,
                                request.getContextPath(),
                                uuid));
                        break;

                    default:
                        response.sendRedirect(String.format(
                                IngredientUtils.TEMPLATE_URL_LISTE,
                                request.getContextPath()));
                        break;

                }

            }
        });

        this.setActionNull(this.getActions().get(ActionPage.VISUALISER.name()));

    }
    //CHECKSTYLE.ON: MethodLength

    public static class ActionRechercherRecette
            extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionRechercherRecette(
                final String path,
                final TransactionManager tm) {
            super(path);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {

                Ingredient entite
                        = IngredientUtils.lireFormulaire(
                                request,
                                transactionManager);
                request.setAttribute(IngredientUtils.JSP_ATTRIBUT_DETAIL,
                        entite);

                List<Recette> recettes
                        = (List<Recette>) transactionManager.executeTransaction(
                                new TransactionManager.Operation<MapperManager>() {
                            @Override
                            public Object execute(final MapperManager mm)
                                    throws PersistenceException {
                                return mm.getRecetteMapper()
                                        .retrieve(".*");
                            }
                        });

                request.setAttribute(
                        IngredientUtils.JSP_ATTRIBUT_FENETRE_MODALE,
                        true);
                request.setAttribute(IngredientUtils.JSP_ATTRIBUT_RECETTES,
                        recettes);

                super.execute(request,
                        response);

            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());
            }

        }
    }

    public static class ActionQuitterRecherche
            extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionQuitterRecherche(
                final String path,
                final TransactionManager tm) {
            super(path);
            this.transactionManager = tm;
        }

        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {
            try {
                Ingredient entite = IngredientUtils.lireFormulaire(
                        request,
                        transactionManager);
                request.setAttribute(IngredientUtils.JSP_ATTRIBUT_DETAIL,
                        entite);

                request.removeAttribute(
                        IngredientUtils.JSP_ATTRIBUT_FENETRE_MODALE);

                super.execute(request,
                        response);
            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());
            }
        }
    }

    public static class ActionSelectionnerRecette
            extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionSelectionnerRecette(
                final String path,
                final TransactionManager tm) {
            super(path);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {
            try {
                Ingredient entite;

                entite = IngredientUtils.lireFormulaire(
                        request,
                        transactionManager);

                Map<String, String> actionParam
                        = RequestUtils.extractActionAttribut(request);
                final String recetteUUID = actionParam.get(
                        IngredientUtils.JSP_ATTRIBUT_RECETTE_UUID);
                Recette recette = null;
                if (recetteUUID != null) {
                    recette = (Recette) transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            return mm.getRecetteMapper()
                                    .retrieve(IdentifiantBase.builder()
                                            .uuid(recetteUUID)
                                            .build());
                        }
                    });
                }

                entite.setRecette(recette);

                request.setAttribute(IngredientUtils.JSP_ATTRIBUT_DETAIL,
                        entite);
                request.removeAttribute(
                        IngredientUtils.JSP_ATTRIBUT_FENETRE_MODALE);
                super.execute(request,
                        response);
            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());
            }
        }
    }

    public static class ActionSupprimerRecette
            extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionSupprimerRecette(
                final String path,
                final TransactionManager tm) {
            super(path);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {
            try {
                Ingredient entite
                        = IngredientUtils.lireFormulaire(
                                request,
                                transactionManager);
                entite.setRecette(null);
                request.setAttribute(IngredientUtils.JSP_ATTRIBUT_DETAIL,
                        entite);

                super.execute(request,
                        response);
            } catch (PersistenceException ex) {
                LOG.log(Level.SEVERE,
                        null,
                        ex);
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ex.toString());
            }

        }
    }
}
