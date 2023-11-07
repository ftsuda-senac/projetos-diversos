package br.senac.tads.dsw.exemplos.dominio;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Entity
public class Interesse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, unique = true, nullable = false)
    // Alternativa - Usar anotações do Validation
    // @NotBlank
    // @Size(max = 100)
    private String nome;

    @ManyToMany(mappedBy = "interesses")
    @JsonIgnore
    private Set<DadosPessoais> pessoas;

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

    public Set<DadosPessoais> getPessoas() {
        return pessoas;
    }

    public void setPessoas(Set<DadosPessoais> pessoas) {
        this.pessoas = pessoas;
    }

    public Interesse() {
        
    }

    public Interesse(String nome) {
        this.nome = nome;
    }

}
