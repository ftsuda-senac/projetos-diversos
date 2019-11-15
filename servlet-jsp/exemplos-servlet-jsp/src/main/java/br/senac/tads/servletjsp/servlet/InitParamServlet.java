/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.servletjsp.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fernando.tsuda
 */
// https://www.oracle.com/technetwork/tutorials/tutorials-1662707.html
// https://docs.oracle.com/cd/E13222_01/wls/docs81/webapp/web_xml.html
// https://docs.oracle.com/cd/E13222_01/wls/docs81/webapp/components.html
// https://stackoverflow.com/questions/13196868/servlet-spec-context-param-vs-env-entry-in-web-xml
@WebServlet(name = "InitParamServlet", urlPatterns = {"/init-param"},
        initParams = {
            @WebInitParam(name = "initParam1", value = "abcd"),
            @WebInitParam(name = "initParam2", value = "xpto")
        })
public class InitParamServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(InitParamServlet.class.getName());

    @Resource(name = "envParam1")
    private String envEntry1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String initParam1 = getInitParameter("initParam1");
        String initParam2 = getInitParameter("initParam2");

        String ctxParam1 = getServletContext().getInitParameter("contextParam1");
        String ctxParam2 = getServletContext().getInitParameter("contextParam2");

        String envEntry2 = "Não encontrado";
        try {
            Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
            envEntry2 = (String) envCtx.lookup("envParam2");
        } catch (NamingException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        request.setAttribute("xparam1", initParam1);
        request.setAttribute("xparam2", initParam2);
        request.setAttribute("ctxparam1", ctxParam1);
        request.setAttribute("ctxparam2", ctxParam2);
        request.setAttribute("enventry1", envEntry1);
        request.setAttribute("enventry2", envEntry2);

        request.getRequestDispatcher("/WEB-INF/jsp/init-param.jsp").forward(request, response);
    }

}
