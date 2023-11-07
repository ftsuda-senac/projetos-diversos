package br.senac.tads.dsw.exemplos.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class FotoPessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 500)
    // NAO PODE REPETIR
    @Column(unique = true)
    private String nomeArquivo;

    @Size(max = 1000)
    private String legenda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id") // FK
    // @JsonIgnore // Adicionar para evitar loop infinito ao gerar JSON
    private DadosPessoais pessoa;
    
    public FotoPessoa() {
        
    }
    
    public FotoPessoa(String nomeArquivo, String legenda) {
        this.nomeArquivo = nomeArquivo;
        this.legenda = legenda;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public DadosPessoais getPessoa() {
        return pessoa;
    }

    public void setPessoa(DadosPessoais pessoa) {
        this.pessoa = pessoa;
    }
}