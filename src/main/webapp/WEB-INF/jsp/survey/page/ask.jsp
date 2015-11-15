<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tiles:insertDefinition name="layout">
    <tiles:putAttribute name="content">
        <h1>${page.title}</h1>
        <form:form method="post" modelAttribute="pageModel" class="form-inline">
            <c:forEach var="question" items="${page.questions}">
                <c:set var="question" value="${question}" scope="request"/>
                <jsp:include page="../question.jsp"></jsp:include>
            </c:forEach>
            <input type="submit" class="btn btn-default pull-right" value="weiter"/>
        </form:form>
    </tiles:putAttribute>
</tiles:insertDefinition>