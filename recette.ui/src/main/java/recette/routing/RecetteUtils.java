package recette.routing;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.domain.Audit;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import core.web.RequestUtils;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import recette.datasource.MapperManager;
import recette.domain.Composant;
import recette.domain.ComposantBase;
import recette.domain.Ingredient;
import recette.domain.Recette;
import recette.domain.RecetteBase;
import recette.domain.Unite;

/**
 *
 * @author dominique huguenin ( dominique.huguenin AT rpn.ch)
 */
public final class RecetteUtils {

    private RecetteUtils() {
    }

    public static final String ELEMENT = "recettes";
    public static final String ID_PATTERN_REGEX = ".*/" + ELEMENT + "/([\\w-]*).html";

    public static final String PAGE_JSP_LISTE = "/WEB-INF/" + ELEMENT + "/liste.jsp";
    public static final String PAGE_JSP_DETAIL = "/WEB-INF/" + ELEMENT + "/detail.jsp";
    public static final String TEMPLATE_URL_LISTE = "%s/" + ELEMENT + ".html";
    public static final String TEMPLATE_URL_DETAIL = "%s/" + ELEMENT + "/%s.html";

    public static final String JSP_ATTRIBUT_ETAT_PAGE = "etatPage";
    public static final String JSP_ATTRIBUT_FENETRE_MODALE = "fenetreModale";
    public static final String JSP_ATTRIBUT_LISTE_UNITE = "uniteListe";
    public static final String JSP_ATTRIBUT_LISTE_INGREDIENT = "ingredientListe";
    public static final String JSP_ATTRIBUT_FILTRE = "filtre";
    public static final String JSP_ATTRIBUT_LISTE = "liste";
    public static final String JSP_ATTRIBUT_DETAIL = "detail";

    public static final String JSP_ATTRIBUT_UUID = "uuid";
    public static final String JSP_ATTRIBUT_VERSION = "version";

    public static final String INDEX_ACTION_PARAM = "index";

    public static final String JSP_ATTRIBUT_NOM = "nom";
    public static final String JSP_ATTRIBUT_RECETTE_DETAIL = "recette_detail";
    public static final String JSP_ATTRIBUT_PREPARATION = "preparation";
    public static final String JSP_ATTRIBUT_NOMBRE_PERSONNES = "nombrePersonnes";
    public static final String COMPOSANT_PARAMETRE_PATTERN_REGEX
            = "composants\\[(\\d+)\\]\\d+\\.(\\w+)";
    public static final String JSP_ATTRIBUT_COMPOSANT_UNITE_UUID = "unite_uuid";
    public static final String JSP_ATTRIBUT_COMPOSANT_INGREDIENT_UUID = "ingredient_uuid";
    public static final String JSP_ATTRIBUT_COMPOSANT_QUANTITE = "quantite";
    public static final String JSP_ATTRIBUT_COMPOSANT_COMMENTAIRE = "commentaire";
    public static final String JSP_ATTRIBUT_FOMULAIRE_COMPOSANT = "nouveauComposant";
    public static final String JSP_ATTRIBUT_FOMULAIRE_UNITE_UUID = "nouveauComposant_unite_uuid";
    public static final String JSP_ATTRIBUT_FOMULAIRE_INGREDIENT_UUID = "nouveauComposant_ingredient_uuid";
    public static final String JSP_ATTRIBUT_FOMULAIRE_QUANTITE = "nouveauComposant_quantite";
    public static final String JSP_ATTRIBUT_FOMULAIRE_COMMENTAIRE = "nouveauComposant_commentaire";

