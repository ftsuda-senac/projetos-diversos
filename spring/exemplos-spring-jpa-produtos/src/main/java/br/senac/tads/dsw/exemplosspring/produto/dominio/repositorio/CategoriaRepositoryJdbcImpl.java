package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoriaRepositoryJdbcImpl implements CategoriaRepository {

    private static final Logger log = LoggerFactory.getLogger(CategoriaRepositoryJdbcImpl.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Categoria> findAll() {
        String sql = "SELECT ID, NOME FROM CATEGORIA";
        List<Categoria> categorias = new ArrayList<>();
        try ( Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getInt("ID"));
                cat.setNome(rs.getString("NOME"));
                categorias.add(cat);
            }
        } catch (SQLException ex) {
            log.error("SQL error", ex);
        }
        return categorias;
    }

    @Override
    public Optional<Categoria> findById(Integer id) {
        String sql = "SELECT NOME FROM CATEGORIA WHERE ID=?";
        try ( Connection conn = dataSource.getConnection(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try ( ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setId(id); // Informado no parametro
                    cat.setNome(rs.getString("NOME"));
                    return Optional.of(cat);
                }
            }
        } catch (SQLException ex) {
            log.error("SQL error", ex);
        }
        return Optional.empty();
    }

    @Override
    public Categoria save(Categoria cat) {
        if (cat.getId() != null) {
            update(cat);
        } else {
            insert(cat);
        }
        return cat;
    }

    private void insert(Categoria cat) {
        String sql = "INSERT INTO CATEGORIA (NOME) VALUES (?)";
        int resultados = 0;
        try ( Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try ( PreparedStatement stmt
                    = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, cat.getNome());
                resultados = stmt.executeUpdate();
                if (resultados > 0) {
                    try ( ResultSet chaves = stmt.getGeneratedKeys()) {
                        if (chaves != null && chaves.next()) {
                            cat.setId(chaves.getInt(1));
                        }
                    }
                }
            }
            if (resultados > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            log.error("SQL error", ex);
        }
    }

    private void update(Categoria cat) {
        String sql = "UPDATE CATEGORIA SET NOME=? WHERE ID=?";
        int resultados = 0;
        try ( Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, cat.getNome());
                stmt.setInt(2, cat.getId());
                resultados = stmt.executeUpdate();
            }
            if (resultados > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            log.error("SQL error", ex);
        }
    }

}
