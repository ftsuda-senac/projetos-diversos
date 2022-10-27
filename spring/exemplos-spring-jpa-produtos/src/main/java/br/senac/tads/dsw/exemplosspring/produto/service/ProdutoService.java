package br.senac.tads.dsw.exemplosspring.produto.service;

import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Categoria;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.ImagemProduto;
import br.senac.tads.dsw.exemplosspring.produto.dominio.entidade.Produto;
import br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio.CategoriaRepository;
import br.senac.tads.dsw.exemplosspring.produto.dominio.repositorio.ProdutoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Produto> findAll(List<Integer> idsCat, int pagina, int qtd) {
        List<Produto> resultados;
        if (idsCat != null && !idsCat.isEmpty()) {
            // Busca pelos IDs das categorias informadas
            resultados = produtoRepository.findByCategoria(idsCat, pagina, qtd);
        } else {
            // Lista todos os produtos usando paginacao
            resultados = produtoRepository.findAll(pagina, qtd);
        }
        return resultados;
    }
    
    public Optional<Produto> findById(Integer id) {
        Optional<Produto> optProd = produtoRepository.findById(id);
        if (optProd.isPresent()) {
            // OBS: Trecho abaixo pode ser substituido pelo handler @PostLoad na classe de entidade
            Produto prod = optProd.get();
            if (prod.getCategorias() != null && !prod.getCategorias().isEmpty()) {
                Set<Integer> idsCategorias = new HashSet<>();
                for (Categoria cat : prod.getCategorias()) {
                    idsCategorias.add(cat.getId());
                }
                prod.setIdsCategorias(idsCategorias);
            }
            if (prod.getImagens() != null && !prod.getImagens().isEmpty()) {
                prod.setImagensList(new ArrayList<>(prod.getImagens()));
            }
            return Optional.of(prod);
        }
        return Optional.empty();
    }

    @Transactional
    public Produto salvar(Produto produto) {
        produto.setDtCadastro(LocalDateTime.now());
        if (produto.getIdsCategorias() != null && !produto.getIdsCategorias().isEmpty()) {
            Set<Categoria> categoriasSelecionadas = new HashSet<>();
            for (Integer idCat : produto.getIdsCategorias()) {
                Optional<Categoria> optCat = categoriaRepository.findById(idCat);
                if (optCat.isPresent()) {
                    Categoria cat = optCat.get();
                    categoriasSelecionadas.add(cat);
                    cat.setProdutos(new HashSet<>(Arrays.asList(produto)));
                }
            }
            produto.setCategorias(categoriasSelecionadas);
        }
        if (produto.getImagensList() != null && !produto.getImagensList().isEmpty()) {
            Set<ImagemProduto> imagens = new LinkedHashSet<>();
            for (ImagemProduto img : produto.getImagensList()) {
                img.setProduto(produto);
                imagens.add(img);
            }
            produto.setImagens(imagens);
        }
        return produtoRepository.save(produto);
    }
    
    @Transactional
    public void remover(Integer id) {
        produtoRepository.deleteById(id);
    }

}
