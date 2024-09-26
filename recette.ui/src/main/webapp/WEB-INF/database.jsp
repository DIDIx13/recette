<%@page contentType="text/html" pageEncoding="UTF-8"%><%--
--%><%@page import="recette.routing.ActionPage"%><%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%--
--%><!DOCTYPE html>
<html>
    <head>
        <%@include file="./jspf/entete-meta.jspf" %>    
        <c:if test="${ (messageErreur != null) or (messageSucces != null) }">
            <meta http-equiv="refresh" content="10;url=${pageContext.request.contextPath}/administration/datasource.html">
        </c:if>
    </head>
    <body>
        <%@include file="./jspf/navbar.jspf" %>
        <section>
            <header>
                <h1>Administration de la source de données</h1>
            </header>

            <c:if test="${ messageSucces != null}">
                <h1>
                    OK!
                    <small>
                        <c:out value="${messageSucces}"/>
                    </small>
                </h1>
            </c:if>
            <c:if test="${ messageErreur != null}">
                <h1>
                    OUPS!
                    <small>
                        <c:out value="${messageErreur}"/>
                        Veuillez avertir l'administrateur de l'application, merci.
                    </small>
                </h1>
            </c:if>
            <div class="item-list">
                <article> 
                    <h3>Source de données</h3>
                    <p>Ces actions permettent de créer/supprimer les objets de la source de données.</p>
                    <p><a href="${pageContext.request.contextPath}/datasource.html?action=<%= ActionPage.VALIDER_CREATION.name()%>"
                          class="btn btn-primary"><i class='fa fa-save'></i>&nbsp;Créer</a>&nbsp;
                        <a href="${pageContext.request.contextPath}/datasource.html?action=<%= ActionPage.VALIDER_SUPPRESSION.name()%>"
                           class="btn btn-danger"><i class='fa fa-remove'></i>&nbsp;Supprimer</a></p>
                </article>
                <article> 
                    <h3>Jeu de démonstration</h3>
                    <p>Cette action permettent d'ajouter des données de démonstration dans la source de données.</p>
                    <p><a href="${pageContext.request.contextPath}/datasource/demodata.html?action=<%= ActionPage.VALIDER_CREATION.name()%>"
                          class="btn btn-primary"><i class='fa fa-save'></i>&nbsp;Créer</a></p>
                </article>
            </div>

        </section>
    </body>
</html>
