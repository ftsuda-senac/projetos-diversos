<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" />
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
  </head>
  <body>

    <div class="container">
      <div class="row">
	<div class="col-12">
	  <h1 class="text-center">Painel de Sorteio - Entrada</h1>
	  <form action="${pageContext.request.contextPath}/painel" method="post" accept-charset="utf-8">

	    <div class="form-group">
	      <label for="txtItens">Preencher um nome por linha</label>
	      <textarea class="form-control" name="itens" id="txtItens" rows="20"></textarea>
	    </div>

	    <div class="form-check">
	      <input type="checkbox" name="aleatorio" class="form-check-input" id="chkAleatorio" value="1" />
	      <label class="form-check-label" for="chkAleatorio">Ordem de apresentação aleatória</label>
	    </div>

	    <button type="submit" class="btn btn-primary btn-block">Preparar painel</button>

	  </form>
	</div>
      </div>
    </div>

  </body>
</html>
