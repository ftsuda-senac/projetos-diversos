package br.senac.tads.dsw.exemplospringsecurity.dominio;

import org.springframework.security.core.GrantedAuthority;

public class Papel implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String nome;

    public Papel() {
    }

    public Papel(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getAuthority() {
        return nome;
    }

}
