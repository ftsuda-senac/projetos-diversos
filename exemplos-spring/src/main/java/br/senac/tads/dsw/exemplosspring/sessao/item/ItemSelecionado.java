/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.sessao.item;

import br.senac.tads.dsw.exemplosspring.item.Item;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author ftsuda
 */
public class ItemSelecionado implements Serializable {

    private Item item;

    private LocalDateTime dataHoraInclusao;
    
    public ItemSelecionado() {
        
    }
    
    public ItemSelecionado(Item item) {
        this.item = item;
        this.dataHoraInclusao = LocalDateTime.now();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }
}
