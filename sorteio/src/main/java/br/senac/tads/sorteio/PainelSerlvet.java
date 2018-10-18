package br.senac.tads.sorteio;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "PainelSerlvet", urlPatterns = {"/painel"})
public class PainelSerlvet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException {
    HttpSession sessao = request.getSession();
    if (sessao.getAttribute("pessoas") == null) {
      response.sendRedirect(request.getContextPath() + "/painel-entrada.jsp");
      return;
    }
    request.getRequestDispatcher("WEB-INF/jsp/painel.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException {
    String itensStr = request.getParameter("itens");
    String aleatorioStr = request.getParameter("aleatorio");
    List<String> pessoas = Arrays.asList(itensStr.split("\r\n"));
    
    // Limpa a lista para remover entradas em branco
    List<String> entradaLimpa = new ArrayList<>();
    for (String pessoa : pessoas) {
      if (StringUtils.isNotBlank(pessoa)) {
	entradaLimpa.add(pessoa);
      }
    }
    List<Integer> indices = new ArrayList<>();
    List<String> apresentacao = new ArrayList<>();

    SecureRandom random = new SecureRandom();

    if ("1".equals(aleatorioStr)) {
      for (int i = 0; i < entradaLimpa.size(); i++) {
	while (true) {
	  int indice = random.nextInt(entradaLimpa.size());
	  if (!indices.contains(indice)) {
	    indices.add(indice);
	    apresentacao.add(entradaLimpa.get(indice));
	    break;
	  }
	}
      }
    } else {
      apresentacao.addAll(entradaLimpa);
    }
    HttpSession sessao = request.getSession();
    sessao.setAttribute("pessoas", apresentacao);
    response.sendRedirect(request.getContextPath() + "/painel");
  }

}
