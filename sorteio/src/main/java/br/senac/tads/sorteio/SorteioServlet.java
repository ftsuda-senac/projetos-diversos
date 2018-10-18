/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.sorteio;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Fernando
 */
@WebServlet(name = "SorteioServlet", urlPatterns = {"/sorteio"})
public class SorteioServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException {
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
	  throws ServletException, IOException {
    String itensStr = request.getParameter("itens");
    String aleatorioStr = request.getParameter("aleatorio");
    String avaliadorStr = request.getParameter("avaliador");
    List<String> grupos = Arrays.asList(itensStr.split("\r\n"));

    // Limpa a lista para remover entradas em branco
    List<String> entradaLimpa = new ArrayList<>();
    for (String grupo : grupos) {
      if (StringUtils.isNotBlank(grupo)) {
	entradaLimpa.add(grupo);
      }
    }

    List<Integer> indices;
    List<String> apresentacao = new ArrayList<>();
    List<Apresentacao> apresentacaoAvaliacao = new ArrayList<>();

    SecureRandom random = new SecureRandom();

    if ("1".equals(aleatorioStr)) {
      indices = new ArrayList<>();
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

    if ("1".equals(avaliadorStr) && apresentacao.size() > 1) {
      indices = new ArrayList<>();
      for (int i = 0; i < apresentacao.size(); i++) {
	while (true) {
	  int indice = random.nextInt(apresentacao.size());
	  if (indice != i && !indices.contains(indice)) {
	    indices.add(indice);
	    apresentacaoAvaliacao.add(new Apresentacao(apresentacao.get(i), apresentacao.get(indice)));
	    break;
	  }
	}
      }
      request.setAttribute("apresentacoes", apresentacaoAvaliacao);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/resultadoAvaliador.jsp");
      dispatcher.forward(request, response);
    } else {
      request.setAttribute("apresentacoes", apresentacao);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/resultado.jsp");
      dispatcher.forward(request, response);
    }

  }
}
