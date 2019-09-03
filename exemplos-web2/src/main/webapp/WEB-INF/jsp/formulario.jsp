<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Produto - Inclusão</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Produto - Inclusão</h1>
        <div>
            <form action="formulario-servlet" method="post">
                <input type="hidden" name="escondido" value="xpto" />
                <div>
                    <label>Nome</label>
                    <div>
                        <input type="text" name="nome" maxlength="50" />
                    </div>
                    <c:if test="${not empty erroNome}">
                        <p style="background-color: lightcoral">
                            <c:out value="${erroNome}" />
                        </p>
                    </c:if>
                </div>
                <div>
                    <label>E-mail</label>
                    <div>
                        <input type="email" />
                    </div>
                </div>
                <div>
                    <label>Senha</label>
                    <div>
                        <input type="password" name="senha" />
                    </div>
                </div>
                <div>
                    <label>Descrição</label>
                    <div>
                        <textarea name="descricao"></textarea>
                    </div>
                    <c:if test="${not empty erroDescricao}">
                        <p style="background-color: lightcoral">
                            <c:out value="${erroDescricao}" />
                        </p>
                    </c:if>
                </div>
                <div>
                    <label>Quantidade</label>
                    <div>
                        <input type="text" name="quantidade" />
                    </div>
                </div>
                <div>
                    <label>Preço compra</label>
                    <div>
                        <input type="text" name="prcompra" />
                    </div>
                </div>
                <div>
                    <label>Preço venda</label>
                    <div>
                        <input type="text" name="prvenda" />
                    </div>
                </div>
                <fieldset>
                    <legend>Disponível</legend>
                    <input type="radio" name="disponivel" value="1" /> <label>Sim</label>
                    <input type="radio" name="disponivel" value="0" /> <label>Não</label>
                </fieldset>
                <fieldset>
                    <legend>Categorias</legend>
                    <input type="checkbox" name="categorias" value="1" /> <label>Categoria 1</label>
                    <input type="checkbox" name="categorias" value="2" /> <label>Categoria 2</label>
                    <input type="checkbox" name="categorias" value="3" /> <label>Categoria 3</label>
                    <input type="checkbox" name="categorias" value="4" /> <label>Categoria 4</label>
                    <input type="checkbox" name="categorias" value="5" /> <label>Categoria 5</label>
                    <c:if test="${not empty erroCategorias}">
                        <p style="background-color: lightcoral">
                            <c:out value="${erroCategorias}" />
                        </p>
                    </c:if>
                </fieldset>
                <div>
                    <select>
                        <option>Opção 1</option>
                        <option>Opção 2</option>
                        <option>Opção 3</option>
                    </select>
                </div>
                <button type="submit">Salvar</button>
                <button type="reset">Resetar</button>
            </form>

        </div>
    </body>
</html>