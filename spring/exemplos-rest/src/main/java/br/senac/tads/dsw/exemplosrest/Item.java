/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.dsw.exemplosrest;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 *
 * @author fernando.tsuda
 */
public class Item {

    private Integer id;

    private String nome;

    private int valor1;

    private int valor2;

    private LocalDateTime dataHora;

    public Item() {
    }

    public Item(Integer id) {
        SecureRandom random = new SecureRandom();
        this.id = id;
        this.nome = "Item " + id;
        valor1 = 1 + random.nextInt(9);
        valor2 = 1 + random.nextInt(99);
        dataHora = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getValor1() {
        return valor1;
    }

    public void setValor1(int valor1) {
        this.valor1 = valor1;
    }

    public int getValor2() {
        return valor2;
    }

    public void setValor2(int valor2) {
        this.valor2 = valor2;
    }

    /**
     * @return the dataHora
     */
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    /**
     * @param dataHora the dataHora to set
     */
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

}
