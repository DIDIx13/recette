package recette.domain;

import core.domain.Audit;
import core.domain.AuditBase;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dom
 */
public class RecetteBaseTest {

    private Identifiant identifiantRef;
    private String nomRef;
    private String detailRef;
    private String preparationRef;
    private Integer nombrePersonneRef;
    private Recette entiteRef;
    private static final Logger LOG = Logger.getLogger(RecetteBaseTest.class.getName());
    private Composant composantRef1;
    private Composant composantRef2;
    private Composant composantRef3;
    private List<Composant> composantsRef;
    private Audit auditRef;

    public RecetteBaseTest() {
    }

    @Before
    public void setUp() {
        identifiantRef = IdentifiantBase.builder().build();
        auditRef = AuditBase.builder()
                .dateCreation(Instant.now())
                .userCreation("user creation")
                .dateModification(Instant.now().plusSeconds(60))
                .userModification("user modification")
                .dateSuppression(Instant.now().plusSeconds(3600))
                .build();
        
        nomRef = "nom recette";
        detailRef = "description recette";
        preparationRef = "preparation recette";
        nombrePersonneRef = 4;

        composantRef1 = ComposantBase.builder()
                .numero(1)
                .ingredient(
                        IngredientBase.builder()
                                .identifiant(
                                        IdentifiantBase.builder()
                                                .build())
                                .build())
                .commentaire("commentaire 1")
                .quantite(123.0)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        composantRef2 = ComposantBase.builder()
                .numero(2)
                .ingredient(IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build())
                .commentaire("commentaire 2")
                .quantite(34.6)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        composantRef3 = ComposantBase.builder()
                .numero(3)
                .ingredient(IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build())
                .commentaire("commentaire 3")
                .quantite(45.7)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        composantsRef = new ArrayList<>();
        composantsRef.add(composantRef1);
        composantsRef.add(composantRef2);
        composantsRef.add(composantRef3);

        entiteRef = RecetteBase.builder()
                .identifiant(identifiantRef)
                .audit(auditRef)
                .nom(nomRef)
                .detail(detailRef)
                .preparation(preparationRef)
                .nombrePersonnes(nombrePersonneRef)
                .composant(composantRef1)
                .composant(composantRef2)
                .composant(composantRef3)
                .build();

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGet() {
        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        Assert.assertEquals(nomRef, this.entiteRef.getNom());
        Assert.assertEquals(detailRef, this.entiteRef.getDetail());
        Assert.assertEquals(preparationRef, this.entiteRef.getPreparation());
        Assert.assertEquals(nombrePersonneRef, this.entiteRef.getNombrePersonnes());
        testAudit(this.auditRef, this.entiteRef.getAudit());

        Assert.assertEquals(composantsRef.size(), this.entiteRef.getComposants().size());
        for (int i = 0; i < composantsRef.size(); i += 1) {
            Assert.assertEquals(composantsRef.get(i).getIngredient(),
                    this.entiteRef.getComposants().get(i).getIngredient());
            Assert.assertEquals(composantsRef.get(i).getCommentaire(),
                    this.entiteRef.getComposants().get(i).getCommentaire());
            Assert.assertEquals(composantsRef.get(i).getQuantite(),
                    this.entiteRef.getComposants().get(i).getQuantite());
            Assert.assertEquals(composantsRef.get(i).getUnite(),
                    this.entiteRef.getComposants().get(i).getUnite());
        }

    }

    private void testAudit(Audit ref, Audit audit) {
        Assert.assertEquals(ref, audit);
        Assert.assertEquals(ref.getUserCreation(), 
                audit.getUserCreation());
        Assert.assertEquals(ref.getUserModification(), 
                audit.getUserModification());
        Assert.assertEquals(ref.getUserSuppression(), 
                audit.getUserSuppression());
    }
    
    @Test
    public void testGetComposantNotSame() {
        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        Assert.assertEquals(nomRef, this.entiteRef.getNom());
        Assert.assertEquals(detailRef, this.entiteRef.getDetail());
        Assert.assertEquals(preparationRef, this.entiteRef.getPreparation());
        Assert.assertEquals(nombrePersonneRef, this.entiteRef.getNombrePersonnes());

        Assert.assertEquals(composantsRef.size(), this.entiteRef.getComposants().size());
        for (int i = 0; i < composantsRef.size(); i += 1) {
            Assert.assertNotSame(composantsRef.get(i),
                    this.entiteRef.getComposants().get(i));
            Assert.assertEquals(composantsRef.get(i).getIngredient(),
                    this.entiteRef.getComposants().get(i).getIngredient());
            Assert.assertEquals(composantsRef.get(i).getCommentaire(),
                    this.entiteRef.getComposants().get(i).getCommentaire());
            Assert.assertEquals(composantsRef.get(i).getQuantite(),
                    this.entiteRef.getComposants().get(i).getQuantite());
            Assert.assertEquals(composantsRef.get(i).getUnite(),
                    this.entiteRef.getComposants().get(i).getUnite());
        }

    }

    @Test
    public void testSet() {
        String nom = "nouveau nom";
        String detail = "nouveau détail";
        String preparation = "nouvelle preparation";
        Integer nombrePersonne = 40;

        Composant composant2 = ComposantBase.builder()
                .composant(composantRef2)
                .build();

        composant2.setCommentaire("Composant modifié");
        composant2.setQuantite(34.6);
        composant2.setUnite(
                UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build());

        Composant composant4 = ComposantBase.builder()
                .numero(4)
                .ingredient(IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build())
                .commentaire("commentaire 4")
                .quantite(45.7)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        List<Composant> composants = new ArrayList<>();
        composants.add(composant2);
        composants.add(composant4);

        this.entiteRef.setNom(nom);
        this.entiteRef.setDetail(detail);
        this.entiteRef.setPreparation(preparation);
        this.entiteRef.setNombrePersonnes(nombrePersonne);
        this.entiteRef.getComposants().get(1).update(composant2);
        this.entiteRef.removeComposant(0);
        this.entiteRef.removeComposant(1);
        this.entiteRef.addComposant(composant4);

        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        Assert.assertEquals(nom, this.entiteRef.getNom());
        Assert.assertEquals(detail, this.entiteRef.getDetail());
        Assert.assertEquals(preparation, this.entiteRef.getPreparation());
        Assert.assertEquals(nombrePersonne, this.entiteRef.getNombrePersonnes());

        for (int i = 0; i < composants.size(); i += 1) {
            Assert.assertEquals(composants.get(i).getIngredient(),
                    this.entiteRef.getComposants().get(i).getIngredient());
            Assert.assertEquals(composants.get(i).getCommentaire(),
                    this.entiteRef.getComposants().get(i).getCommentaire());
            Assert.assertEquals(composants.get(i).getQuantite(),
                    this.entiteRef.getComposants().get(i).getQuantite());
            Assert.assertEquals(composants.get(i).getUnite(),
                    this.entiteRef.getComposants().get(i).getUnite());
        }

    }

    @Test
    public void testSetComposantNotSame() {
        String nom = "nouveau nom";
        String detail = "nouveau détail";
        String preparation = "nouvelle preparation";
        Integer nombrePersonne = 40;

        Composant composant2 = ComposantBase.builder()
                .composant(composantRef2)
                .build();

        composant2.setCommentaire("Composant modifié");
        composant2.setQuantite(34.6);
        composant2.setUnite(
                UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build());

        Composant composant4 = ComposantBase.builder()
                .numero(4)
                .ingredient(IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build())
                .commentaire("commentaire 4")
                .quantite(45.7)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        List<Composant> composants = new ArrayList<>();
        composants.add(composant2);
        composants.add(composant4);

        this.entiteRef.setNom(nom);
        this.entiteRef.setDetail(detail);
        this.entiteRef.setPreparation(preparation);
        this.entiteRef.setNombrePersonnes(nombrePersonne);
        this.entiteRef.getComposants().get(1).update(composant2);
        this.entiteRef.removeComposant(0);
        this.entiteRef.removeComposant(1);
        this.entiteRef.addComposant(composant4);

        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        Assert.assertEquals(nom, this.entiteRef.getNom());
        Assert.assertEquals(detail, this.entiteRef.getDetail());
        Assert.assertEquals(preparation, this.entiteRef.getPreparation());
        Assert.assertEquals(nombrePersonne, this.entiteRef.getNombrePersonnes());

        for (int i = 0; i < composants.size(); i += 1) {
            Assert.assertNotSame(composants.get(i),
                    this.entiteRef.getComposants().get(i));

            Assert.assertEquals(composants.get(i).getIngredient(),
                    this.entiteRef.getComposants().get(i).getIngredient());
            Assert.assertEquals(composants.get(i).getCommentaire(),
                    this.entiteRef.getComposants().get(i).getCommentaire());
            Assert.assertEquals(composants.get(i).getQuantite(),
                    this.entiteRef.getComposants().get(i).getQuantite());
            Assert.assertEquals(composants.get(i).getUnite(),
                    this.entiteRef.getComposants().get(i).getUnite());
        }

    }

    @Test
    public void testAddComposantEnPosition() {
        int position = 1;
        Composant composant = ComposantBase.builder()
                .numero(4)
                .ingredient(IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build())
                .commentaire("commentaire 4")
                .quantite(45.7)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        int sizeRef = this.entiteRef.getComposants().size();

        this.entiteRef.addComposant(position, composant);

        Assert.assertEquals(sizeRef + 1, this.entiteRef.getComposants().size());
        Composant c = this.entiteRef.getComposants().get(position);
        Assert.assertNotSame(composant, c);
        Assert.assertNotNull(c.getNumero());
        Assert.assertEquals(composant.getIngredient(), c.getIngredient());
        Assert.assertEquals(composant.getCommentaire(), c.getCommentaire());
        Assert.assertEquals(composant.getQuantite(), c.getQuantite());
        Assert.assertEquals(composant.getUnite(), c.getUnite());

    }

    @Test
    public void testAddNullComposant() {

        int sizeRef = this.entiteRef.getComposants().size();

        this.entiteRef.addComposant(null);

        Assert.assertEquals(sizeRef, this.entiteRef.getComposants().size());

    }

    @Test
    public void testAddNullComposantEnPosition() {

        int sizeRef = this.entiteRef.getComposants().size();

        this.entiteRef.addComposant(0, null);

        Assert.assertEquals(sizeRef, this.entiteRef.getComposants().size());

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddComposantEnMauvaisePosition() {
        int position = -1;
        Composant composant = ComposantBase.builder()
                .numero(4)
                .ingredient(IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build())
                .commentaire("commentaire 4")
                .quantite(45.7)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        this.entiteRef.addComposant(position, composant);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddComposantEnMauvaisePosition2() {
        int position = 1000;
        Composant composant = ComposantBase.builder()
                .numero(4)
                .ingredient(IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build())
                .commentaire("commentaire 4")
                .quantite(45.7)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        this.entiteRef.addComposant(position, composant);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveComposantEnMauvaisePosition() {
        int position = -1;

        this.entiteRef.removeComposant(position);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveComposantEnMauvaisePosition2() {
        int position = 1000;

        this.entiteRef.removeComposant(position);
    }

    @Test
    public void testUpdate() {
        String nom = "nouveau nom";
        String detail = "nouveau détail";
        String preparation = "nouvelle preparation";
        Integer nombrePersonnes = 40;

        Composant composant2 = ComposantBase.builder()
                .composant(composantRef2)
                .build();
        composant2.setCommentaire("Composant modifié");
        composant2.setQuantite(34.6);
        composant2.setUnite(
                UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build());

        Composant composant4 = ComposantBase.builder()
                .numero(4)
                .ingredient(IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build())
                .commentaire("commentaire 4")
                .quantite(45.7)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        List<Composant> composants = new ArrayList<>();
        composants.add(composant2);
        composants.add(composant4);

        Recette entite = RecetteBase.builder()
                .nom(nom)
                .detail(detail)
                .preparation(preparation)
                .nombrePersonnes(nombrePersonnes)
                .composant(composant2)
                .composant(composant4)
                .build();

        this.entiteRef.update(entite);

        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        testAudit(this.auditRef, this.entiteRef.getAudit());
        Assert.assertEquals(nom, this.entiteRef.getNom());
        Assert.assertEquals(detail, this.entiteRef.getDetail());
        Assert.assertEquals(preparation, this.entiteRef.getPreparation());
        Assert.assertEquals(nombrePersonnes, this.entiteRef.getNombrePersonnes());

        Assert.assertEquals(composants.size(), this.entiteRef.getComposants().size());
        for (int i = 0; i < composants.size(); i += 1) {
            Assert.assertEquals(composants.get(i).getIngredient(),
                    this.entiteRef.getComposants().get(i).getIngredient());
            Assert.assertEquals(composants.get(i).getCommentaire(),
                    this.entiteRef.getComposants().get(i).getCommentaire());
            Assert.assertEquals(composants.get(i).getQuantite(),
                    this.entiteRef.getComposants().get(i).getQuantite());
            Assert.assertEquals(composants.get(i).getUnite(),
                    this.entiteRef.getComposants().get(i).getUnite());
        }

    }

    @Test
    public void testUpdateAvecComposantNotSame() {
        String nom = "nouveau nom";
        String detail = "nouveau détail";
        String preparation = "nouvelle preparation";
        Integer nombrePersonnes = 40;

        Composant composant2 = ComposantBase.builder()
                .composant(composantRef2)
                .build();

        composant2.setCommentaire("Composant modifié");
        composant2.setQuantite(34.6);
        composant2.setUnite(
                UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build());

        Composant composant4 = ComposantBase.builder()
                .numero(4)
                .ingredient(IngredientBase.builder()
                        .identifiant(
                                IdentifiantBase.builder()
                                        .build())
                        .build())
                .commentaire("commentaire 4")
                .quantite(45.7)
                .unite(UniteBase.builder()
                        .identifiant(IdentifiantBase.builder().build())
                        .build())
                .build();

        List<Composant> composants = new ArrayList<>();
        composants.add(composant2);
        composants.add(composant4);

        Recette entite = RecetteBase.builder()
                .nom(nom)
                .detail(detail)
                .preparation(preparation)
                .nombrePersonnes(nombrePersonnes)
                .composant(composant2)
                .composant(composant4)
                .build();

        this.entiteRef.update(entite);

        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        Assert.assertEquals(nom, this.entiteRef.getNom());
        Assert.assertEquals(detail, this.entiteRef.getDetail());
        Assert.assertEquals(preparation, this.entiteRef.getPreparation());
        Assert.assertEquals(nombrePersonnes, this.entiteRef.getNombrePersonnes());

        Assert.assertEquals(composants.size(), this.entiteRef.getComposants().size());
        for (int i = 0; i < composants.size(); i += 1) {
            Assert.assertNotSame(composants.get(i),
                    this.entiteRef.getComposants().get(i));

            Assert.assertEquals(composants.get(i).getIngredient(),
                    this.entiteRef.getComposants().get(i).getIngredient());
            Assert.assertEquals(composants.get(i).getCommentaire(),
                    this.entiteRef.getComposants().get(i).getCommentaire());
            Assert.assertEquals(composants.get(i).getQuantite(),
                    this.entiteRef.getComposants().get(i).getQuantite());
            Assert.assertEquals(composants.get(i).getUnite(),
                    this.entiteRef.getComposants().get(i).getUnite());
        }

    }

    @Test
    public void testUpdateNull() {
        this.entiteRef.update(null);

        Assert.assertEquals(identifiantRef, this.entiteRef.getIdentifiant());
        Assert.assertEquals(nomRef, this.entiteRef.getNom());
        Assert.assertEquals(detailRef, this.entiteRef.getDetail());
        Assert.assertEquals(preparationRef, this.entiteRef.getPreparation());
        Assert.assertEquals(nombrePersonneRef, this.entiteRef.getNombrePersonnes());
    }

    @Test
    public void testEquals() {
        Recette entite = RecetteBase.builder()
                .identifiant(this.entiteRef.getIdentifiant())
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef.hashCode(), entite.hashCode());
    }

    @Test
    public void testEqualsSame() {
        Assert.assertSame(this.entiteRef, this.entiteRef);
        Assert.assertEquals(this.entiteRef, this.entiteRef);
        Assert.assertEquals(this.entiteRef.hashCode(), this.entiteRef.hashCode());
    }

    @Test
    public void testNotEquals() {
        Identifiant id = IdentifiantBase.builder().build();

        Recette entite = RecetteBase.builder()
                .identifiant(id)
                .build();
        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());
    }

    @Test
    public void testEqualsNull() {
        Ingredient entite = null;

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

    @Test
    public void testMoveComposant() {
        int positionDepart = 1;
        int positionArrivee = 0;
        int size = this.entiteRef.getComposants().size();
        Composant composant1 = this.entiteRef.getComposants().get(positionDepart);
        this.entiteRef.moveComposant(positionDepart, positionArrivee);
        Composant composant2 = this.entiteRef.getComposants().get(positionArrivee);

        Assert.assertEquals(size, this.entiteRef.getComposants().size());
        Assert.assertEquals(composant1, composant2);
        Assert.assertSame(composant1, composant2);

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveComposantMauvaisePositionDepart() {
        int positionDepart = -11;
        int positionArrivee = 0;
        this.entiteRef.moveComposant(positionDepart, positionArrivee);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveComposantMauvaisePositionDepart2() {
        int positionDepart = 1000;
        int positionArrivee = 0;
        this.entiteRef.moveComposant(positionDepart, positionArrivee);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveComposantMauvaisePositionArrivee() {
        int positionDepart = 1;
        int positionArrivee = -10;
        this.entiteRef.moveComposant(positionDepart, positionArrivee);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveComposantMauvaisePositionArrivee2() {
        int positionDepart = 1;
        int positionArrivee = 10000;
        this.entiteRef.moveComposant(positionDepart, positionArrivee);
    }

    @Test
    public void testClone() {
        Recette entite = RecetteBase.builder()
                .recette(entiteRef)
                .build();
        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef, entite);
        Assert.assertEquals(this.entiteRef.hashCode(), entite.hashCode());

        Assert.assertEquals(this.entiteRef.getIdentifiant(),
                entite.getIdentifiant());
        Assert.assertEquals(this.entiteRef.getNom(),
                entite.getNom());
        Assert.assertEquals(this.entiteRef.getDetail(),
                entite.getDetail());
        Assert.assertEquals(this.entiteRef.getPreparation(),
                entite.getPreparation());
        Assert.assertEquals(this.entiteRef.getNombrePersonnes(),
                entite.getNombrePersonnes());

        testAudit(this.auditRef, this.entiteRef.getAudit());
        
        Assert.assertEquals(this.entiteRef.getComposants().size(),
                entite.getComposants().size());

        for (int i = 0; i < this.entiteRef.getComposants().size(); i += 1) {
            Assert.assertNotSame(entiteRef.getComposants().get(i),
                    entite.getComposants().get(i));

            Assert.assertEquals(this.entiteRef.getComposants().get(i)
                    .getIngredient(),
                    entite.getComposants().get(i).getIngredient());
            Assert.assertEquals(this.entiteRef.getComposants().get(i)
                    .getCommentaire(),
                    entite.getComposants().get(i).getCommentaire());
            Assert.assertEquals(this.entiteRef.getComposants().get(i)
                    .getQuantite(),
                    entite.getComposants().get(i).getQuantite());
            Assert.assertEquals(this.entiteRef.getComposants().get(i)
                    .getUnite(),
                    entite.getComposants().get(i).getUnite());
        }

    }

    @Test
    public void testCloneNull() {
        Recette entite = RecetteBase.builder()
                .recette(null)
                .build();

        Assert.assertNotSame(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef, entite);
        Assert.assertNotEquals(this.entiteRef.hashCode(), entite.hashCode());

        Assert.assertNull(entite.getIdentifiant());
        Assert.assertNull(entite.getNom());
        Assert.assertNull(entite.getDetail());
        Assert.assertNull(entite.getPreparation());
        Assert.assertNull(entite.getNombrePersonnes());

        Assert.assertEquals(0, entite.getComposants().size());

    }

}
