/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.exemplosjdbc;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author fernando.tsuda
 */
public class Principal {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        ExemploDAO dao = new ExemploDAO();
        dao.criarTabela();
        do {
            System.out.println("********** DIGITE UMA OPÇÃO **********");
            System.out.println("(1) Incluir com statement");
            System.out.println("(2) Incluir com statement");
            System.out.println("(3) Incluir com prepared statement");
            System.out.println("(9) SAIR");
            System.out.print("Opção: ");

            String opcaoStr = entrada.nextLine();
            try {
                int opcao = Integer.parseInt(opcaoStr);
                String valor;
                switch (opcao) {
                    case 1:
                        // Pula o switch para listar os dados.
                        break;
                    case 2:
                        System.out.println("Digite o valor a ser incluido usando Statement: ");
                        valor = entrada.nextLine();
                        dao.salvarComStatement(valor);
                        break;
                    case 3:
                        System.out.println("Digite o valor a ser incluido usando PreparedStatement: ");
                        valor = entrada.nextLine();
                        dao.salvarComPreparedStatement(valor);
                        break;
                    case 9:
                        System.exit(0);
                    default:
                        System.out.println("OPÇÃO INVÁLIDA.");
                }
            } catch (NumberFormatException e) {
                System.out.println("OPÇÃO INVÁLIDA.");
            }
            List<String> valoresSalvos = dao.listar();
            if (valoresSalvos.isEmpty()) {
                System.out.println("===== BANCO DE DADOS VAZIO ===== ");
            } else {
                System.out.println("===== VALORES NO BANCO =====");
                for (String val : valoresSalvos) {
                    System.out.println(val);
                }
            }
        } while (true);
    }

}
