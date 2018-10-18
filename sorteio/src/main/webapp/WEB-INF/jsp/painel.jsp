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
    <style>
      #tblSorteio td {
	width: 10%;
	height: 100px;
	vertical-align: middle;
	text-align: center;
	font-size: 0.8em;
      }
      #tblSorteio td.passou {
	background-color: #2980b9;
      }
      #tblSorteio td.selecionado {
	background-color: #f00 !important;
      }
      #historico {
	display: none;
      }
      #modalResult .modal-content {
	background-image: url('img/silvio.jpg');
	background-repeat: no-repeat;
	background-position: center center;
	background-size: cover;
	color: white;
      }
      .modal-body p {
	font-size: 1.5em;
      }

    </style>
    <script>
      var qtItems = ${sessionScope.pessoas.size()};
      var clock = setInterval(setTime, 1);
      var counter = 0;
      var countdown = 0;
      var idSelected = "p1";
      var number = 0;

      function setTime() {
	counter++;
      }

      function selectNumber() {
	countdown--;
	$(idSelected).removeClass("selecionado");
	var currentNumber;
	do {
	  currentNumber = 1 + Math.floor(Math.random() * qtItems);
	} while (currentNumber === number);
	number = currentNumber;
	idSelected = "#p" + number;
	$(idSelected).addClass("selecionado");
	$(idSelected).addClass("passou");

	var timer;
	if (countdown > 500) {
	  timer = 10;
	} else if (countdown > 250) {
	  timer = 20;
	} else if (countdown > 100) {
	  timer = 30;
	} else if (countdown > 20) {
	  timer = 50;
	} else if (countdown > 15) {
	  timer = 100;
	} else if (countdown > 10) {
	  timer = 200;
	} else if (countdown > 7) {
	  timer = 400;
	} else if (countdown > 4) {
	  timer = 700;
	} else if (countdown > 2) {
	  timer = 1000;
	} else {
	  setTimeout(function () {
	    $("#modalResult").modal("show");
	    //alert("" + $(idSelected).text() + " foi escolhido(a) !!!");
	    $("#historico").show();
	    $("#historico ol").prepend("<li>" + $(idSelected).text() + "</li>");
	  }, 200);
	  return;
	}
	setTimeout(selectNumber, timer);
      }

      $(function () {
	$("#gatilho").mousedown(function (e) {
	  counter = 0;
	  clock = setInterval(setTime, 1);
	}).mouseup(function (e) {
	  clearInterval(clock);
	  countdown = 50 + counter;
	  if (countdown > 1000) {
	    countdown = 1000;
	  }
	  $("#tblSorteio td").removeClass("passou");
	  selectNumber();
	});

	$('#modalResult').on('show.bs.modal', function (ev) {
	  var modal = $(this);
	  modal.find('.modal-body').html("<p>A pessoa escolhida foi:<br /><b>" + $(idSelected).text() + "</b></p>")
	});
      });
    </script>
  </head>
  <body>
    <div class="container">
      <div class="row">
	<div class="col-12">
	  <h1 class="text-center">Painel de sorteio</h1>
	  <table class="table table-dark table-bordered table-sm" id="tblSorteio">
	    <tbody>
	      <tr>
		<c:forEach items="${sessionScope.pessoas}" var="nome" varStatus="stat">
		  <td id="p${stat.count}" style="width: 10%"><c:out value="${nome}" /></td>
		  <c:if test="${stat.count % 10 == 0 && !stat.last}">
		  </tr><tr>
		  </c:if>
		</c:forEach>
	      </tr>
	    </tbody>
	  </table>
	</div>
      </div>
      <div class="row">
	<div class="col-12">
	  <button type="button" id="gatilho" class="btn btn-danger btn-block">Sortear</button>
	</div>
      </div>
      <div class="row" id="historico">
	<div class="col-12">
	  <h3>Pessoas sorteadas</h3>
	  <ol reversed>
	  </ol>
	</div>
      </div>
    </div>

    <div class="modal" tabindex="-1" role="dialog" id="modalResult">
      <div class="modal-dialog" role="document">
	<div class="modal-content">
	  <div class="modal-header">
	    <h5 class="modal-title">Parabéns</h5>
	    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	      <span aria-hidden="true">&times;</span>
	    </button>
	  </div>
	  <div class="modal-body">
	    <p>Modal body text goes here.</p>
	  </div>
	  <div class="modal-footer">
	    <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
	  </div>
	</div>
      </div>
    </div>
  </body>
</html>
