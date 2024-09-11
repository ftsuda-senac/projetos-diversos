/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.senac.tads.dsw;

import java.time.LocalDateTime;

public class AppConsoleSimple {

    public String gerarHtml() {
        return "<!DOCTYPE html>" + System.lineSeparator()
                + "<html>" + System.lineSeparator()
                + "<head>" + System.lineSeparator()
                + "\t<meta charset=\"UTF-8\" />" + System.lineSeparator()
                + "\t<title>Exemplo HTML</title>" + System.lineSeparator()
                + "</head>" + System.lineSeparator()
                + "<body>" + System.lineSeparator()
                + "\t<h1>Exemplo HTML gerado por classe Java</h1>" + System.lineSeparator()
                + "\t<p>Data e hora: " + LocalDateTime.now() + "</p>" + System.lineSeparator()
                + "</body>" + System.lineSeparator()
                + "</html>";
    }

    public String gerarJson() {
        return "{" + System.lineSeparator()
                + "\t\"texto\" : \"Exemplo JSON gerado por classe Java\"," + System.lineSeparator()
                + "\t\"dataHora\" : \"" + LocalDateTime.now() + "\"" + System.lineSeparator()
                + "}";
    }

    public static void main(String[] args) {
        AppConsole app = new AppConsole();
        String resposta = app.gerarHtml();
        System.out.println(resposta);

    }

}
