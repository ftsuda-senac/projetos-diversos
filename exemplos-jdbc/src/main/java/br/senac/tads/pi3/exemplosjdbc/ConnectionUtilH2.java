/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.exemplosjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author fernando.tsuda
 */
public class ConnectionUtilH2 {

    /**
     * Função responsável pela obtenção de uma conexão com BD H2
     * 
     * @return objeto {@link Connection}
     * @throws SQLException 
     */
    public static final Connection obterConexao() throws SQLException {
        // 1) Declarar o driver JDBC de acordo com o Banco de dados usado
        // Neste caso, está sendo usado o banco H2 (Banco SGBD embutido ou embedded)
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException(ex);
        }

        // 2) Abrir a conexão
        // O arquivo do banco de dados está localizado no diretório C:\Users\<NOME_USUARIO>\exemplobd.mv
        Connection conn = DriverManager.getConnection(
                "jdbc:h2:file:~/exemplobd;LOCK_TIMEOUT=10000;COLLATION=PORTUGUESE_BRAZIL",
                "sa",
                "");
        return conn;
    }
}
