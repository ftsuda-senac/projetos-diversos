/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.pi3.exemplosjdbc;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

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
  /*
  public final Connection obterConexao() throws SQLException {
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
        "jdbc:h2:file:~/exemplobd;LOCK_TIMEOUT=10000;COLLATION=PORTUGUESE_BRAZIL", "sa", "");
    return conn;
  }
  */
  
  public final Connection obterConexao() throws SQLException {

    try (FileReader propReader = new FileReader("/home/brainyit/conexao-bd.properties")) {
      Properties bdProps = new Properties();
      bdProps.load(propReader); //
      // 1) Declarar o driver JDBC de acordo com o Banco de dados usado
      Class.forName("org.h2.Driver");

      // 2) Abrir a conexão usando as propriedades configuradas no arquivo
      return DriverManager.getConnection(bdProps.getProperty("url"), bdProps);
    } catch (IOException e) {
      // IMPLEMENTAR TRATAMENTO
      throw new SQLException("Arquivo de conexao-bd.properties não encontrado", e);
    } catch (ClassNotFoundException e) {
      // IMPLEMENTAR TRATAMENTO
      throw new SQLException("Driver do MySQL não encontrado", e);
    }
  }

  public static DataSource retrieveDS() {
    JdbcDataSource ds = new JdbcDataSource();
    ds.setURL("jdbc:h2:file:~/exemplobd;LOCK_TIMEOUT=10000;COLLATION=PORTUGUESE_BRAZIL");
    ds.setUser("sa");
    ds.setPassword("");
    return ds;
    // Context ctx = new InitialContext();
    // ctx.bind("jdbc/dsName", ds);
  }
}
