package br.senac.tads.pi3.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MvcServlet", urlPatterns = { "/mvc" })
public class MvcServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setAttribute("msg", "Texto gerado no Servlet");
		request.getRequestDispatcher("/WEB-INF/jsp/mvc.jsp").forward(request, response);
	}

}
