/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.exemplosjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernando.tsuda
 */
public class ExemploDAO {

    private Connection obterConexao() throws ClassNotFoundException, SQLException {
        // 1) Declarar o driver JDBC de acordo com o Banco de dados usado
        Class.forName("org.h2.Driver");

        // 2) Abrir a conexão
        // O arquivo do banco de dados está localizado no diretório C:\Users\<NOME_USUARIO>\exemplobd.mv
        Connection conn = DriverManager.getConnection(
                "jdbc:h2:file:~/exemplobd;LOCK_TIMEOUT=10000;COLLATION=PORTUGUESE_BRAZIL",
                "sa",
                "");
        return conn;
    }

    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS exemplo (id INT PRIMARY KEY auto_increment, valor VARCHAR(255))";
        try (Connection conn = obterConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public List<String> listarPreJava7() {
        String sql = "SELECT valor FROM exemplo";
        List<String> resultados = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = obterConexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String valor = rs.getString("valor");
                resultados.add(valor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return resultados;
    }

    public List<String> listar() {
        String sql = "SELECT valor FROM exemplo";
        // CÓDIGO ABAIXO SOMENTE PARA JAVA 7 OU SUPERIOR
        List<String> resultados = new ArrayList<>();
        try (Connection conn = obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String valor = rs.getString("valor");
                resultados.add(valor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return resultados;
    }

    public int salvarComStatement(String valor) {
        String sql = "INSERT INTO exemplo (valor) VALUES ('" + valor + "')";
        int resultados = 0;
        try (Connection conn = obterConexao();
                Statement stmt = conn.createStatement()) {
            resultados = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return resultados;
    }

    public int salvarComPreparedStatement(String valor) {
        String sql = "INSERT INTO exemplo (valor) VALUES (?)";
        int resultados = 0;
        try (Connection conn = obterConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valor);
            resultados = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return resultados;
    }

    public int salvarRecuperandoId(String valor) {
        String sql = "INSERT INTO exemplo (valor) VALUES (?)";
        int resultados = 0;
        try (Connection conn = obterConexao()) {
            // DESLIGAR O AUTO COMMIT
            conn.setAutoCommit(false);

            // ADICIONAR O Statement.RETURN_GENERATED_KEYS NA CHAMADA DO MÉTODO
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, valor);
                resultados = stmt.executeUpdate();
                try (ResultSet chaves = stmt.getGeneratedKeys()) {

                    if (chaves.next()) {
                        int idGerado = chaves.getInt(1);

                        // USA O ID GERADO PARA DEMAIS OPERACOES
                    }

                }
            }
            // EFETIVA TODAS AS OPERAÇÕES REALIZADAS
            conn.commit();
        } catch (SQLException ex) {
            
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return resultados;
    }
}
