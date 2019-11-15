<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" />
        <script src="${pageContext.request.contextPath}/resources/js/jquery-3.3.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/popper.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    </head>
    <body>

        <div class="container">
            <div class="row">
                <div class="col-12">
                    <h1 class="text-center">System Properties</h1>
                    <table class="table table-striped table-sm">
                        <thead class="thead-dark">
                            <tr>
                                <th>Propriedade</th>
                                <th>Valor</th>
                            </tr>
                        </thead>
                        <c:if test="${systemProperties != null && !systemProperties.isEmpty()}">
                            <tbody>
                                <c:forEach items="${systemProperties}" var="prop">
                                    <tr>
                                        <td><c:out value="${prop.key}" /></td>
                                        <td><c:out value="${prop.value}" /></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </c:if>
                    </table>
                    <hr />
                    <h1 class="text-center">Environment variables</h1>
                    <table class="table table-striped table-sm">
                        <thead class="thead-dark">
                            <tr>
                                <th>Nome</th>
                                <th>Valor</th>
                            </tr>
                        </thead>
                        <c:if test="${environmentVariables != null && !environmentVariables.isEmpty()}">
                            <tbody>
                                <c:forEach items="${environmentVariables}" var="prop">
                                    <tr>
                                        <td><c:out value="${prop.key}" /></td>
                                        <td><c:out value="${prop.value}" /></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </c:if>
                    </table>

                </div>
            </div>
        </div>

    </body>
</html>
