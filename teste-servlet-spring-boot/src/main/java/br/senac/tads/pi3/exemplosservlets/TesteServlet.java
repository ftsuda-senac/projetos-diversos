package br.senac.tads.pi3.exemplosservlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TesteServlet", urlPatterns = {"/teste-servlet"})
public class TesteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
//		try (PrintWriter out = resp.getWriter()) {
//			out.write("testeServlet");
//		}
        req.setAttribute("msg", "Mensagem gerada no Servlet");
        req.getRequestDispatcher("/WEB-INF/jsp/teste.jsp").forward(req, resp);
    }

}