    public static Recette lireFormulaire(
            final HttpServletRequest request,
            final TransactionManager transactionManager)
            throws PersistenceException {

        String uuid = request.getParameter(JSP_ATTRIBUT_UUID);
        if ("".equals(uuid)) {
            uuid = null;
        }
        Long version = RequestUtils.extractLongParametre(
                request, JSP_ATTRIBUT_VERSION);

        Identifiant identifiant = null;
        if (uuid != null) {
            identifiant = IdentifiantBase.builder()
                    .uuid(uuid)
                    .version(version)
                    .build();
        }
        String nom = request.getParameter(JSP_ATTRIBUT_NOM);
        String detail = request.getParameter(JSP_ATTRIBUT_RECETTE_DETAIL);
        String preparation = request.getParameter(JSP_ATTRIBUT_PREPARATION);
        Integer nombrePersonnes = RequestUtils.extractIntegerParametre(request,
                JSP_ATTRIBUT_NOMBRE_PERSONNES);

        RecetteBase.Builder builder = RecetteBase.builder()
                .identifiant(identifiant)
                .nom(nom)
                .detail(detail)
                .preparation(preparation)
                .nombrePersonnes(nombrePersonnes);

        Enumeration<String> parametres = request.getParameterNames();
        List<String> parametreList = Collections.list(parametres);
        Collections.sort(parametreList);

        Map<Integer, ComposantBase.Builder> composantBuilders = new TreeMap<>();
        Pattern pattern = Pattern.compile(COMPOSANT_PARAMETRE_PATTERN_REGEX);
        for (String param : parametreList) {
            Matcher matcher = pattern.matcher(param);
            if (matcher.matches()) {
                String indiceStr = matcher.group(1);
                Integer indice = Integer.valueOf(indiceStr);
                String attribut = matcher.group(2);

                ComposantBase.Builder b = composantBuilders.get(indice);
                if (b == null) {
                    b = ComposantBase.builder();
                    b.numero(indice);
                    composantBuilders.put(indice,
                            b);
                }

                switch (attribut) {
                    case JSP_ATTRIBUT_COMPOSANT_QUANTITE:
                        Double quantite = RequestUtils.extractDoubleParametre(
                                request,
                                param);
                        b.quantite(quantite);
                        break;

                    case JSP_ATTRIBUT_COMPOSANT_UNITE_UUID:
                        final String uniteUUID
                                = request.getParameter(param);
                        final Identifiant uniteId = IdentifiantBase.builder()
                                .uuid(uniteUUID)
                                .build();

                        Unite unite = (Unite) transactionManager.executeTransaction(
                                new TransactionManager.Operation<MapperManager>() {
                            @Override
                            public Object execute(final MapperManager mm)
                                    throws PersistenceException {
                                return mm.getUniteMapper().retrieve(uniteId);
                            }
                        });
                        b.unite(unite);

                        break;
                    case JSP_ATTRIBUT_COMPOSANT_INGREDIENT_UUID:
                        final String ingredientUUID
                                = request.getParameter(param);
                        final Identifiant ingredientID = IdentifiantBase.builder()
                                .uuid(ingredientUUID)
                                .build();

                        Ingredient ingredient = (Ingredient) transactionManager.executeTransaction(
                                new TransactionManager.Operation<MapperManager>() {
                            @Override
                            public Object execute(final MapperManager mm)
                                    throws PersistenceException {
                                return mm.getIngredientMapper().retrieve(
                                        ingredientID);
                            }
                        });
                        b.ingredient(ingredient);

                        break;
                    case JSP_ATTRIBUT_COMPOSANT_COMMENTAIRE:
                        String commentaire = request.getParameter(param);
                        b.commentaire(commentaire);
                        break;

                    default:
                        break;
                }

            }
        }

        for (ComposantBase.Builder tb : composantBuilders.values()) {
            builder.composant(tb.build());
        }
        return builder.build();
    }

    public static Composant lireComposantFormulaire(
            final HttpServletRequest request,
            final TransactionManager transactionManager)
            throws PersistenceException {

        final String uniteUUID = request.getParameter(
                JSP_ATTRIBUT_FOMULAIRE_UNITE_UUID);

        Unite unite
                = (Unite) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(final MapperManager mm)
                            throws PersistenceException {
                        if (uniteUUID == null || "".equals(uniteUUID)) {
                            return null;
                        }

                        return mm.getUniteMapper()
                                .retrieve(IdentifiantBase.builder()
                                        .uuid(uniteUUID)
                                        .build());
                    }
                });

        final String ingredientUUID = request.getParameter(
                JSP_ATTRIBUT_FOMULAIRE_INGREDIENT_UUID);

        Ingredient ingredient
                = (Ingredient) transactionManager.executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(final MapperManager mm)
                            throws PersistenceException {
                        if (ingredientUUID == null
                                || "".equals(ingredientUUID)) {
                            return null;
                        }

                        return mm.getIngredientMapper().retrieve(
                                IdentifiantBase.builder()
                                        .uuid(ingredientUUID)
                                        .build());
                    }
                });

        if (ingredient == null) {
            ingredient = new IngredientNull();
        }

        Double quantite = RequestUtils.extractDoubleParametre(request,
                JSP_ATTRIBUT_FOMULAIRE_QUANTITE);

        String commentaire = request.getParameter(
                JSP_ATTRIBUT_FOMULAIRE_COMMENTAIRE);

        return ComposantBase.builder()
                .quantite(quantite)
                .unite(unite)
                .ingredient(ingredient)
                .commentaire(commentaire)
                .build();

    }

    public static class IngredientNull
            implements Ingredient {

        @Override
        public Audit getAudit() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getDetail() {
            return null;
        }

        @Override
        public void setDetail(final String detail) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Identifiant getIdentifiant() {
            return null;
        }

        @Override
        public String getNom() {
            return null;
        }

        @Override
        public void setNom(final String nom) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Recette getRecette() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setRecette(final Recette recette) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void update(final Ingredient entite) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
