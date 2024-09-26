package recette.datasource.db;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class SQL {

    public static final class ENTITES {

        public static final class ATTRIBUTS {

            public static final String UUID = "uuid";
            public static final String VERSION = "version";
            public static final String DATE_CREATION = "date_creation";
            public static final String USER_CREATION = "user_creation";
            public static final String DATE_MODIFICATION = "date_modification";
            public static final String USER_MODIFICATION = "user_modification";
            public static final String DATE_SUPPRESSION = "date_suppression";
            public static final String USER_SUPPRESSION = "user_suppression";
        }
    }

    public static final class UNITES {

        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS unites (\n"
                + "    uuid VARCHAR, -- aid\n"
                + "    version INTEGER DEFAULT 1,\n"
                + "    code TEXT NOT NULL,\n"
                + "\n"
                + "    date_creation TIMESTAMP DEFAULT now(),\n"
                + "    user_creation TEXT,\n "
                + "    date_modification TIMESTAMP,\n"
                + "    user_modification TEXT,\n "
                + "    date_suppression TIMESTAMP,\n"
                + "    user_suppression TEXT,\n"
                + "\n"
                + "    CONSTRAINT pk_unites\n"
                + "        PRIMARY KEY (uuid)\n"
                + ")";

        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS unites\n"
                + "    DROP CONSTRAINT IF EXISTS nid1_unites_code,\n"
                + "    ADD CONSTRAINT nid1_unites_code\n"
                + "            UNIQUE (code)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS unites CASCADE";

        public static final String INSERT
                = "INSERT INTO unites (uuid, code, user_creation)\n"
                + "VALUES (?,?,?)";

        public static final String SELECTION
                = "SELECT u.uuid,\n"
                + " u.version,\n"
                + " u.code,\n"
                + " u.date_creation, u.user_creation,\n"
                + " u.date_modification, u.user_modification,\n"
                + " u.date_suppression, u.user_suppression\n";

        public static final String SELECT_BY_UUID
                = SELECTION
                + "FROM unites u\n"
                + "WHERE u.uuid = ?\n";

        public static final String SELECT_BY_FILTRE
                = SELECTION
                + "FROM unites u\n"
                + "WHERE u.code ~* ?\n"
                + "ORDER BY u.code";

        public static final String UPDATE
                = "UPDATE unites \n"
                + "SET  code = ?,\n"
                + "     version = version + 1,\n"
                + "     date_modification = now(),\n"
                + "     user_modification = ?\n"
                + "WHERE uuid = ?\n"
                + "      AND version = ?";

        public static final String DELETE
                = "DELETE FROM unites\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE uuid = ?\n"
                + "      AND version = ?";

        public static final class ATTRIBUTS {

            public static final String CODE = "code";

        }
    }

    public static final class INGREDIENTS {

        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS ingredients (\n"
                + "    uuid VARCHAR, -- aid\n"
                + "    version INTEGER DEFAULT 1,\n"
                + "    nom TEXT NOT NULL,\n"
                + "    detail TEXT,\n"
                + "    recettes_uuid VARCHAR, -- aid\n"
                + "\n"
                + "    date_creation TIMESTAMP DEFAULT now(),\n"
                + "    user_creation TEXT,\n "
                + "    date_modification TIMESTAMP,\n"
                + "    user_modification TEXT,\n "
                + "    date_suppression TIMESTAMP,\n"
                + "    user_suppression TEXT,\n"
                + "\n"
                + "    CONSTRAINT pk_ingredients\n"
                + "        PRIMARY KEY (uuid)\n"
                + ")";

        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS ingredients\n"
                + "    DROP CONSTRAINT IF EXISTS fk1_ingredients_recettes,\n"
                + "    ADD CONSTRAINT fk1_ingredients_recettes\n"
                + "            FOREIGN KEY (recettes_uuid)\n"
                + "            REFERENCES recettes (uuid)  DEFERRABLE,\n"
                + "    DROP CONSTRAINT IF EXISTS u1_ingredients_nom,\n"
                + "    ADD CONSTRAINT u1_ingredients_nom\n"
                + "            UNIQUE (nom),\n"
                + "    DROP CONSTRAINT IF "
                + "         EXISTS u2_ingredients_recettes_uuid,\n"
                + "    ADD CONSTRAINT u2_ingredients_recettes_uuid\n"
                + "            UNIQUE (recettes_uuid)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS ingredients CASCADE";

        public static final String INSERT
                = "INSERT INTO ingredients ( uuid, nom,\n"
                + "       detail, recettes_uuid, user_creation)\n"
                + "VALUES (?,?,?,?,?)";

        public static final String DELETE
                = "DELETE FROM ingredients\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE uuid = ?\n"
                + "  AND version = ?";

        public static final String SELECTION
                = "SELECT i.uuid,\n"
                + "       i.version,\n"
                + "       i.nom,\n"
                + "       i.detail,\n"
                + "       i.recettes_uuid,\n"
                + " i.date_creation, i.user_creation,\n"
                + " i.date_modification, i.user_modification,\n"
                + " i.date_suppression, i.user_suppression\n";

        public static final String SELECT_BY_UUID
                = SELECTION
                + "FROM ingredients i\n"
                + "WHERE i.uuid = ?\n";

        public static final String SELECT_BY_FILTRE
                = SELECTION
                + "FROM ingredients i\n"
                + "WHERE i.nom ~* ?\n";

        public static final String UPDATE
                = "UPDATE ingredients\n"
                + "SET  nom = ?,\n"
                + "     detail = ?,\n"
                + "     recettes_uuid = ?,\n "
                + "     version = version + 1,\n"
                + "     date_modification = now(),\n"
                + "     user_modification = ?\n"
                + "WHERE uuid = ?\n"
                + "      AND version = ?";

        public static final class ATTRIBUTS {

            public static final String NOM = "nom";

            public static final String DETAIL = "detail";

            public static final String RECETTES_UUID = "recettes_uuid";

        }
    }

    public static final class COMPOSANTS {

        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS composants (\n"
                + "    recettes_uuid VARCHAR NOT NULL, -- aid\n"
                + "    numero INTEGER NOT NULL, --aid\n"
                + "    ordre INTEGER NOT NULL,\n"
                + "    quantite NUMERIC(8,2),\n"
                + "    commentaire TEXT,\n"
                + "    ingredients_uuid VARCHAR NOT NULL, -- aid\n"
                + "    unites_uuid VARCHAR, -- aid\n"
                + "\n"
                + "    CONSTRAINT pk_composants\n"
                + "        PRIMARY KEY (recettes_uuid,numero)\n"
                + ")";

        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS composants\n"
                + "    DROP CONSTRAINT IF EXISTS fk1_composants_recettes,\n"
                + "    ADD CONSTRAINT fk1_composants_recettes\n"
                + "            FOREIGN KEY (recettes_uuid)\n"
                + "            REFERENCES recettes (uuid) ON DELETE CASCADE,\n"
                + "    DROP CONSTRAINT IF EXISTS fk2_composants_ingredients,\n"
                + "    ADD CONSTRAINT fk2_composants_ingredients\n"
                + "            FOREIGN KEY (ingredients_uuid)\n"
                + "            REFERENCES ingredients (uuid),\n"
                + "    DROP CONSTRAINT IF EXISTS fk3_composants_unites,\n"
                + "    ADD CONSTRAINT fk3_composants_unites\n"
                + "            FOREIGN KEY (unites_uuid)\n"
                + "            REFERENCES unites (uuid),\n"
                + "    DROP CONSTRAINT IF EXISTS u1_composants_ordre,\n"
                + "    ADD CONSTRAINT u1_composants_ordre\n"
                + "            UNIQUE (recettes_uuid,ordre)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS composants CASCADE";

        public static final String INSERT
                = "INSERT INTO composants (recettes_uuid, numero, \n"
                + "                        ordre, quantite, commentaire, \n"
                + "                        ingredients_uuid, unites_uuid)\n"
                + "VALUES(?,?,?,?,?,?,?)";

        public static final String SELECTION
                = "SELECT c.numero,\n"
                + "c.quantite,\n"
                + "c.commentaire,\n"
                + "c.ingredients_uuid,\n"
                + "c.unites_uuid\n";

        public static final String SELECT_BY_UUID_RECETTE
                = SELECTION
                + "FROM composants c\n"
                + "WHERE c.recettes_uuid = ?\n"
                + "ORDER BY c.ordre\n";

        public static final String DELETE
                = "DELETE FROM composants\n";

        public static final String DELETE_BY_UUID_RECETTES
                = DELETE
                + "WHERE recettes_uuid = ?\n";

        public static final class ATTRIBUTS {

            public static final String NUMERO = "numero";

            public static final String QUANTITE = "quantite";

            public static final String COMMENTAIRE = "commentaire";

            public static final String INGREDIENTS_UUID = "ingredients_uuid";

            public static final String UNITES_UUID = "unites_uuid";
        }

    }

    public static final class RECETTES {

        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS recettes (\n"
                + "    uuid VARCHAR, -- aid\n"
                + "    version INTEGER DEFAULT 1,\n"
                + "    nom TEXT NOT NULL,\n"
                + "    detail TEXT,\n"
                + "    preparation TEXT,    \n"
                + "    nombre_personnes INTEGER DEFAULT 4,    \n"
                + "\n"
                + "    date_creation TIMESTAMP DEFAULT now(),\n"
                + "    user_creation TEXT,\n "
                + "    date_modification TIMESTAMP,\n"
                + "    user_modification TEXT,\n "
                + "    date_suppression TIMESTAMP,\n"
                + "    user_suppression TEXT,\n"
                + "\n"
                + "    CONSTRAINT pk_recettes\n"
                + "        PRIMARY KEY (uuid)\n"
                + ")";

        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS recettes\n"
                + "    DROP CONSTRAINT IF EXISTS u1_recettes_nom,\n"
                + "    ADD CONSTRAINT u1_recettes_nom\n"
                + "            UNIQUE (nom)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS recettes CASCADE";

        public static final String INSERT
                = "INSERT INTO recettes (uuid, nom, detail,\n"
                + " preparation, nombre_personnes, user_creation)\n"
                + "VALUES(?,?,?,?,?,?)";

        public static final String DELETE
                = "DELETE FROM recettes\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE uuid = ?\n"
                + "      AND version = ?";
        public static final String SELECTION
                = "SELECT r.uuid, r.version,\n"
                + "   r.nom, r.detail,\n"
                + "   r.preparation,\n"
                + "   r.nombre_personnes,\n"
                + " r.date_creation, r.user_creation,\n"
                + " r.date_modification, r.user_modification,\n"
                + " r.date_suppression, r.user_suppression\n";

        public static final String SELECT_BY_UUID
                = SELECTION
                + "FROM recettes r\n"
                + "WHERE r.uuid = ?\n";

        public static final String SELECT_BY_FILTRE
                = SELECTION
                + "FROM recettes r\n"
                + "WHERE r.nom ~* ?\n";

        public static final String UPDATE
                = "UPDATE recettes\n"
                + "SET  nom = ?,\n"
                + "     detail = ?,\n"
                + "     preparation = ?,\n "
                + "     nombre_personnes = ?,\n"
                + "     version = version + 1,\n"
                + "     date_modification = now(),\n"
                + "     user_modification = ?\n"
                + "WHERE uuid = ?"
                + "      AND version = ?";

        public static final class ATTRIBUTS {

            public static final String NOM = "nom";
            public static final String DETAIL = "detail";
            public static final String PREPARATION = "preparation";
            public static final String NOMBRE_PERSONNES = "nombre_personnes";
        }

    }
}
