<%@page contentType="text/html" pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
        
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
                <h2>Liste</h2>
                <form method="POST" >
                    <button name="action" value="CREER">
                        Ajouter un nouvel ingrédient...
                    </button>
                </form>
                <div class="item-list">
                    <c:forEach var="detail" items="${liste}">
                        <article>
                            <header>
                                <small>${detail.identifiant.UUID}</small>
                                <h3>
                                    <a href="${pageContext.request.contextPath}/ingredients/${detail.identifiant.UUID}.html">
                                        ${detail.nom}
                                    </a>
                                </h3>
                                <p>${detail.detail}</p>
                            </header>
                        </article>
                    </c:forEach>
                </div>
            </section>
        </section>
    </body>
</html>
