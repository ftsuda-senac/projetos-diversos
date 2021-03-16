/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.servletjsp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "UploadArquivoServlet", urlPatterns = {"/upload-arquivo"},
        initParams = {@WebInitParam(name = "contextoAcessoUpload", value = "/teste-uploads")})
@MultipartConfig(maxFileSize = 20848820) // 5MB == 20848820 bytes == 5*1024*1024
public class UploadArquivoServlet extends HttpServlet {

    // private File diretorio;
    //
    // @Override
    // public void init(ServletConfig config) throws ServletException {
    // super.init(config);
    // String path = config.getInitParameter("diretorio");
    // diretorio = new File(path);
    // diretorio.mkdirs();
    // }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp/upload-arquivo.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    // Obtido em https://stackoverflow.com/a/2424824
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Part filePart = request.getPart("arquivo"); // Retrieves <input type="file" name="arquivo">

        // Recupara o valor configurado no context-param (web.xml)
        String diretorio = getServletContext().getInitParameter("diretorioUpload");

        String nomeArquivo = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE
                                                                                                  // fix.
        InputStream conteudoArquivo = filePart.getInputStream();

        // **** TRATAR O InputStream conforme necessidade
        // Pega os bytes e salva no disco
        Path destino = Paths.get(diretorio + nomeArquivo);
        Files.copy(conteudoArquivo, destino);

        // Mensagens e feedback para usuário:
        request.setAttribute("msg", "Arquivo carregado com sucesso.");

        // Recupera contexto configurado no @WebInitParam acima
        String contextoAcessoUpload = getInitParameter("contextoAcessoUpload");
        String urlAcessoUpload = contextoAcessoUpload + "/" + nomeArquivo;
        request.setAttribute("urlAcessoUpload", urlAcessoUpload);

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp/upload-arquivo.jsp");
        dispatcher.forward(request, response);
    }

}
