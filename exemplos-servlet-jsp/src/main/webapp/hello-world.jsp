<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>JSP Page</title>
  </head>
  <body>
    <h1>Hello World!</h1>
    <hr>
    <h2>Trecho do código da classe <i><code>hello_002dworld_jsp.java</code></i> gerado a partir do JSP</h2>
    <p>Normalmente está localizado no diretório <code>{TOMCAT_HOME}/work/Catalina/localhost/[CONTEXT-PATH]/org/apache/jsp</code>
    <div>
      <code>
        
        public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)<br>
      throws java.io.IOException, javax.servlet.ServletException {<br>
<br>
    final java.lang.String _jspx_method = request.getMethod();<br>
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {<br>
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");<br>
      return;<br>
    }<br>
<br>
    final javax.servlet.jsp.PageContext pageContext;<br>
    javax.servlet.http.HttpSession session = null;<br>
    final javax.servlet.ServletContext application;<br>
    final javax.servlet.ServletConfig config;<br>
    javax.servlet.jsp.JspWriter out = null;<br>
    final java.lang.Object page = this;<br>
    javax.servlet.jsp.JspWriter _jspx_out = null;<br>
    javax.servlet.jsp.PageContext _jspx_page_context = null;<br><br>
        try {<br>
      response.setContentType("text/html;charset=UTF-8");<br>
      pageContext = _jspxFactory.getPageContext(this, request, response,<br>
      			null, true, 8192, true);<br>
      _jspx_page_context = pageContext;<br>
      application = pageContext.getServletContext();<br>
      config = pageContext.getServletConfig();<br>
      session = pageContext.getSession();<br>
      out = pageContext.getOut();<br>
      _jspx_out = out;<br><br>

      out.write("\n");<br>
      out.write("&lt;!DOCTYPE html&gt;\n");<br>
      out.write("&lt;html&gt;\n");<br>
      out.write("  &lt;head&gt;\n");<br>
      out.write("    &lt;meta charset=\"UTF-8\"&gt;\n");<br>
      out.write("    &lt;title&gt;JSP Page&lt;/title&gt;\n");<br>
      out.write("  &lt;/head&gt;\n");<br>
      out.write("  &lt;body&gt;\n");<br>
      out.write("    &lt;h1&gt;Hello World!&lt;/h1&gt;\n");<br>
      out.write("  &lt;/body&gt;\n");<br>
      out.write("&lt;/html&gt;\n");<br>
    } catch (java.lang.Throwable t) {<br>
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){<br>
        out = _jspx_out;<br>
        if (out != null && out.getBufferSize() != 0)<br>
          try {<br>
            if (response.isCommitted()) {<br>
              out.flush();<br>
            } else {<br>
              out.clearBuffer();<br>
            }<br>
          } catch (java.io.IOException e) {}<br>
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);<br>
        else throw new ServletException(t);<br>
      }<br>
    } finally {<br>
      _jspxFactory.releasePageContext(_jspx_page_context);<br>
      }<br>
    }<br>
      </code>
    </div> 
  </body>
</html>
