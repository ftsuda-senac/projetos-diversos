package br.senac.tads.servletjsp.modelo;

import java.util.ArrayList;
import java.util.List;

// Wrapper para permitir manter produtos em uma ArrayList no jsp quando usa jsp:useBean
public class ArrayProdutos {

    private List<Produto> produtos = new ArrayList<>();

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public void setProduto(Produto p) {
        this.produtos.add(p);
    }

}
