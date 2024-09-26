<%@page contentType="text/html" pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
        
--%><%@page import="recette.routing.ActionPage"%><%--
--%><%@page import="recette.routing.EtatPage"%><%--
--%><%@page import="recette.routing.UniteUtils"%><%--
--%><!DOCTYPE html>
<html>
    <%@include file="../jspf/entete.jspf" %>
    <body>
        <%@include file="../jspf/navbar.jspf" %>
        <section>
            <header>
                <h1>Unités</h1>
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

                    <input hidden="" name="${UniteUtils.JSP_ATTRIBUT_UUID}" 
                           value="${detail.identifiant.UUID}"/>
                    <input hidden="" name="${UniteUtils.JSP_ATTRIBUT_VERSION}" 
                           value="${detail.identifiant.version}"/>
                    <label for="uniteCode">code</label>
                    <input type="text" 
                           id="uniteCode" 
                           name="${UniteUtils.JSP_ATTRIBUT_CODE}"
                           value="${detail.code}" 
                           placeholder="code" required=""
                           <c:if test="${EtatPage.VISUALISATION == etatPage or 
                                         EtatPage.SUPPRESSION == etatPage}">
                                 readonly=""
                           </c:if>
                           placeholder="code" required=""
                           title="saisir le code de l'unité!"/>
                </form>
            </section>
        </section>
    </body>

</html>
