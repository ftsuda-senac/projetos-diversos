/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosspring.sessao.item;

import java.io.Serializable;
import java.time.LocalDateTime;
import br.senac.tads.dsw.exemplosspring.item.Item;

/**
 *
 * @author ftsuda
 */
public class ItemSelecionado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Item item;

    private LocalDateTime dataHoraInclusao;

    private String userAgent;

    public ItemSelecionado() {

    }

    public ItemSelecionado(Item item, String userAgent) {
        this.item = item;
        this.dataHoraInclusao = LocalDateTime.now();
        this.userAgent = userAgent;
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

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
