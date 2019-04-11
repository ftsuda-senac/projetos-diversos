/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads4.dsw.servletjsp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author fernando.tsuda
 */
public class ConnectionUtil {

    /**
     * Função responsável pela obtenção de uma conexão com BD.
     * @return objeto {@link Connection}
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static final Connection obterConexaoBD() throws SQLException, ClassNotFoundException {
        // 1A) Declarar o driver JDBC de acordo com o Banco de dados usado
        // Neste caso, conectando com MySQL 8.x
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 1B) Abrir a conexão
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/produtobd?"
                        + "useUnicode=yes&"
                        + "characterEncoding=UTF-8&"
                        + "useTimezone=true&"
                        + "serverTimezone=UTC",
                "USUARIO", // Usuário de conexão
                "SENHA"); // Senha
        return conn;
    }

}
