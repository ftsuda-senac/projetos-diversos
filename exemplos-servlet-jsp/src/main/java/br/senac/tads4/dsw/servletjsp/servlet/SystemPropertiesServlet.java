/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads4.dsw.servletjsp.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

/**
 *
 * @author fernando.tsuda
 */
@WebServlet(name = "SystemPropertiesServlet", urlPatterns = {"/system-properties"})
public class SystemPropertiesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<KeyValue> systemProperties = new ArrayList<>();
        systemProperties.add(new KeyValue("file.separator", System.getProperty("file.separator")));
        systemProperties.add(new KeyValue("java.class.path", System.getProperty("java.class.path")));
        systemProperties.add(new KeyValue("java.home", System.getProperty("java.home")));
        systemProperties.add(new KeyValue("java.vendor", System.getProperty("java.vendor")));
        systemProperties.add(new KeyValue("java.vendor.url", System.getProperty("java.vendor.url")));
        systemProperties.add(new KeyValue("java.version", System.getProperty("java.version")));
        systemProperties.add(new KeyValue("line.separator", StringEscapeUtils.escapeJava(System.getProperty("line.separator"))));
        systemProperties.add(new KeyValue("os.arch", System.getProperty("os.arch")));
        systemProperties.add(new KeyValue("os.name", System.getProperty("os.name")));
        systemProperties.add(new KeyValue("os.version", System.getProperty("os.version")));
        systemProperties.add(new KeyValue("path.separator", System.getProperty("path.separator")));
        systemProperties.add(new KeyValue("user.dir", System.getProperty("user.dir")));
        systemProperties.add(new KeyValue("user.home", System.getProperty("user.home")));
        systemProperties.add(new KeyValue("user.name", System.getProperty("user.name")));
        
        List<KeyValue> environmentVariables = new ArrayList<>();
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
             environmentVariables.add(new KeyValue(envName, env.get(envName)));
        }
        
        request.setAttribute("systemProperties", systemProperties);
        request.setAttribute("environmentVariables", environmentVariables);
        request.getRequestDispatcher("/WEB-INF/jsp/system-properties.jsp").forward(request, response);

    }

}
