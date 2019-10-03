/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplolocal;

import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author fernando.tsuda
 */
public class ProdutoMain {
    
    public static void main(String... args) {
        ProdutoService service = new ProdutoServiceMock();
        List<Produto> lista = service.findAll(0, 120);
        for (Produto p : lista) {
            System.out.println("Dados produto ID " + p.getId());
            System.out.println("Nome: " + p.getNome());
            System.out.println("Descrição: " + p.getDescricao());
            DecimalFormat df = new DecimalFormat("#,##0.00");
            System.out.println("Preço compra: " + df.format(p.getPrecoCompra()));
            System.out.println("Preço venda: " + df.format(p.getPrecoVenda()));
            System.out.println("Quantidade: " + p.getQuantidade());
            System.out.println("================================================");
        }
    }
    
}
