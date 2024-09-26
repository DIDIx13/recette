package recette.routing;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import core.web.RequestUtils;
import javax.servlet.http.HttpServletRequest;
import recette.datasource.MapperManager;
import recette.domain.Ingredient;
import recette.domain.IngredientBase;
import recette.domain.Recette;

/**
 *
 * @author dominique huguenin ( dominique.huguenin AT rpn.ch)
 */
public final class IngredientUtils {

    private IngredientUtils() {
    }

    public static final String ELEMENT = "ingredients";
    public static final String ID_PATTERN_REGEX = ".*/" + ELEMENT + "/([\\w-]*).html";

    public static final String PAGE_JSP_LISTE = "/WEB-INF/" + ELEMENT + "/liste.jsp";
    public static final String PAGE_JSP_DETAIL = "/WEB-INF/" + ELEMENT + "/detail.jsp";
    public static final String TEMPLATE_URL_LISTE = "%s/" + ELEMENT + ".html";
    public static final String TEMPLATE_URL_DETAIL = "%s/" + ELEMENT + "/%s.html";
    public static final String JSP_ATTRIBUT_ETAT_PAGE = "etatPage";

    public static final String JSP_ATTRIBUT_FENETRE_MODALE = "fenetreModale";

    public static final String JSP_ATTRIBUT_FILTRE = "filtre";
    public static final String JSP_ATTRIBUT_LISTE = "liste";
    public static final String JSP_ATTRIBUT_DETAIL = "detail";

    public static final String JSP_ATTRIBUT_UUID = "uuid";
    public static final String JSP_ATTRIBUT_VERSION = "version";
    public static final String JSP_ATTRIBUT_NOM = "nom";
    public static final String JSP_ATTRIBUT_INGREDIENT_DETAIL = "ingredient_detail";
    public static final String JSP_ATTRIBUT_RECETTE_UUID = "recette_uuid";
    public static final String JSP_ATTRIBUT_RECETTE_NOM = "recette_nom";
    public static final String JSP_ATTRIBUT_RECETTES = "recettes";

    public static Ingredient lireFormulaire(
            final HttpServletRequest request,
            final TransactionManager tm)
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
        String detail = request.getParameter(JSP_ATTRIBUT_INGREDIENT_DETAIL);
        final String recetteUUID = request.getParameter(
                JSP_ATTRIBUT_RECETTE_UUID);

        Recette recette = null;
        if (recetteUUID != null) {
            recette = (Recette) tm.executeTransaction(
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

        return IngredientBase.builder()
                .identifiant(identifiant)
                .nom(nom)
                .detail(detail)
                .recette(recette)
                .build();
    }
}
