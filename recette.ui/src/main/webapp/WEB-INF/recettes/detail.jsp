<%@page contentType="text/html" pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
        
--%><%@page import="recette.routing.ActionPage"%><%--
--%><%@page import="recette.routing.EtatPage"%><%--
--%><%@page import="recette.routing.RecetteUtils"%><%--

--%><!DOCTYPE html>
<html>
    <%@include file="../jspf/entete.jspf" %>
    <body>
        <%@include file="../jspf/navbar.jspf" %>
        <section>
            <header>
                <h1>Recettes</h1>
            </header>
            <%@include file="./filtre.jspf" %>
            <section>
                <h2>Détail</h2>
                <form class="grid-form" method="POST">
                    <nav>
                        <button type="submit" name="action" formnovalidate=""
                                value="<%= ActionPage.QUITTER.name()%>">
                            Quitter
                        </button>
                        <c:choose>
                            <c:when test="${EtatPage.VISUALISATION == etatPage}">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.MODIFIER.name()%>">
                                    Modifier...
                                </button>
                                <button type="submit" name="action"
                                        value="<%= ActionPage.SUPPRIMER.name()%>">
                                    Supprimer...
                                </button>

                            </c:when>
                            <c:when test="${EtatPage.MODIFICATION == etatPage}">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.VALIDER_MODIFICATION.name()%>">
                                    Modifier
                                </button>
                            </c:when>
                            <c:when test="${EtatPage.SUPPRESSION == etatPage}">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.VALIDER_SUPPRESSION.name()%>">
                                    Supprimer
                                </button>
                            </c:when>
                            <c:when test="${EtatPage.CREATION == etatPage}">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.VALIDER_CREATION.name()%>">
                                    Créer
                                </button>
                            </c:when>

                        </c:choose>

                    </nav>
                    <%@include file="../jspf/identifiant.jspf" %>

                    <input hidden="" name="${RecetteUtils.JSP_ATTRIBUT_UUID}" 
                           value="${detail.identifiant.UUID}"/>
                    <input hidden="" name="${RecetteUtils.JSP_ATTRIBUT_VERSION}" 
                           value="${detail.identifiant.version}"/>


                    <label class="new_row" for="recetteNom">nom</label>
                    <input type="text" id="recetteNom" 
                           name="${RecetteUtils.JSP_ATTRIBUT_NOM}"
                           value="${detail.nom}" 
                           <c:if test="${EtatPage.VISUALISATION == etatPage or 
                                         EtatPage.SUPPRESSION == etatPage}">
                                 readonly=""
                           </c:if>

                           placeholder="nom" required="" />
                    <label for="recetteDetail">détail</label>
                    <textarea id="recetteDetail" 
                              name="${RecetteUtils.JSP_ATTRIBUT_RECETTE_DETAIL}"
                              placeholder="détail de la recette" 
                              <c:if test="${EtatPage.VISUALISATION == etatPage or 
                                            EtatPage.SUPPRESSION == etatPage}">
                                    readonly=""
                              </c:if>

                              rows="4"
                              cols="50">${detail.detail}</textarea>
                    <label for="recettePreparation">préparation</label>
                    <textarea id="recettePreparation" 
                              name="${RecetteUtils.JSP_ATTRIBUT_PREPARATION}"
                              <c:if test="${EtatPage.VISUALISATION == etatPage or 
                                            EtatPage.SUPPRESSION == etatPage}">
                                    readonly=""
                              </c:if>


                              placeholder="préparation de la recette" 
                              rows="20" cols="80">${detail.preparation}</textarea>
                    <label for="recetteNombrePersonnes">nombre de personne</label>
                    <input type="number" 
                           id="recetteNombrePersonnes" 
                           name="${RecetteUtils.JSP_ATTRIBUT_NOMBRE_PERSONNES}"                           
                           <c:if test="${EtatPage.VISUALISATION == etatPage or 
                                         EtatPage.SUPPRESSION == etatPage}">
                                 readonly=""
                           </c:if>

                           value="${detail.nombrePersonnes}" 
                           step="1" min="1" />

                    <section>
                        <h3>listes des ingrédients</h3>

                        <span class="new_row">quantité</span>
                        <span>unité</span>
                        <span>ingrédient</span>
                        <span>commentaire</span>
                        <span>action</span>

                        <c:forEach var="composant"
                                   items="${detail.composants}"
                                   varStatus="loop">
                            <span class="new_row" >
                                <input class="new_row"
                                       type="number" 
                                       name="composants[${loop.index}]1.quantite"
                                       value='<c:out value="${composant.quantite}"/>'                                    
                                       <c:if test="${EtatPage.VISUALISATION == etatPage or 
                                                     EtatPage.SUPPRESSION == etatPage}">
                                             readonly=""
                                       </c:if>
                                       placeholder="quantité" />
                            </span>
                            <span>
                                <input hidden="" 
                                       name="composants[${loop.index}]2.unite_uuid"
                                       value='<c:out value="${composant.unite.identifiant.UUID}"/>'/>

                                <input type="text" 
                                       value='<c:out value="${composant.unite.code}"/>' 
                                       placeholder="unité" 
                                       readonly="" />
                                <c:if test="${composant.unite != null}">
                                    <a class="button" 
                                       href="${pageContext.request.contextPath}/unites/${composant.unite.identifiant.UUID}.html">
                                        Voir
                                    </a>
                                </c:if>
                                <%-- TODO - revoir la sélection d'une unité du composant 
                                <c:if test="${EtatPage.MODIFICATION== etatPage or 
                                              EtatPage.CREATION == etatPage}">
                                      <button type="submit" name="action"
                                              value="<%= ActionPage.SUPPRIMER_UNITE.name()%>[index=${loop.index}]">
                                          Supprimer
                                      </button>
                                      <button type="submit" name="action"
                                              value="<%= ActionPage.RECHERCHER_UNITE.name()%>[index=${loop.index}]">
                                          Rechercher...
                                      </button>
                                </c:if>
                                --%>
                            </span>
                            <span>
                                <input hidden="" 
                                       name="composants[${loop.index}]3.ingredient_uuid"
                                       value='<c:out value="${composant.ingredient.identifiant.UUID}"/>'/>

                                <input type="text" 
                                       value="${composant.ingredient.nom}" 
                                       readonly="" />
                                <a class="button" 
                                   href="${pageContext.request.contextPath}/ingredients/${composant.ingredient.identifiant.UUID}.html">
                                    Voir
                                </a>
                            </span>
                            <span>
                                <input type="text" 
                                       <c:if test="${EtatPage.VISUALISATION == etatPage or 
                                                     EtatPage.SUPPRESSION == etatPage}">
                                             readonly=""
                                       </c:if>
                                       name="composants[${loop.index}]4.commentaire"
                                       value="${composant.commentaire}" 
                                       placeholder="commentaire" />
                            </span>
                            <span>
                                <c:if test="${EtatPage.MODIFICATION== etatPage or 
                                              EtatPage.CREATION == etatPage}">
                                      <button type="submit" name="action"
                                              value="<%= ActionPage.SUPPRIMER_COMPOSANT.name()%>[${RecetteUtils.INDEX_ACTION_PARAM}=${loop.index}]">
                                          Supprimer
                                      </button>
                                </c:if>
                            </span>

                        </c:forEach>

                        <c:if test="${EtatPage.MODIFICATION == etatPage or 
                                      EtatPage.CREATION == etatPage}">
                              <span  class="new_row">
                                  <input 
                                      type="number" 
                                      name="${RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_QUANTITE}"
                                      value='<c:out value="${nouveauComposant.quantite}"/>'
                                      placeholder="quantité" />
                              </span>
                              <span>
                                  <input hidden="" 
                                         name="${RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_UNITE_UUID}"
                                         value='<c:out value="${nouveauComposant.unite.identifiant.UUID}"/>'/>
                                  <input type="text" 
                                         placeholder="unité" 
                                         value='<c:out value="${nouveauComposant.unite.code}"/>'
                                         readonly="" />
                                  <button type="submit" name="action"
                                          value="<%= ActionPage.SUPPRIMER_UNITE.name()%>">
                                      Supprimer
                                  </button>
                                  <button type="submit" name="action"
                                          value="<%= ActionPage.RECHERCHER_UNITE.name()%>">
                                      Rechercher...
                                  </button>
                              </span>
                              <span>
                                  <input hidden="" 
                                         name="${RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_INGREDIENT_UUID}"
                                         value='<c:out value="${nouveauComposant.ingredient.identifiant.UUID}"/>'/>
                                  <input type="text" 
                                         placeholder="ingrédient" 
                                         value='<c:out value="${nouveauComposant.ingredient.nom}"/>'
                                         readonly="" />
                                  <button type="submit" name="action"
                                          value="<%= ActionPage.SUPPRIMER_INGREDIENT.name()%>">
                                      Supprimer
                                  </button>
                                  <button type="submit" name="action"
                                          value="<%= ActionPage.RECHERCHER_INGREDIENT.name()%>">
                                      Rechercher...
                                  </button>
                              </span>
                              <span>
                                  <input type="text" 
                                         name="${RecetteUtils.JSP_ATTRIBUT_FOMULAIRE_COMMENTAIRE}"
                                         value='<c:out value="${nouveauComposant.commentaire}"/>'
                                         placeholder="commentaire" />
                              </span>
                              <span>
                                  <button type="submit" name="action"
                                          value="<%= ActionPage.AJOUTER_COMPOSANT.name()%>">
                                      Ajouter
                                  </button>
                              </span>
                        </c:if>
                    </section>

                    <c:if test="${ (uniteListe != null) or (ingredientListe != null) }">
                        <div class="FenetreModale">
                            <div class="FenetreModale-dialog">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.QUITTER_RECHERCHE.name()%>">
                                    Fermer
                                </button>

                                <c:if test="${uniteListe != null}">
                                    <c:forEach var="unite" items="${uniteListe}">
                                        <article>
                                            <header>
                                                <h3>${unite.code}</h3>
                                            </header>
                                            <button type="submit" name="action"
                                                    value="<%= ActionPage.SELECTIONNER_UNITE.name()%>[${RecetteUtils.JSP_ATTRIBUT_COMPOSANT_UNITE_UUID}=${unite.identifiant.UUID}]">
                                                Selectionner
                                            </button>
                                        </article>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${ingredientListe != null}">
                                    <c:forEach var="ingredient" items="${ingredientListe}">
                                        <article>
                                            <header>
                                                <h3>${ingredient.nom}</h3>
                                                <p>${ingredient.detail}</p>
                                            </header>
                                            <button type="submit" name="action"
                                                    value="<%= ActionPage.SELECTIONNER_INGREDIENT.name()%>[${RecetteUtils.JSP_ATTRIBUT_COMPOSANT_INGREDIENT_UUID}=${ingredient.identifiant.UUID}]">
                                                Selectionner
                                            </button>
                                        </article>
                                    </c:forEach>
                                </c:if>                                    
                            </div>
                        </div>
                    </c:if>                            

                </form>

            </section>

        </section>
    </body>

</html>
