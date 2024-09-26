package recette.domain;

import core.domain.IdentifiantBase;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dominique huguenin (dominique.huguenin AT rpn.ch)
 */
public class ComposantBaseTest {

    private Integer numeroRef;
    private Double quantiteRef;
    private String commentaireRef;
    private Ingredient ingredientRef;
    private Unite uniteRef;
    private Composant entiteRef;
    private static final Logger LOG = Logger.getLogger(ComposantBaseTest.class.getName());

    @Before
    public void setUp() {

        numeroRef = 1234;
        quantiteRef = 2.5;
        commentaireRef = "commentaire";

        ingredientRef
                = IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build();

        ingredientRef.setNom("nom ingrédient");
        ingredientRef.setDetail("description ingrédient");

        uniteRef = UniteBase.builder()
                .identifiant(IdentifiantBase.builder().build())
                .code("c.c")
                .build();

        entiteRef = ComposantBase.builder()
                .numero(numeroRef)
                .ingredient(ingredientRef)
                .commentaire(commentaireRef)
                .quantite(quantiteRef)
                .unite(uniteRef)
                .build();
    }

    @Test
    public void testGet() {
        Assert.assertEquals(this.numeroRef, this.entiteRef.getNumero());
        Assert.assertEquals(this.quantiteRef, this.entiteRef.getQuantite());
        Assert.assertEquals(this.commentaireRef, this.entiteRef.getCommentaire());
        Assert.assertEquals(this.ingredientRef, this.entiteRef.getIngredient());
        Assert.assertEquals(this.uniteRef, this.entiteRef.getUnite());
    }

    @Test
    public void testSet() {
        Double quantite = 2500.0;
        String commentaire = "nouveau commentaire";

        Unite unite = UniteBase.builder()
                .identifiant(IdentifiantBase.builder().build())
                .code("g")
                .build();

        this.entiteRef.setCommentaire(commentaire);
        this.entiteRef.setQuantite(quantite);
        this.entiteRef.setUnite(unite);

        Assert.assertEquals(this.numeroRef, this.entiteRef.getNumero());
        Assert.assertEquals(quantite, this.entiteRef.getQuantite());
        Assert.assertEquals(commentaire, this.entiteRef.getCommentaire());
        Assert.assertEquals(this.ingredientRef, this.entiteRef.getIngredient());
        Assert.assertEquals(unite, this.entiteRef.getUnite());
    }

    @Test
    public void testUpdate() {
        Double quantite = 2500.0;
        String commentaire = "nouveau commentaire";

        Unite unite = UniteBase.builder()
                .identifiant(IdentifiantBase.builder().build())
                .code("g")
                .build();

        Composant entite = ComposantBase.builder()
                .commentaire(commentaire)
                .quantite(quantite)
                .unite(unite)
                .build();

        this.entiteRef.update(entite);

        Assert.assertEquals(this.numeroRef, this.entiteRef.getNumero());
        Assert.assertEquals(quantite, this.entiteRef.getQuantite());
        Assert.assertEquals(commentaire, this.entiteRef.getCommentaire());
        Assert.assertEquals(this.ingredientRef, this.entiteRef.getIngredient());
        Assert.assertEquals(unite, this.entiteRef.getUnite());
    }

    @Test
    public void testUpdateNull() {
        this.entiteRef.update(null);

        Assert.assertEquals(this.numeroRef, this.entiteRef.getNumero());
        Assert.assertEquals(this.quantiteRef, this.entiteRef.getQuantite());
        Assert.assertEquals(this.commentaireRef, this.entiteRef.getCommentaire());
        Assert.assertEquals(this.ingredientRef, this.entiteRef.getIngredient());
        Assert.assertEquals(this.uniteRef, this.entiteRef.getUnite());
    }

    @Test
    public void testEquals() {
        Composant entite = ComposantBase.builder()
                .numero(this.entiteRef.getNumero())
                .ingredient(this.entiteRef.getIngredient())
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef.hashCode(), entite.hashCode());
    }

    @Test
    public void testEqualsSame() {
        Assert.assertSame(this.entiteRef, this.entiteRef);
        Assert.assertEquals(this.entiteRef, this.entiteRef);
        Assert.assertEquals(this.entiteRef.hashCode(),
                this.entiteRef.hashCode());
    }

    @Test
    public void testNotEquals() {
        Integer numero = 100;

        Composant entite = ComposantBase.builder()
                .numero(numero)
                .ingredient(this.entiteRef.getIngredient())
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
    }

    @Test
    public void testNotEquals2() {
        Integer numero = 100;

        Composant entite = ComposantBase.builder()
                .numero(numero)
                .ingredient(
                        IngredientBase.builder()
                                .identifiant(
                                        IdentifiantBase.builder()
                                                .build())
                                .build())
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
    }
    
    @Test
    public void testNotEquals3() {
        Integer numero = this.entiteRef.getNumero();

        Composant entite = ComposantBase.builder()
                .numero(numero)
                .ingredient(
                        IngredientBase.builder()
                                .identifiant(
                                        IdentifiantBase.builder()
                                                .build())
                                .build())
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
    }
    

    @Test
    public void testEqualsEntiteNull() {
        Composant entite = null;

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
    }

    @Test
    public void testEqualsObject() {
        Object entite = new Object();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
    }

    @Test
    public void testToString() {
        LOG.info(this.entiteRef.toString());
    }
    
    public void testClone() {
        Composant entite = ComposantBase.builder()
                .composant(this.entiteRef)
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef.hashCode(), entite.hashCode());

        Assert.assertEquals(this.entiteRef.getNumero(), entite.getNumero());
        Assert.assertEquals(this.entiteRef.getQuantite(), entite.getQuantite());
        Assert.assertEquals(this.entiteRef.getCommentaire(), entite.getCommentaire());
        Assert.assertEquals(this.entiteRef.getIngredient(), entite.getIngredient());
        Assert.assertEquals(this.entiteRef.getUnite(), entite.getUnite());

    }
    
    @Test(expected = NullPointerException.class)
    public void testCloneNullComposant() {
        Composant entite = ComposantBase.builder()
                .composant(null)
                .build();

    }
    

}
