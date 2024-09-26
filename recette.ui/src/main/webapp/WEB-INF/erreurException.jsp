<%@page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8"%><%--
--%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%--
--%><%@page import="java.util.*, java.io.*"%><%--
--%><!DOCTYPE html>
<html>
    <%@include file="./jspf/entete.jspf" %>
    <body>
        <%@include file="./jspf/navbar.jspf" %>
        <section>
            <hgroup>
                <h2>Erreur</h2>
                <h3><c:out value="${requestScope['javax.servlet.error.message']}"/></h3>

            </hgroup>
            <p><c:out value="${requestScope['javax.servlet.error.exception']}"/></p>

            <pre><%exception.printStackTrace(new PrintWriter(out));%></pre>
        </section>
    </body>
</html>
