/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads4.dsw.servletjsp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author fernando.tsuda
 */
public class ProdutoDAO {

    public void executarSelectAll() {

        String sql = "SELECT ID, NOME, PRECO_VENDA FROM PRODUTO";
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet resultados = null;

        try {
            // 1) ABRIR CONEXÃO COM BD
            // 1A) Declarar o driver JDBC de acordo com o Banco de dados usado
            Class.forName("com.mysql.jdbc.Driver");

            // 1B) Abrir a conexão
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/produtobd",
                    "USUARIO", // Usuário de conexão
                    "SENHA"); // Senha

            // 2) EXECUTAR O COMANDO NO BANCO DE DADOS
            // NESTE CASO UM SELECT DEFINIDO NA VARIAVEL sql ACIMA
            stmt = conn.prepareStatement(sql);
            resultados = stmt.executeQuery();
            while (resultados.next()) {
                // TRATAR RESULTADOS
                long id = resultados.getLong("ID");
                String nome = resultados.getString("NOME");
            }
        } catch (ClassNotFoundException ex) {
            // TRATAR EXCEÇÃO
        } catch (SQLException ex) {
            // TRATAR EXCEÇÃO
        } finally {
            // 3) FECHAR CONEXÃO
            if (resultados != null) {
                try {
                    resultados.close();
                } catch (SQLException ex) {
                    /* TRATAR EXCEÇÃO */ }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    /* TRATAR EXCEÇÃO */ }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    /* TRATAR EXCEÇÃO */ }
            }
        }
    }

    // CÓDIGO ABAIXO SOMENTE PARA JAVA 7 OU SUPERIOR
    public void executarSelectAllJava7() {

        String sql = "SELECT ID, NOME, PRECO_VENDA FROM PRODUTO";
        try {
            // 1) ABRIR CONEXÃO COM BD
            // 1A) Declarar o driver JDBC de acordo com o Banco de dados usado
            Class.forName("com.mysql.jdbc.Driver");
            // 1B) Abrir a conexão
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/produtobd",
                    "USUARIO", // Usuário de conexão
                    "SENHA"); // Senha;

                    // EXECUTA O COMANDO NO BANCO DE DADOS
                    // NESTE CASO UM SELECT DEFINIDO NA VARIAVEL sql ACIMA
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet resultados = stmt.executeQuery()) {

                while (resultados.next()) {
                    // TRATAR RESULTADOS
                    long id = resultados.getLong("ID");
                    String nome = resultados.getString("NOME");
                }
            } catch (SQLException ex) {
                // TRATAR EXCEÇÃO
            }
            // AO SAIR DESTE BLOCO, O FECHAMENTO DA CONEXÃO ESTÁ IMPLICITO
            // RECURSO TRY-WITH-RESOURCES DO JAVA 7 OU SUPERIOR
        } catch (ClassNotFoundException e) {
            // TRATAR EXCEÇÃO
        }
    }

    public void executarSelectPorId(long id) {
        String sql = "SELECT ID, NOME, PRECO_VENDA FROM PRODUTO WHERE ID = ?";
        try {
            // 1) ABRIR CONEXÃO COM BD
            // 1A) Declarar o driver JDBC de acordo com o Banco de dados usado
            Class.forName("com.mysql.jdbc.Driver");
            // 1B) Abrir a conexão
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/produtobd",
                    "USUARIO", // Usuário de conexão
                    "SENHA"); // Senha;
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                try (ResultSet resultados = stmt.executeQuery()) {
                    while (resultados.next()) {
                        // TRATAR RESULTADOS
                        long idRec = resultados.getLong("ID");
                        String nome = resultados.getString("NOME");
                    }
                }
            } catch (SQLException ex) {
                // TRATAR EXCEÇÃO
            }
        } catch (ClassNotFoundException e) {
            // TRATAR EXCEÇÃO
        }
    }

    public void incluir(String nome, BigDecimal precoVenda) throws ClassNotFoundException, SQLException {

        String query = "INSERT INTO PRODUTO (NOME, PRECO_VENDA) VALUES (?, ?)";
        try {
            // 1) Declarar o driver JDBC de acordo com o Banco de dados usado
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/produtobd",
                    "USUARIO", // Usuário de conexão
                    "SENHA")) // Senha
            {
                // DESLIGAR O AUTO COMMIT
                conn.setAutoCommit(false);

                // ADICIONAR O Statement.RETURN_GENERATED_KEYS NA CHAMADA DO MÉTODO
                try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, nome);
                    stmt.setBigDecimal(2, precoVenda);
                    stmt.executeUpdate();

                    try (ResultSet chaves = stmt.getGeneratedKeys()) {
                        if (chaves.next()) {
                            long idNovo = chaves.getLong(1);
                            // USAR O ID RECUPERADO PARA SALVAR INFORMACOES ASSOCIADAS

                        }
                    }
                    // EFETIVA TODAS AS OPERAÇÕES REALIZADAS
                    conn.commit();
                } catch (SQLException e) {
                    // CASO OCORRA ERRO, DESFAZ TODAS AS OPERAÇÕES
                    conn.rollback();
                }
            } catch (SQLException e) {
                // TRATAR EXCEÇÃO
            }
        } catch (ClassNotFoundException e) {
            // TRATAR EXCEÇÃO
        }

    }
}
