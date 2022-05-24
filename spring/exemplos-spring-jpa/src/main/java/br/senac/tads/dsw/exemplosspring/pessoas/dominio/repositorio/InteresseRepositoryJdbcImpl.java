package br.senac.tads.dsw.exemplosspring.pessoas.dominio.repositorio;

import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.Interesse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// @Repository
public class InteresseRepositoryJdbcImpl implements InteresseRepository {

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Interesse> findAll() {
        String sql = "SELECT id, nome FROM interesse";
        List<Interesse> resultados = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Interesse interesse = new Interesse();
                interesse.setId(rs.getInt("id"));
                interesse.setNome(rs.getString("nome"));
                resultados.add(interesse);
            }
        } catch (SQLException ex) {
            // TODO: Tratar exceção adequadamente
            ex.printStackTrace();
        }
        return resultados;
    }

    @Override
    public Optional<Interesse> findById(Integer id) {
        String sql = "SELECT id, nome FROM interesse WHERE id = ?";
        Interesse interesse = null;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    interesse = new Interesse();
                    interesse.setId(rs.getInt("id"));
                    interesse.setNome(rs.getString("nome"));
                }
            }
        } catch (SQLException ex) {
            // TODO: Tratar exceção adequadamente
            ex.printStackTrace();
        }
        return Optional.ofNullable(interesse);
    }

    @Override
    public Interesse save(Interesse interesse) {
        if (interesse.getId() == null) {
            insert(interesse);
        } else {
            update(interesse);
        }
        return interesse;
    }

    private void insert(Interesse interesse) {
        String sql = "INSERT INTO interesse (nome) VALUES (?)";
        int qtResultados = 0;
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt
                    = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, interesse.getNome());
                qtResultados = stmt.executeUpdate();
                if (qtResultados > 0) {
                    try (ResultSet chaves = stmt.getGeneratedKeys()) {
                        if (chaves != null && chaves.next()) {
                            interesse.setId(chaves.getInt(1));
                        }
                    }
                }
            }
            if (qtResultados > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            // TODO: Tratar exceção adequadamente
            ex.printStackTrace();
        }
    }

    private void update(Interesse interesse) {
        String sql = "UPDATE interesse SET nome=? WHERE id=?";
        int qtResultados = 0;
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt
                    = conn.prepareStatement(sql)) {
                stmt.setString(1, interesse.getNome());
                stmt.setInt(2, interesse.getId());
                qtResultados = stmt.executeUpdate();
            }
            if (qtResultados > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            // TODO: Tratar exceção adequadamente
            ex.printStackTrace();
        }
    }

}
