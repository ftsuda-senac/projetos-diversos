<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Exemplo Upload de arquivo</title>
    </head>
    <body>
        <h1>Upload de arquivo</h1>
        <c:if test="${msg != null}">
            <p><c:out value="${msg}" /></p>
            <p>Acessar o arquivo através do link <a href="${urlAcessoUpload}" target="_blank">${urlAcessoUpload}</a></p>
        </c:if>   
        <form action="${pageContext.request.contextPath}/upload-arquivo"  method="post" enctype="multipart/form-data">
            <input type="file" name="arquivo" />
            <button type="submit">Enviar</button>
        </form>
        <hr>
        <h2>Instruções para uso no Windows e servidor Tomcat:</h2>
        <ul>
            <li>
                Criar o diretório <code>C:\uploads</code> onde os arquivos serão salvos após o upload.
                <ul>
                    <li>Se quiser alterar o diretório, alterar o valor no <code>diretorioUpload</code> dentro do arquivo <code>/WEB-INF/web.xml</code></li>
                </ul>
            </li>
            <li>
                Configurar o Tomcat para criar uma raíz de contexto para acessar o conteúdo do diretório <code>C:\uploads</code> através da Web.
                <ul>
                    <li>
                        Editar o arquivo server.xml do Tomcat: pelo Netbeans, na aba serviços acessar a opção "Servidores", escolher o Tomcat, clicar com o botão direito e selecionar opção "Editar server.xml".
                        <div><img src="resources/img/upload1.png" alt="Acessar opção para configurar o server.xml" style="width: 500px"/></div>
                    </li>
                    <li>
                        No arquivo, adicionar o trecho <code>&lt;Context docBase="C:/uploads" path="/teste-uploads" /&gt;</code> antes de fechar a tag <code>&lt;/Host&gt;</code> e salvar.
                        <div><img src="resources/img/upload2.png" alt="Incluir o trecho de código no server.xml" style="width: 500px"/></div>
                    </li>
                </ul>
            </li>
            <li>Reiniciar o Tomcat, acessar o link <a href="upload-arquivo">/upload-arquivo</a> e testar.</li>
        </ul>
        <div>
            Referências usadas:
            <ul>
                <li><a href="https://javaee.github.io/tutorial/servlets011.html#BABFGCHB" rel="nofollow" target="_blank">https://javaee.github.io/tutorial/servlets011.html#BABFGCHB</a></li>
                <li><a href="https://stackoverflow.com/a/2424824" rel="nofollow" target="_blank">https://stackoverflow.com/a/2424824</a></li>
                <li><a href="https://stackoverflow.com/a/18664715" rel="nofollow" target="_blank">https://stackoverflow.com/a/18664715</a></li>
                <li><a href="https://www.moreofless.co.uk/static-content-web-pages-images-tomcat-outside-war/" rel="nofollow" target="_blank">https://www.moreofless.co.uk/static-content-web-pages-images-tomcat-outside-war/</a></li>
            </ul>
        </div>
    </body>
</html>