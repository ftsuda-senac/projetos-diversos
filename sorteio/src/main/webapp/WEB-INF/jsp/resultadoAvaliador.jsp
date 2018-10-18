<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    <script src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/popper.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
  </head>
  <body>

    <div class="container">
      <div class="row">
	<div class="col-12">
	  <h1 class="text-center">Resultado</h1>
	  <table class="table table-striped">
	    <colgroup>
	      <col style="width: 20%" />
	      <col style="width: 40%" />
	      <col style="width: 40%" />
	    <thead>
	      <tr>
		<th>#</th>
		<th>Grupo que apresenta</th>
		<th>Grupo que avalia</th>
	      </tr>
	    </thead>
	    <tbody>
	      <c:forEach items="${apresentacoes}" var="apr" varStatus="stat">
		<tr>
		  <td><c:out value="${stat.count}" /></td>
		  <td><b><c:out value="${apr.apresentador}" /></b></td>
		  <td style="color: blue;"><b><c:out value="${apr.avaliador}" /></b></td>
		</tr>
	      </c:forEach>      
	    </tbody>
	  </table>
	</div>
      </div>
    </div>

  </body>
</html>
