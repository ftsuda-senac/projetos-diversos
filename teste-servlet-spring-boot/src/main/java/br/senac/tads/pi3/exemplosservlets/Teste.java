package br.senac.tads.pi3.exemplosservlets;

import java.io.Serializable;

public class Teste implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String value;

    private boolean option;

    public Teste() {

    }

    public Teste(Long id, String value, boolean option) {
        this.id = id;
        this.value = value;
        this.option = option;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

}
