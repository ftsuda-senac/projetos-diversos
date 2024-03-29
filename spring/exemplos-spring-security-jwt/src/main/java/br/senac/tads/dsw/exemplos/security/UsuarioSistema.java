package br.senac.tads.dsw.exemplos.security;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioSistema implements UserDetails {

    private String username;

    private String nomeCompleto;

    private String email;

    private String telefone;

    private String arquivoFoto;

    private String hashSenha;

    private List<Papel> papeis;

    public UsuarioSistema() {

    }

    public UsuarioSistema(String username, String nomeCompleto, String hashSenha, List<Papel> papeis) {
        this.username = username;
        this.nomeCompleto = nomeCompleto;
        this.hashSenha = hashSenha;
        this.papeis = papeis;
    }

    public UsuarioSistema(String username, String nomeCompleto, String hashSenha, String email, String telefone, String arquivoFoto, List<Papel> papeis) {
        this(username, nomeCompleto, hashSenha, papeis);
        this.email = email;
        this.telefone = telefone;
        this.arquivoFoto = arquivoFoto;
    }

    @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    public List<Papel> getAuthorities() {
        return papeis;
    }

    @Override
    public String getPassword() {
        return hashSenha;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getHashSenha() {
        return hashSenha;
    }

    public void setHashSenha(String hashSenha) {
        this.hashSenha = hashSenha;
    }

    public List<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(List<Papel> papeis) {
        this.papeis = papeis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getArquivoFoto() {
        return arquivoFoto;
    }

    public void setArquivoFoto(String arquivoFoto) {
        this.arquivoFoto = arquivoFoto;
    }

}
