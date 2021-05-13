<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<style>
.msg-erro {
  border: 2px solid chocolate;
  padding: 10px;
  margin: 5px 0;
  background-color: moccasin;
  color: chocolate;
}
</style>
</head>
<body>
  <h1>Login</h1>
  <c:if test="${msgErro != null}">
    <div class="msg-erro">
      <c:out value="${msgErro}" />
    </div>
  </c:if>
  <div>
    <form method="post" action="${pageContext.request.contextPath}/login">
      <div>
        <label>Username</label>
        <input type="text" name="username" />
      </div>
      <div>
        <label>Senha</label>
        <input type="password" name="senha" />
      </div>
      <button type="submit">Enviar</button>
    </form>
  </div>
</body>
</html>
