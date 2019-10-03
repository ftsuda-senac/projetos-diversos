/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.servletjsp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
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
            // 1) OBTER CONEXÃO COM BD
            conn = ConnectionUtil.obterConexaoBD();

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
        // 1) OBTER CONEXÃO COM BD
        try (Connection conn = ConnectionUtil.obterConexaoBD();
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
        } catch (ClassNotFoundException e) {
            // TRATAR EXCEÇÃO
        }
        // AO SAIR DESTE BLOCO, O FECHAMENTO DA CONEXÃO ESTÁ IMPLICITO
        // RECURSO TRY-WITH-RESOURCES DO JAVA 7 OU SUPERIOR
    }

    public void executarSelectPorId(long id) {
        String sql = "SELECT ID, NOME, PRECO_VENDA FROM PRODUTO WHERE ID = ?";
        try (Connection conn = ConnectionUtil.obterConexaoBD();
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
        } catch (ClassNotFoundException e) {
            // TRATAR EXCEÇÃO
        }
    }

    public void incluirComTransacao(String nome, BigDecimal precoVenda) throws ClassNotFoundException, SQLException {
        String query = "INSERT INTO PRODUTO (NOME, PRECO_VENDA) VALUES (?, ?)";
        try (Connection conn = ConnectionUtil.obterConexaoBD()) {
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
                // O commit() EFETIVA TODAS AS OPERAÇÕES REALIZADAS NO BD
                conn.commit();
            } catch (SQLException e) {
                // CASO OCORRA ERRO, O rollback() DESFAZ TODAS AS OPERAÇÕES
                conn.rollback();
            }
        } catch (SQLException e) {
            // TRATAR EXCEÇÃO
        } catch (ClassNotFoundException e) {
            // TRATAR EXCEÇÃO
        }
    }
}
