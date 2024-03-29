/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.servletjsp;

/**
 *
 * @author fernando.tsuda
 */
import java.util.Map;
import java.util.Properties;
import org.apache.commons.text.StringEscapeUtils;

public class SystemPropertiesMain {

    /*
     * https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
     * https://docs.oracle.com/javase/tutorial/essential/environment/env.html
     */
    public static void main(String... args) {
        System.out.println("======== System properties ==========");
        System.out.println(
                "    Novos parâmetros podem ser adicionandos com a flag -Dparam=valor ao iniciar a aplicação");
        Properties systemProperties = System.getProperties();
        for (String key : systemProperties.stringPropertyNames()) {
            if ("line.separator".equals(key)) {
                System.out.println(
                        key + ": " + StringEscapeUtils.escapeJava(System.getProperty(key)));
            } else {
                System.out.println(key + ": " + System.getProperty(key));
            }
        }
        /*
         * System.out.println("file.separator: " + System.getProperty("file.separator"));
         * System.out.println("java.class.path: " + System.getProperty("java.class.path"));
         * System.out.println("java.home: " + System.getProperty("java.home"));
         * System.out.println("java.vendor: " + System.getProperty("java.vendor"));
         * System.out.println("java.vendor.url: " + System.getProperty("java.vendor.url"));
         * System.out.println("java.version: " + System.getProperty("java.version"));
         * System.out.println("line.separator: " +
         * StringEscapeUtils.escapeJava(System.getProperty("line.separator")));
         * System.out.println("os.arch: " + System.getProperty("os.arch"));
         * System.out.println("os.name: " + System.getProperty("os.name"));
         * System.out.println("os.version: " + System.getProperty("os.version"));
         * System.out.println("path.separator: " + System.getProperty("path.separator"));
         * System.out.println("user.dir: " + System.getProperty("user.dir"));
         * System.out.println("user.home: " + System.getProperty("user.home"));
         * System.out.println("user.name: " + System.getProperty("user.name"));
         */

        System.out.println(System.lineSeparator() + "======== Environment variables ==========");
        System.out
                .println("    Variáveis de ambiente podem ser configuradas no sistema operacional");
        System.out.println(
                "    Windows => SET VARIAVEL=VALOR (para verificar: ECHO %VARIAVEL% ) - SEPARADOR: ;");
        System.out.println(
                "    Linux => export VARIAVEL=VALOR (para verificar: echo $VARIAVEL ) - SEPARADOR: :");

        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n", envName, env.get(envName));
        }
    }

}
