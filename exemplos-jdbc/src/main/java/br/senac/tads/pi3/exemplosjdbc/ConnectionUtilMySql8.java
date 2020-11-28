package br.senac.tads.pi3.exemplosjdbc;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/* Conteúdo do arquivo conexao-bd.properties
url=jdbc:mysql://localhost:3306/agendabd?useUnicode=yes&characterEncoding=UTF-8&useTimezone=true&serverTimezone=UTC
user=USUARIO
password=SENHA
 */
public class ConnectionUtilMySql8 {

  public final Connection obterConexao() throws SQLException {

    try (FileReader propReader = new FileReader("[CAMINHO-COMPLETO]/conexao-bd.properties")) {
      Properties bdProps = new Properties();
      bdProps.load(propReader);

      // 1) Declarar o driver JDBC de acordo com o Banco de dados usado
      Class.forName("com.mysql.cj.jdbc.Driver");

      // 2) Abrir a conexão usando as propriedades configuradas no arquivo
      Connection conn = DriverManager.getConnection(bdProps.getProperty("url"), bdProps);
      return conn;
    } catch (IOException e) {
      // IMPLEMENTAR TRATAMENTO
      throw new SQLException("Arquivo de conexao-bd.properties não encontrado", e);
    } catch (ClassNotFoundException e) {
      // IMPLEMENTAR TRATAMENTO
      throw new SQLException("Driver do MySQL não encontrado", e);
    }
  }

}
