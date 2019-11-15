<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exemplo para gerar URLs de recursos estáticos</title>
        <link href="${pageContext.request.contextPath}/resources/css/arquivo1.css" rel="stylesheet" />
        <link href="resources/css/arquivo2.css" rel="stylesheet" />
        <script src="resources/js/arquivo1.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/arquivo2.js"></script>
    </head>
    <body>
        <h1>Exemplo de referência aos arquivos estaticos dentro do próprio projeto</h1>
        <div>
            <img src="resources/img/senac_logo.png" alt="Essa imagem não vai carregar" />
        </div>
        <div>
            <img src="${pageContext.request.contextPath}/resources/img/senac_logo.png" alt="Essa imagem carrega corretamente" title="Essa imagem carrega corretamente" style="width: 100px;" />
        </div>
        <p>Ver o código-fonte gerado no navegador (<code>CTRL + U</code> no Chrome) e o código no JSP <code>/WEB-INF/jsp/url-composta.jsp</code>.
    </body>
</html>
