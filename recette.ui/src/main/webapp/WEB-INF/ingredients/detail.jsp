<%@page contentType="text/html" pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
        
--%><%@page import="recette.routing.ActionPage"%><%--
--%><%@page import="recette.routing.EtatPage"%><%--
--%><%@page import="recette.routing.IngredientUtils"%><%--

--%><!DOCTYPE html>
<html>
    <%@include file="../jspf/entete.jspf" %>
    <body>
        <%@include file="../jspf/navbar.jspf" %>
        <section>
            <header>
                <h1>Ingrédients</h1>
            </header>
            <%@include file="./filtre.jspf" %>
            <section>
                <h2>Détail</h2>
                <form class="grid-form"  method="POST">
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
                    
                    <input hidden="" name="${IngredientUtils.JSP_ATTRIBUT_UUID}" 
                           value="${detail.identifiant.UUID}"/>
                    <input hidden="" name="${IngredientUtils.JSP_ATTRIBUT_VERSION}" 
                           value="${detail.identifiant.version}"/>
                    <label for="ingredientNom">nom</label>
                    <input type="text" id="ingredientNom" 
                           name="${IngredientUtils.JSP_ATTRIBUT_NOM}" 
                           value="${detail.nom}" 
                           <c:if test="${EtatPage.VISUALISATION == etatPage 
                                         or 
                                         EtatPage.SUPPRESSION == etatPage }"> 
                                 readonly=""
                           </c:if>                           
                           placeholder="nom" 
                           required=""/>
                    <label for="ingredientDetail">détail</label>
                    <textarea id="ingredientDetail" 
                              name="${IngredientUtils.JSP_ATTRIBUT_INGREDIENT_DETAIL}" 
                              <c:if test="${EtatPage.VISUALISATION == etatPage 
                                            or 
                                            EtatPage.SUPPRESSION == etatPage}"> 
                                    readonly=""
                              </c:if>                
                              placeholder="détail de l'ingrédient" 
                              rows="4"
                              cols="50">${detail.detail}</textarea>
                    <label for="ingredientRecette">recette</label>
                    <span>
                        <input hidden="" name="${IngredientUtils.JSP_ATTRIBUT_RECETTE_UUID}" 
                               value="${detail.recette.identifiant.UUID}"/>
                        <input type="text" id="ingredientRecette" 
                               name="${IngredientUtils.JSP_ATTRIBUT_RECETTE_NOM}" 
                               value="${detail.recette.nom}" 
                               placeholder="nom de la recette"
                               readonly />
                        <a class="button" 
                           href="${pageContext.request.contextPath}/recettes/${detail.recette.identifiant.UUID}.html">
                            Voir
                        </a>
                        <c:if test="${EtatPage.MODIFICATION == etatPage
                                      or EtatPage.CREATION == etatPage}"> 
                              <button type="submit" name="action"
                                      value="<%= ActionPage.SUPPRIMER_RECETTE.name()%>">
                                  Supprimer
                              </button>
                              <button type="submit" name="action"
                                      value="<%= ActionPage.RECHERCHER_RECETTE.name()%>">
                                  Rechercher...
                              </button>
                        </c:if>                           

                    </span>

                    <c:if test="${ fenetreModale }">
                        <div class="FenetreModale">
                            <div class="FenetreModale-dialog">
                                <button type="submit" name="action"
                                        value="<%= ActionPage.QUITTER_RECHERCHE.name()%>">
                                    Fermer
                                </button>

                                <c:forEach var="recette" items="${recettes}">
                                    <article>
                                        <header>
                                            <h3>${recette.nom}</h3>
                                            <p>${recette.detail}</p>
                                        </header>
                                        <button type="submit" name="action"
                                                formnovalidate=""
                                                value="<%= ActionPage.SELECTIONNER_RECETTE.name()%>[recette_uuid=${recette.identifiant.UUID}]">
                                            Selectionner
                                        </button>
                                    </article>
                                </c:forEach>
                            </div>
                        </div>

                    </c:if>                            
                </form>

            </section>

        </section>
    </body>

</html>
