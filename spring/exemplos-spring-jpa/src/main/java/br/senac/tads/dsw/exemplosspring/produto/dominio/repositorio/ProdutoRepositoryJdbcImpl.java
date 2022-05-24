package br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.ImagemProduto;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;

public class ProdutoRepositoryJdbcImpl implements ProdutoRepository {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Produto> findAll(int offset, int quantidade) {
        String sqlProduto =
                "SELECT ID, NOME, DESCRICAO, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE, DISPONIVEL, DT_CADASTRO FROM PRODUTO";
        String sqlImagensProduto =
                "SELECT ID, NOME_ARQUIVO, LEGENDA FROM IMAGEM_PRODUTO WHERE PRODUTO_ID = ?";
        String sqlProdutoCategoria =
                "SELECT ID_CATEGORIA FROM PRODUTO_CATEGORIA WHERE PRODUTO_ID = ?";

        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlProduto);
                ResultSet rs = stmt.executeQuery()) {

            if (rs != null) {
                while (rs.next()) {
                    Produto p = new Produto();
                    p.setId(rs.getInt("ID"));
                    p.setNome(rs.getString("NOME"));
                    p.setDescricao(rs.getString("DESCRICAO"));
                    p.setPrecoCompra(rs.getBigDecimal("PRECO_COMPRA"));
                    p.setPrecoVenda(rs.getBigDecimal("PRECO_VENDA"));
                    p.setQuantidade(rs.getInt("QUANTIDADE"));
                    p.setDisponivel(rs.getBoolean("DISPONIVEL"));
                    p.setDtCadastro(rs.getTimestamp("DT_CADASTRO").toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime());

                    // RECUPERA IMAGENS
                    Set<ImagemProduto> imagens = new LinkedHashSet<>();
                    try (PreparedStatement stmtImagem = conn.prepareStatement(sqlImagensProduto)) {
                        stmtImagem.setLong(1, p.getId());
                        try (ResultSet rsImagem = stmtImagem.executeQuery()) {
                            while (rsImagem.next()) {
                                imagens.add(new ImagemProduto(rsImagem.getLong("ID"),
                                        rsImagem.getString("NOME_ARQUIVO"),
                                        rsImagem.getString("LEGENDA")));
                            }
                        }
                    }
                    p.setImagens(imagens);

                    // RECUPERA CATEGORIAS
                    List<Categoria> todasCategorias = categoriaRepository.findAll();
                    Map<Integer, Categoria> mapCategorias = new HashMap<>();
                    for (Categoria cat : todasCategorias) {
                        mapCategorias.put(cat.getId(), cat);
                    }
                    Set<Categoria> categorias = new LinkedHashSet<>();
                    try (PreparedStatement stmtCat = conn.prepareStatement(sqlProdutoCategoria)) {
                        stmtCat.setLong(1, p.getId());
                        try (ResultSet rsCat = stmtCat.executeQuery()) {
                            while (rsCat.next()) {
                                categorias.add(mapCategorias.get(rsCat.getInt("CATEGORIA_ID")));
                            }
                        }
                    }
                    p.setCategorias(categorias);
                    produtos.add(p);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return produtos;
    }

    @Override
    public List<Produto> findByCategoria(List<Integer> idsCat, int offset, int quantidade) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Produto findById(Integer id) {
        String sqlProduto =
                "SELECT ID, NOME, DESCRICAO, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE, DISPONIVEL, DT_CADASTRO FROM PRODUTO WHERE ID = ?";
        String sqlImagensProduto =
                "SELECT ID, NOME_ARQUIVO, LEGENDA FROM IMAGEM_PRODUTO WHERE PRODUTO_ID = ?";
        String sqlProdutoCategoria =
                "SELECT ID_CATEGORIA FROM PRODUTO_CATEGORIA WHERE PRODUTO_ID = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlProduto)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null) {
                    while (rs.next()) {
                        Produto p = new Produto();
                        p.setId(id);
                        p.setNome(rs.getString("NOME"));
                        p.setDescricao(rs.getString("DESCRICAO"));
                        p.setPrecoCompra(rs.getBigDecimal("PRECO_COMPRA"));
                        p.setPrecoVenda(rs.getBigDecimal("PRECO_VENDA"));
                        p.setQuantidade(rs.getInt("QUANTIDADE"));
                        p.setDisponivel(rs.getBoolean("DISPONIVEL"));
                        p.setDtCadastro(rs.getTimestamp("DT_CADASTRO").toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDateTime());

                        // RECUPERA IMAGENS
                        Set<ImagemProduto> imagens = new LinkedHashSet<>();
                        try (PreparedStatement stmtImagem =
                                conn.prepareStatement(sqlImagensProduto)) {
                            stmtImagem.setLong(1, p.getId());
                            try (ResultSet rsImagem = stmtImagem.executeQuery()) {
                                while (rsImagem.next()) {
                                    imagens.add(new ImagemProduto(rsImagem.getLong("ID"),
                                            rsImagem.getString("NOME_ARQUIVO"),
                                            rsImagem.getString("LEGENDA")));
                                }
                            }
                        }
                        p.setImagens(imagens);

                        // RECUPERA CATEGORIAS
                        List<Categoria> todasCategorias = categoriaRepository.findAll();
                        Map<Integer, Categoria> mapCategorias = new HashMap<>();
                        for (Categoria cat : todasCategorias) {
                            mapCategorias.put(cat.getId(), cat);
                        }
                        Set<Categoria> categorias = new LinkedHashSet<>();
                        try (PreparedStatement stmtCat =
                                conn.prepareStatement(sqlProdutoCategoria)) {
                            stmtCat.setLong(1, p.getId());
                            try (ResultSet rsCat = stmtCat.executeQuery()) {
                                while (rsCat.next()) {
                                    categorias.add(mapCategorias.get(rsCat.getInt("CATEGORIA_ID")));
                                }
                            }
                        }
                        p.setCategorias(categorias);
                        return p;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Produto save(Produto p) {
        if (p.getId() != null) {
            update(p);
        } else {
            insert(p);
        }
        return p;
    }

    private void insert(Produto p) {
        String sqlProduto =
                "INSERT INTO PRODUTO (NOME, DESCRICAO, PRECO_COMPRA, PRECO_VENDA, QUANTIDADE, DISPONIVEL, DT_CADASTRO) VALUES (?,?,?,?,?,?,?)";
        String sqlProdutoCategoriaIns =
                "INSERT INTO PRODUTO_CATEGORIA (PRODUTO_ID, CATEGORIA_ID) VALUES(?,?)";
        String sqlImagemIns =
                "INSERT INTO IMAGEM_PRODUTO (NOME_ARQUIVO, LEGENDA, PRODUTO_ID) VALUES (?,?,?)";

        int resultados = 0;
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt =
                    conn.prepareStatement(sqlProduto, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, p.getNome());
                stmt.setString(2, p.getDescricao());
                stmt.setBigDecimal(3, p.getPrecoCompra());
                stmt.setBigDecimal(4, p.getPrecoVenda());
                stmt.setInt(5, p.getQuantidade());
                stmt.setBoolean(6, p.isDisponivel());
                stmt.setTimestamp(7,
                        new java.sql.Timestamp(Date
                                .from(p.getDtCadastro().atZone(ZoneId.systemDefault()).toInstant())
                                .getTime()));
                resultados = stmt.executeUpdate();

                if (resultados > 0) {
                    try (ResultSet chaves = stmt.getGeneratedKeys()) {
                        if (chaves != null && chaves.next()) {
                            p.setId(chaves.getInt(1));
                        }
                    }
                    // Incluindo novo produto - inclui as imagens cadastradas
                    if (p.getImagens() != null && !p.getImagens().isEmpty()) {
                        for (ImagemProduto img : p.getImagens()) {
                            try (PreparedStatement stmtImg = conn.prepareStatement(sqlImagemIns)) {
                                stmtImg.setString(1, img.getNomeArquivo());
                                stmtImg.setString(2, img.getLegenda());
                                stmtImg.setLong(3, p.getId());
                                stmtImg.executeUpdate();
                            }
                        }
                    }

                    // Salva categorias associadas
                    if (p.getCategorias() != null && !p.getCategorias().isEmpty()) {
                        for (Categoria cat : p.getCategorias()) {
                            try (PreparedStatement stmtCatIns =
                                    conn.prepareStatement(sqlProdutoCategoriaIns)) {
                                stmtCatIns.setLong(1, p.getId());
                                stmtCatIns.setInt(2, cat.getId());
                                stmtCatIns.executeUpdate();
                            }
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
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    private void update(Produto p) {
        String sqlProduto =
                "UPDATE PRODUTO SET NOME=?, DESCRICAO=?, PRECO_COMPRA=?, PRECO_VENDA=?, QUANTIDADE=?, DISPONIVEL=? WHERE ID=?";
        String sqlProdutoCategoriaDel = "DELETE FROM PRODUTO_CATEGORIA WHERE PRODUTO_ID=?";
        String sqlProdutoCategoriaIns =
                "INSERT INTO PRODUTO_CATEGORIA (PRODUTO_ID, CATEGORIA_ID) VALUES(?,?)";

        int resultados = 0;
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sqlProduto)) {
                stmt.setString(1, p.getNome());
                stmt.setString(2, p.getDescricao());
                stmt.setBigDecimal(3, p.getPrecoCompra());
                stmt.setBigDecimal(4, p.getPrecoVenda());
                stmt.setInt(5, p.getQuantidade());
                stmt.setBoolean(6, p.isDisponivel());
                stmt.setLong(7, p.getId());
                resultados = stmt.executeUpdate();
                if (resultados > 0) {
                    // Atualizar imagens (TODO)

                    // Salva categorias associadas
                    try (PreparedStatement stmtCatDel =
                            conn.prepareStatement(sqlProdutoCategoriaDel)) {
                        stmtCatDel.setLong(1, p.getId());
                        stmtCatDel.executeQuery();
                    }
                    if (p.getCategorias() != null && !p.getCategorias().isEmpty()) {
                        for (Categoria cat : p.getCategorias()) {
                            try (PreparedStatement stmtCatIns =
                                    conn.prepareStatement(sqlProdutoCategoriaIns)) {
                                stmtCatIns.setLong(1, p.getId());
                                stmtCatIns.setInt(2, cat.getId());
                                stmt.executeQuery();
                            }
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
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sqlProd = "DELETE FROM PRODUTO WHERE ID=?";
        String sqlProdCat = "DELETE FROM PRODUTO_CATEGORIA WHERE PRODUTO_ID=?";
        String sqlImg = "DELETE FROM IMAGEM_PRODUTO WHERE PRODUTO_ID=?";

        int resultados = 0;
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtProdCat = conn.prepareStatement(sqlProdCat)) {
                stmtProdCat.setInt(1, id);
                stmtProdCat.executeUpdate();
            }
            try (PreparedStatement stmtImg = conn.prepareStatement(sqlImg)) {
                stmtImg.setInt(1, id);
                stmtImg.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(sqlProd)) {
                stmt.setInt(1, id);
                resultados = stmt.executeUpdate();
            }
            if (resultados > 0) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    private void insertImagem(Connection conn, Long idProd, ImagemProduto img) {
        String sql =
                "INSERT INTO IMAGEM_PRODUTO (NOME_ARQUIVO, LEGENDA, PRODUTO_ID) VALUES (?,?,?)";
    }

    private void updateImagem(Connection conn, ImagemProduto img) {
        String sql = "UPDATE IMAGEM_PRODUTO SET NOME_ARQUIVO=?, LEGENDA=? WHERE ID=?";
    }

    private void deleteImagem(Connection conn, ImagemProduto img) {
        String sql = "DELETE FROM IMAGEM_PRODUTO WHERE ID=?";
    }
}
