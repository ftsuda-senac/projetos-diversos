/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.sessao.contador;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author ftsuda
 */
public class Contador implements Serializable {

    private Map<String, Integer> dados = new LinkedHashMap<>();
    
    public void adicionar(String nome) {
        if (!dados.containsKey(nome)) {
            dados.put(nome, 0);
        }
        int valor = dados.get(nome);
        dados.put(nome, valor + 1);
    }

    public Map<String, Integer> getDados() {
        return dados;
    }

    public void setDados(Map<String, Integer> contador) {
        this.dados = contador;
    }
}