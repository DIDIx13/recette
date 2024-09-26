package recette.domain;

import core.domain.AuditBase;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class DemoData {
//CHECKSTYLE.OFF: TypeName

    private final Map<Identifiant, Unite> unites;
    private final Map<Identifiant, Recette> recettes;
    private final Map<Identifiant, Ingredient> ingredients;

    private static final Logger LOG
            = Logger.getLogger(DemoData.class.getName());

    public DemoData() {
        this.unites = new HashMap<>();
        this.ingredients = new HashMap<>();
        this.recettes = new HashMap<>();
    }

    public void initialisation() {
        initialisation(0L);
    }

    public void initialisation(final Long version) {
        initialisation(version, false);
    }

    public void initialisation(final Long version,
            final boolean createAudit) {
        this.recettes.clear();
        this.ingredients.clear();
        this.unites.clear();

        this.initUnites(version,
                createAudit);
        this.initIngredients(version,
                createAudit);
        this.initRecettes(version,
                createAudit);

        Ingredient sauceTomate
                = this.ingredients.get(
                        IdentifiantBase.builder()
                                .uuid(INGREDIENTS.SAUCE_TOMATES.UUID)
                                .build());

        sauceTomate.setRecette(this.recettes.get(
                IdentifiantBase.builder()
                        .uuid(RECETTES.SAUCE_TOMATES.UUID)
                        .build()));
    }

    public Map<Identifiant, Unite> getUnites() {
        return unites;
    }

    public Map<Identifiant, Recette> getRecettes() {
        return recettes;
    }

    public Map<Identifiant, Ingredient> getIngredients() {
        return ingredients;
    }

    public static class UNITES {

        public enum FIELDS {

            UUID,
            CODE
        }

        public static class C_S {

            public static final String UUID
                    = "DEMO0000-0000-0000-0002-000000000001";

            public static final String CODE
                    = "c.s";
        }

        public static class C_C {

            public static final String UUID
                    = "DEMO0000-0000-0000-0002-000000000002";

            public static final String CODE = "c.c";
        }

        public static class G {

            public static final String UUID
                    = "DEMO0000-0000-0000-0002-000000000003";

            public static final String CODE
                    = "g";
        }

        public static class BRINS {

            public static final String UUID
                    = "DEMO0000-0000-0000-0002-000000000004";

            public static final String CODE
                    = "brins";
        }

    }

    public static class INGREDIENTS {

        public enum FIELDS {

            UUID,
            NOM,
            DETAIL,
            RECETTE_UUID
        }

        public static class AUBERGINE {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000001";

            public static final String NOM = "aubergine";

            public static final String DETAIL = "description d'une aubergine";
        }

        public static class HUILE {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000002";

            public static final String NOM = "huile";
        }

        public static class SEL {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000003";

            public static final String NOM = "sel";
        }

        public static class POIVRE {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000004";

            public static final String NOM = "poivre";
        }

        public static class CITRON_BIO {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000005";

            public static final String NOM = "citron bio";

        }

        public static class TOMATE_CERISE {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000006";

            public static final String NOM = "tomate cerise";
        }

        public static class FETA {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000007";

            public static final String NOM = "feta";
        }

        public static class THYM {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000008";

            public static final String NOM = "thym";
        }

        public static class MURE {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000009";

            public static final String NOM = "mûre";
        }

        public static class SAUCE_TOMATES {

            public static final String UUID
                    = "DEMO0000-0000-0000-0001-000000000010";

            public static final String NOM
                    = "sauce au tomates";

            public static final String DETAIL
                    = "Sauce tomate - avec des herbes aromatique";
        }

    }

    public static class RECETTES {

        public enum FIELDS {

            UUID,
            NOM,
            DETAIL,
            PREPARATION,
            NOMBRE_PERSONNES
        }

        public enum COMPOSANTS_FIELDS {

            ORDRE,
            INGREDIENT_UUID,
            QUANTITE,
            COMMENTAIRE,
            UNITE_UUID

        }

        public static class AUBERGINES_AU_FOUR {

            public static final String UUID
                    = "DEMO0000-0000-0000-0003-000000000001";

            public static final String NOM
                    = "Aubergines au four";

            public static final String DETAIL
                    = "Ces aubergines gratinées garnies de tomates, feta et "
                    + "mûres sont un régal pour l’oeil et les papilles";

            public static final String PREPARATION
                    = "Préchauffer le four à 220°C. \n"
                    + "\n"
                    + "  Partager les aubergines dans la longueur et, à "
                    + "l’aide d’un couteau, inciser \n"
                    + "  en croisillons sur env. 2 cm de profondeur, disposer"
                    + " sur une plaque chemisée \n"
                    + "  de papier cuisson. Arroser d’un filet d’huile, saler, "
                    + "poivrer.\n"
                    + "\n"
                    + "  Cuisson: env. 25 min au milieu du four. Retirer.\n"
                    + "\n"
                    + "  Râper le zeste du citron, presser 2 c.s. de jus, "
                    + "mélanger avec l’huile \n"
                    + "  dans un grand bol, saler, poivrer. Couper les tomates "
                    + "en deux, émietter la feta,\n"
                    + "  effeuiller le thym, incorporer à la sauce avec les "
                    + "mûres, répartir sur les\n"
                    + "  aubergines.";

            public static final Integer NOMBRE_PERSONNES
                    = 4;

            public static class C_1 {

                public static final int ORDRE = 1;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.AUBERGINE.UUID;

                public static final Double QUANTITE = 4.0;

                public static final String COMMENTAIRE = "d'env. 250g";

            }

            public static class C_2 {

                public static final int ORDRE = 2;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.HUILE.UUID;

                public static final Double QUANTITE = 4.0;

                public static final String UNITE_UUID = UNITES.C_S.UUID;
            }

            public static class C_3 {

                public static final int ORDRE = 3;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.SEL.UUID;

                public static final Double QUANTITE = 0.75;

                public static final String UNITE_UUID = UNITES.C_C.UUID;

            }

            public static class C_4 {

                public static final int ORDRE = 4;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.POIVRE.UUID;

                public static final String COMMENTAIRE = "un peu";

            }

            public static class C_5 {

                public static final int ORDRE = 5;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.CITRON_BIO.UUID;

                public static final Double QUANTITE = 1.0;
            }

            public static class C_6 {

                public static final int ORDRE = 6;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.HUILE.UUID;

                public static final Double QUANTITE = 3.0;

                public static final String UNITE_UUID = UNITES.C_S.UUID;
            }

            public static class C_7 {

                public static final int ORDRE = 7;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.SEL.UUID;

                public static final Double QUANTITE = 0.25;

                public static final String UNITE_UUID = UNITES.C_C.UUID;

            }

            public static class C_8 {

                public static final int ORDRE = 8;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.POIVRE.UUID;

                public static final String COMMENTAIRE = "un peu";

            }

            public static class C_9 {

                public static final int ORDRE = 9;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.TOMATE_CERISE.UUID;

                public static final Double QUANTITE = 250.0;

                public static final String UNITE_UUID = UNITES.G.UUID;
            }

            public static class C_10 {

                public static final int ORDRE = 10;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.FETA.UUID;

                public static final Double QUANTITE = 200.0;

                public static final String UNITE_UUID = UNITES.G.UUID;
            }

            public static class C_11 {

                public static final int ORDRE = 11;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.THYM.UUID;

                public static final Double QUANTITE = 4.0;

                public static final String UNITE_UUID = UNITES.BRINS.UUID;
            }

            public static class C_12 {

                public static final int ORDRE = 12;

                public static final String INGREDIENT_UUID
                        = INGREDIENTS.MURE.UUID;

                public static final Double QUANTITE = 250.0;

                public static final String UNITE_UUID = UNITES.G.UUID;
            }
        }

        public static class POIRES_AUX_AMANDES {

            public static final String UUID
                    = "DEMO0000-0000-0000-0003-000000000002";

            public static final String NOM
                    = "Poires aux amandes en chemise";

            public static final String DETAIL
                    = "Une poire habillée de lanières feuilletées et fourrée "
                    + "d’amandes. Difficile de résister!";

            public static final Integer NOMBRE_PERSONNES
                    = 4;
        }

        public static class POIVRONS_AU_FOUR {

            public static final String UUID
                    = "DEMO0000-0000-0000-0003-000000000003";

            public static final String NOM
                    = "Poivrons au four et cuisses de poulet";

            public static final String DETAIL
                    = "Super pratique à faire au four: poulet au paprika sur "
                    + "poivrons avec feta et noix de cajou.";

            public static final Integer NOMBRE_PERSONNES
                    = 4;
        }

        public static class SAUCE_TOMATES {

            public static final String UUID
                    = "DEMO0000-0000-0000-0003-000000000004";

            public static final String NOM
                    = "Sauce aux tomates";

            public static final String DETAIL
                    = "Sauce tomate - avec des herbes aromatiques";

            public static final Integer NOMBRE_PERSONNES
                    = 4;
        }
    }

    private void initUnites(final Long version,
            final boolean createAudit) {
        try {
            for (Class c : UNITES.class.getClasses()) {
                if (!c.isEnum()) {
                    String uuid = null;
                    String valeur;

                    if (c.getDeclaredField(
                            UNITES.FIELDS.UUID.name()) != null) {
                        uuid = (String) c.getDeclaredField(
                                UNITES.FIELDS.UUID.name())
                                .get(null);
                    }

                    Identifiant identifiant = IdentifiantBase.builder()
                            .uuid(uuid)
                            .version(version)
                            .build();

                    UniteBase.Builder eb = UniteBase.builder()
                            .identifiant(identifiant);

                    if (c.getDeclaredField(
                            UNITES.FIELDS.CODE.name()) != null) {
                        valeur = (String) c.getDeclaredField(
                                UNITES.FIELDS.CODE.name())
                                .get(null);

                        eb.code(valeur);
                    }
                    if (createAudit) {
                        eb.audit(AuditBase.builder()
                                .userCreation("demo")
                                .dateCreation(Instant.now())
                                .build());
                    }
                    Unite entite = eb.build();
                    this.unites.put(identifiant,
                            entite);
                }
            }
        } catch (IllegalArgumentException
                | IllegalAccessException
                | NoSuchFieldException
                | SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void initIngredients(final Long version,
            final boolean createAudit) {
        try {
            for (Class c : INGREDIENTS.class.getClasses()) {
                if (!c.isEnum()) {
                    String valeur;

                    String uuid = (String) c.getDeclaredField(
                            UNITES.FIELDS.UUID.name())
                            .get(null);

                    Identifiant identifiant = IdentifiantBase.builder()
                            .uuid(uuid)
                            .version(version)
                            .build();

                    IngredientBase.Builder eb = IngredientBase.builder()
                            .identifiant(identifiant);

                    try {
                        valeur = (String) c.getDeclaredField(
                                INGREDIENTS.FIELDS.NOM.name())
                                .get(null);

                        eb.nom(valeur);
                    } catch (NoSuchFieldException ex) {
                        /* ne fait rien */
                    }

                    try {
                        valeur = (String) c.getDeclaredField(
                                INGREDIENTS.FIELDS.DETAIL.name())
                                .get(null);

                        eb.detail(valeur);
                    } catch (NoSuchFieldException ex) {
                        /* ne fait rien */
                    }
                    if (createAudit) {
                        eb.audit(AuditBase.builder()
                                .userCreation("demo")
                                .dateCreation(Instant.now())
                                .build());
                    }

                    Ingredient entite = eb.build();
                    this.ingredients.put(identifiant,
                            entite);
                }
            }
        } catch (IllegalArgumentException
                | IllegalAccessException
                | NoSuchFieldException
                | SecurityException ex) {
            throw new RuntimeException(ex);
        }

    }

    private void initRecettes(final Long version,
            final boolean createAudit) {
        try {
            for (Class c : RECETTES.class.getClasses()) {
                if (!c.isEnum()) {
                    String valeur;

                    String uuid = (String) c.getDeclaredField(
                            RECETTES.FIELDS.UUID.name())
                            .get(null);

                    Identifiant identifiant = IdentifiantBase.builder()
                            .uuid(uuid)
                            .version(version)
                            .build();

                    RecetteBase.Builder eb = RecetteBase.builder()
                            .identifiant(identifiant);

                    try {
                        valeur = (String) c.getDeclaredField(
                                RECETTES.FIELDS.NOM.name())
                                .get(null);

                        eb.nom(valeur);
                    } catch (NoSuchFieldException ex) {
                        /* ne fait rien */
                    }

                    try {
                        valeur = (String) c.getDeclaredField(
                                RECETTES.FIELDS.DETAIL.name())
                                .get(null);

                        eb.detail(valeur);
                    } catch (NoSuchFieldException ex) {
                        /* ne fait rien */
                    }

                    try {
                        valeur = (String) c.getDeclaredField(
                                RECETTES.FIELDS.PREPARATION.name())
                                .get(null);

                        eb.preparation(valeur);
                    } catch (NoSuchFieldException ex) {
                        /* ne fait rien */
                    }

                    try {
                        Integer intValeur = (Integer) c.getDeclaredField(
                                RECETTES.FIELDS.NOMBRE_PERSONNES.name())
                                .get(null);

                        eb.nombrePersonnes(intValeur);
                    } catch (NoSuchFieldException ex) {
                        /* ne fait rien */
                    }
                    if (createAudit) {
                        eb.audit(AuditBase.builder()
                                .userCreation("demo")
                                .dateCreation(Instant.now())
                                .build());
                    }

                    Recette entite = eb.build();
                    this.recettes.put(identifiant,
                            entite);

                    Map<Integer, Composant> composants = new TreeMap<>();
                    for (Class cc : c.getClasses()) {
                        if (!cc.isEnum()) {

                            ComposantBase.Builder cb = ComposantBase.builder();

                            Integer ordre
                                    = (Integer) cc.getDeclaredField(
                                            RECETTES.COMPOSANTS_FIELDS.ORDRE
                                                    .name()
                                    ).get(null);
                            cb.numero(ordre);

                            valeur = (String) cc.getDeclaredField(
                                    RECETTES.COMPOSANTS_FIELDS.INGREDIENT_UUID
                                            .name())
                                    .get(null);

                            cb.ingredient(this.ingredients.get(
                                    IdentifiantBase.builder()
                                            .uuid(valeur)
                                            .build()));

                            try {
                                valeur = (String) cc.getDeclaredField(
                                        RECETTES.COMPOSANTS_FIELDS.COMMENTAIRE
                                                .name())
                                        .get(null);

                                cb.commentaire(valeur);
                            } catch (NoSuchFieldException ex) {
                                /* ne fait rien */
                            }

                            try {
                                Double doubleValeur = (Double) cc
                                        .getDeclaredField(
                                                RECETTES.COMPOSANTS_FIELDS.QUANTITE
                                                        .name())
                                        .get(null);

                                cb.quantite(doubleValeur);
                            } catch (NoSuchFieldException ex) {
                                /* ne fait rien */
                            }

                            try {
                                valeur = (String) cc.getDeclaredField(
                                        RECETTES.COMPOSANTS_FIELDS.UNITE_UUID
                                                .name())
                                        .get(null);

                                cb.unite(this.unites.get(
                                        IdentifiantBase.builder()
                                                .uuid(valeur)
                                                .build()));
                            } catch (NoSuchFieldException ex) {
                                /* ne fait rien */
                            }

                            Composant composant = cb.build();

                            composants.put(ordre,
                                    composant);

                        }
                    }
                    for (Map.Entry<Integer, Composant> entry
                            : composants.entrySet()) {
                        entite.addComposant(entry.getValue());
                    }

                }
            }
        } catch (IllegalArgumentException
                | IllegalAccessException
                | NoSuchFieldException
                | SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
//CHECKSTYLE.ON: TypeName
}
