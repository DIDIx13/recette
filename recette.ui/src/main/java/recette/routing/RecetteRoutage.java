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
import recette.domain.Composant;
import recette.domain.ComposantBase;
import recette.domain.Ingredient;
import recette.domain.Recette;
import recette.domain.Unite;
import recette.routing.RecetteUtils.IngredientNull;

/**
 *
 * @author dominique huguenin ( dominique.huguenin AT rpn.ch)
 */
@WebServlet(name = "RecetteRoutage",
        urlPatterns = {"/recettes/*"})

public class RecetteRoutage extends Routage {

    private final Pattern idPatternRegex;

    private TransactionManager transactionManager;
    private static final Logger LOG
            = Logger.getLogger(RecetteRoutage.class.getName());

    public RecetteRoutage() {
        idPatternRegex = Pattern.compile(RecetteUtils.ID_PATTERN_REGEX);

    }

    //CHECKSTYLE.OFF: MethodLength
    @Override
    public void init() throws ServletException {

        this.transactionManager
                = Utils.getTransactionManager(this.getServletContext());

        this.getActions().put(ActionPage.VISUALISER.name(),
                new Action.Forward(
                        RecetteUtils.PAGE_JSP_DETAIL
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

                    Recette detail = (Recette) transactionManager
                            .executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getRecetteMapper()
                                            .retrieve(id);
                                }

                            });

