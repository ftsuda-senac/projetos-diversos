package br.senac.tads.geradordadospessoais;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TypewriterEffect {

    public static void main(String[] args) {
        String str = "Hello world Hello world Hello world Hello world Hello world Hello world Hello world";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int i;
        boolean cancelled = false;
        for (i = 0; i < str.length() && !cancelled; i++) {
            System.out.print(str.charAt(i));
            try {
                Thread.sleep(200);// 0.3s pause between characters
            } catch (InterruptedException ex) {
                System.err.println("ATENÇÃO - ESPERA INTERROMPIDA: " + ex.getMessage());
            }
            try {
                if (reader.ready()) {
                    char input = (char) reader.read();
                    if (input == 13) { // Enter
                        cancelled = true;
                    }
                }
            } catch (IOException ex) {
                System.err.println("ERRO IO: " + ex.getMessage());
            }
        }
        System.out.println(str.substring(i));
    }
}
