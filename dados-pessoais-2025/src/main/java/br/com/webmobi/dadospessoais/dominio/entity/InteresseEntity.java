package br.com.webmobi.dadospessoais.dominio.entity;

import java.util.Map;

import br.com.webmobi.dadospessoais.util.AppUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Interesse")
@Table(name = "tb_interesse")
public class InteresseEntity {

    @Id
    @SequenceGenerator(name = "seq_interesse_id", allocationSize = 1, initialValue = 101)
    @GeneratedValue(generator = "seq_interesse_id")
    private Integer id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    private String nome;

    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    private String nomeNormalizado;

    public InteresseEntity(String nome) {
        this.nome = nome;
    }

    @PrePersist
    public void onCreate() {
        this.nomeNormalizado = AppUtil.normalizarStringComExcecoes(this.nome.trim(),
            Map.ofEntries(Map.entry("c++", "cpp"), Map.entry("c#", "csharp")));
    }

}
