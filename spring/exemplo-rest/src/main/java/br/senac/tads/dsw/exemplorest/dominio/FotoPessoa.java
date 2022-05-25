package br.senac.tads.dsw.exemplorest.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class FotoPessoa {

    @Id
    @SequenceGenerator(name = "seq_fotopessoa_id",
            sequenceName = "seq_fotopessoa_id",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "seq_fotopessoa_id")
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
    @JsonIgnore // Adicionar para evitar loop infinito ao gerar JSON
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