                    if (detail != null) {
                        request.setAttribute(RecetteUtils.JSP_ATTRIBUT_DETAIL,
                                detail);

                        request.getSession().setAttribute(
                                RecetteUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.VISUALISATION);
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
                        RecetteUtils.PAGE_JSP_DETAIL
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

                    Recette detail = (Recette) transactionManager
                            .executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getRecetteMapper()
                                            .retrieve(id);
                                }
                            });

                    if (detail != null) {
                        request.setAttribute(RecetteUtils.JSP_ATTRIBUT_DETAIL,
                                detail);

                        request.getSession().setAttribute(
                                RecetteUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.MODIFICATION);
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
                    final Recette entite
                            = RecetteUtils.lireFormulaire(
                                    request,
                                    transactionManager);

                    transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            mm.getRecetteMapper().update(entite);
                            return null;
                        }
                    });

                    response.sendRedirect(String.format(
                            RecetteUtils.TEMPLATE_URL_DETAIL,
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
                        RecetteUtils.PAGE_JSP_DETAIL
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

                    Recette detail = (Recette) transactionManager
                            .executeTransaction(
                                    new TransactionManager.Operation<MapperManager>() {
                                @Override
                                public Object execute(final MapperManager mm)
                                        throws PersistenceException {
                                    return mm.getRecetteMapper()
                                            .retrieve(id);
                                }
                            });

                    if (detail != null) {
                        request.setAttribute(RecetteUtils.JSP_ATTRIBUT_DETAIL,
                                detail);

                        request.getSession().setAttribute(
                                RecetteUtils.JSP_ATTRIBUT_ETAT_PAGE,
                                EtatPage.SUPPRESSION);
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
                        String.format(RecetteUtils.TEMPLATE_URL_LISTE,
                                this.getServletContext().getContextPath())
                ) {
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

                    transactionManager.executeTransaction(
                            new TransactionManager.Operation<MapperManager>() {
                        @Override
                        public Object execute(final MapperManager mm)
                                throws PersistenceException {
                            mm.getRecetteMapper().delete(entite);
                            return null;
                        }
                    });

                    super.execute(request, response);

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
                new Action() {
            @Override
            public void execute(
                    final HttpServletRequest request,
                    final HttpServletResponse response)
                    throws ServletException, IOException {

                EtatPage etatPage = (EtatPage) request.getSession()
                        .getAttribute(RecetteUtils.JSP_ATTRIBUT_ETAT_PAGE);

                switch (etatPage) {
                    case SUPPRESSION:
                    case MODIFICATION:
                        String uuid = RequestUtils.extractId(request,
                                idPatternRegex);

                        response.sendRedirect(String.format(
                                RecetteUtils.TEMPLATE_URL_DETAIL,
                                request.getContextPath(),
                                uuid));
                        break;

                    default:
                        response.sendRedirect(String.format(
                                RecetteUtils.TEMPLATE_URL_LISTE,
                                request.getContextPath()));
                        break;

                }

            }
        });

        this.getActions().put(ActionPage.QUITTER_RECHERCHE.name(),
                new ActionQuitterRecherche(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.RECHERCHER_UNITE.name(),
                new ActionRechercherUnite(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SELECTIONNER_UNITE.name(),
                new ActionSelectionneUnite(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SUPPRIMER_UNITE.name(),
                new ActionSupprimerUnite(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.RECHERCHER_INGREDIENT.name(),
                new ActionRechercherIngredient(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SELECTIONNER_INGREDIENT.name(),
                new ActionSelectionnerIngredient(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SUPPRIMER_INGREDIENT.name(),
                new ActionSupprimerIngredient(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.AJOUTER_COMPOSANT.name(),
                new ActionAjouterComposant(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.getActions().put(ActionPage.SUPPRIMER_COMPOSANT.name(),
                new ActionSupprimerComposant(
                        RecetteUtils.PAGE_JSP_DETAIL,
                        transactionManager));

        this.setActionNull(this.getActions().get(ActionPage.VISUALISER.name()));

    }
    //CHECKSTYLE.ON: MethodLength

    public static class ActionQuitterRecherche extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionQuitterRecherche(
                final String page,
                final TransactionManager tm) {
            super(page);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                Recette recette
                        = RecetteUtils.lireFormulaire(
                                request,
                                transactionManager);

                Composant composant
                        = RecetteUtils.lireComposantFormulaire(
                                request,
                                transactionManager);

                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_DETAIL,
                        recette);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_COMPOSANT,
                        composant);

                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_INGREDIENT);
                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_UNITE);

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

    public static class ActionRechercherUnite extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionRechercherUnite(
                final String page,
                final TransactionManager tm) {
            super(page);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                Recette recette
                        = RecetteUtils.lireFormulaire(
                                request,
                                transactionManager);

                Composant composant
                        = RecetteUtils.lireComposantFormulaire(
                                request,
                                transactionManager);

                List<Unite> unites
                        = (List<Unite>) transactionManager.executeTransaction(
                                new TransactionManager.Operation<MapperManager>() {
                            @Override
                            public Object execute(final MapperManager mm)
                                    throws PersistenceException {
                                return mm.getUniteMapper()
                                        .retrieve(".*");
                            }
                        });

                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_DETAIL,
                        recette);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_COMPOSANT,
                        composant);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_UNITE,
                        unites);

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

    public static class ActionSelectionneUnite extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionSelectionneUnite(
                final String page,
                final TransactionManager tm) {
            super(page);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                Recette recette
                        = RecetteUtils.lireFormulaire(
                                request,
                                transactionManager);

                Composant composant
                        = RecetteUtils.lireComposantFormulaire(
                                request,
                                transactionManager);

                Map<String, String> actionParam
                        = RequestUtils.extractActionAttribut(request);
                final String uniteUUID = actionParam.get(
                        RecetteUtils.JSP_ATTRIBUT_COMPOSANT_UNITE_UUID);

                Unite unite
                        = (Unite) transactionManager.executeTransaction(
                                new TransactionManager.Operation<MapperManager>() {
                            @Override
                            public Object execute(final MapperManager mm)
                                    throws PersistenceException {
                                if (uniteUUID == null) {
                                    return null;

                                }

                                return mm.getUniteMapper().retrieve(
                                        IdentifiantBase.builder()
                                                .uuid(uniteUUID)
                                                .build());
                            }
                        });

                if (unite != null) {
                    composant = ComposantBase.builder()
                            .composant(composant)
                            .unite(unite)
                            .build();
                }

                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_DETAIL,
                        recette);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_COMPOSANT,
                        composant);

                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_INGREDIENT);
                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_UNITE);

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

    public static class ActionSupprimerUnite extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionSupprimerUnite(
                final String page,
                final TransactionManager tm) {
            super(page);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                Recette recette
                        = RecetteUtils.lireFormulaire(
                                request,
                                transactionManager);

                Composant composant
                        = RecetteUtils.lireComposantFormulaire(
                                request,
                                transactionManager);

                composant.setUnite(null);

                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_DETAIL,
                        recette);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_COMPOSANT,
                        composant);

                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_INGREDIENT);
                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_UNITE);

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

    public static class ActionRechercherIngredient extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionRechercherIngredient(
                final String page,
                final TransactionManager tm) {
            super(page);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                Recette recette
                        = RecetteUtils.lireFormulaire(
                                request,
                                transactionManager);

                Composant composant
                        = RecetteUtils.lireComposantFormulaire(
                                request,
                                transactionManager);

                List<Ingredient> ingredients
                        = (List<Ingredient>) transactionManager.executeTransaction(
                                new TransactionManager.Operation<MapperManager>() {
                            @Override
                            public Object execute(final MapperManager mm)
                                    throws PersistenceException {
                                return mm.getIngredientMapper()
                                        .retrieve(".*");
                            }
                        });

                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_DETAIL,
                        recette);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_COMPOSANT,
                        composant);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_INGREDIENT,
                        ingredients);

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

    public static class ActionSelectionnerIngredient extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionSelectionnerIngredient(
                final String page,
                final TransactionManager tm) {
            super(page);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                Recette recette
                        = RecetteUtils.lireFormulaire(
                                request,
                                transactionManager);

                Composant composant
                        = RecetteUtils.lireComposantFormulaire(
                                request,
                                transactionManager);

                Map<String, String> actionParam
                        = RequestUtils.extractActionAttribut(request);
                final String ingredientUUID = actionParam.get(
                        RecetteUtils.JSP_ATTRIBUT_COMPOSANT_INGREDIENT_UUID);

                Ingredient ingredient
                        = (Ingredient) transactionManager.executeTransaction(
                                new TransactionManager.Operation<MapperManager>() {
                            @Override
                            public Object execute(final MapperManager mm)
                                    throws PersistenceException {
                                if (ingredientUUID == null) {
                                    return null;

                                }

                                return mm.getIngredientMapper().retrieve(
                                        IdentifiantBase.builder()
                                                .uuid(ingredientUUID)
                                                .build());
                            }
                        });

                if (ingredient != null) {
                    composant = ComposantBase.builder()
                            .composant(composant)
                            .ingredient(ingredient)
                            .build();
                }

                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_DETAIL,
                        recette);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_COMPOSANT,
                        composant);

                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_INGREDIENT);
                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_UNITE);

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

    public static class ActionSupprimerIngredient extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionSupprimerIngredient(
                final String page,
                final TransactionManager tm) {
            super(page);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                Recette recette
                        = RecetteUtils.lireFormulaire(
                                request,
                                transactionManager);

                Composant composant
                        = RecetteUtils.lireComposantFormulaire(
                                request,
                                transactionManager);

                composant = ComposantBase.builder()
                        .composant(composant)
                        .ingredient(new IngredientNull())
                        .build();

                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_DETAIL,
                        recette);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_COMPOSANT,
                        composant);

                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_INGREDIENT);
                request.removeAttribute(
                        RecetteUtils.JSP_ATTRIBUT_LISTE_UNITE);

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

    public static class ActionAjouterComposant extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionAjouterComposant(
                final String page,
                final TransactionManager tm) {
            super(page);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                Recette recette
                        = RecetteUtils.lireFormulaire(
                                request,
                                transactionManager);

                Composant composant
                        = RecetteUtils.lireComposantFormulaire(
                                request,
                                transactionManager);

                if (!(composant.getIngredient() == null
                        || composant.getIngredient() instanceof IngredientNull)) {
                    recette.addComposant(composant);
                    composant = null;
                }

                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_DETAIL,
                        recette);
                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_COMPOSANT,
                        composant);

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

    public static class ActionSupprimerComposant extends Action.Forward {

        private final TransactionManager transactionManager;

        public ActionSupprimerComposant(
                final String page,
                final TransactionManager tm) {
            super(page);
            this.transactionManager = tm;
        }

        @Override
        public void execute(
                final HttpServletRequest request,
                final HttpServletResponse response)
                throws ServletException, IOException {

            try {
                Recette recette
                        = RecetteUtils.lireFormulaire(
                                request,
                                transactionManager);

                Map<String, String> actionParam = RequestUtils.extractActionAttribut(
                        request);
                try {
                    int index = Integer.parseInt(actionParam.get(
                            RecetteUtils.INDEX_ACTION_PARAM));
                    recette.removeComposant(index);
                } catch (NumberFormatException ex) {
                    //NE FAIS RIEN
                }

                request.setAttribute(
                        RecetteUtils.JSP_ATTRIBUT_DETAIL,
                        recette);

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
