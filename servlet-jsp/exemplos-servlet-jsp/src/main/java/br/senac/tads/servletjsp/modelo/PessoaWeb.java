/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.servletjsp.modelo;

import java.time.LocalDate;

/**
 *
 * @author fernando.tsuda
 */
public class PessoaWeb {

    private Long id;

    //@Size(min = 2, max = 100, message = "Nome inválido")
    private String nome;

    //@NotBlank // BEAN VALIDATION 2.0
    //@Email
    private String email;

    //@Size(min = 6, max = 64)
    private String senha;

    //@Pattern(regexp = "^(?:(?:\\+|00)?(55)\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})\\-?(\\d{4}))$",
    //       message = "Formato do telefone inválido")
    private String telefone;

    private String urlImagem;

    private LocalDate dtNascimento;

    private int sexo; // 0-feminino, 1-masculino

    private String[] interesses;

    public PessoaWeb() {
    }

    public PessoaWeb(Long id, String nome, String email, String telefone, String urlImagem, LocalDate dtNascimento, int sexo, String[] interesses) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.urlImagem = urlImagem;
        this.dtNascimento = dtNascimento;
        this.sexo = sexo;
        this.interesses = interesses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public int getIdade() {
        return LocalDate.now().getYear() - dtNascimento.getYear();
    }

    public boolean isAniversario() {
        LocalDate hoje = LocalDate.now();
        return (dtNascimento.getDayOfMonth() == hoje.getDayOfMonth() && dtNascimento.getMonthValue() == hoje.getMonthValue());
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public String[] getInteresses() {
        return interesses;
    }

    public void setInteresses(String[] interesses) {
        this.interesses = interesses;
    }

    public boolean isValido() {
        boolean nomeValido = nome != null && nome.trim().length() > 0;
        boolean emailValido = email != null && email.trim().length() > 0;
        boolean telefoneValido = telefone != null && telefone.trim().length() > 0;
        boolean idadeValida = getIdade() > 0;
        return nomeValido && emailValido && telefoneValido && idadeValida;
    }

    @Override
    public String toString() {
        return "Pessoa{" + "id=" + id + ", nome=" + nome + ", email=" + email + ", telefone=" + telefone + ", urlImagem=" + urlImagem + ", dtNascimento=" + dtNascimento + ", sexo=" + sexo + ", interesses=" + interesses + '}';
    }
}
