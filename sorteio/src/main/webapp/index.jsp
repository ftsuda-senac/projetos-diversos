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
	  <h1 class="text-center">Sorteio</h1>
	  <form action="${pageContext.request.contextPath}/sorteio" method="post" accept-charset="utf-8">

	    <div class="form-group">
	      <label for="txtItens">Preencher um nome por linha</label>
	      <textarea class="form-control" name="itens" id="txtItens" rows="10"></textarea>
	    </div>

	    <div class="form-check">
	      <input type="checkbox" name="aleatorio" class="form-check-input" id="chkAleatorio" value="1" />
	      <label class="form-check-label" for="chkAleatorio">Ordem de apresentação aleatória</label>
	    </div>
	    <div class="form-check">
	      <input type="checkbox" name="avaliador" class="form-check-input" id="chkAleatorio" value="1" checked />
	      <label class="form-check-label" for="chkAvaliador">Sortear avaliadores</label>
	    </div>

	    <button type="submit" class="btn btn-primary btn-block">Sortear</button>

	  </form>
	</div>
      </div>
    </div>

  </body>
</html>
