package br.senac.tads.dsw.exemplosspring.i18n;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

public class Info {

    @NotBlank(message = "{info.nome.blank}")
    //@NotBlank(message = "O nome deve ser preenchido")
    private String nome;

    @NotBlank(message = "{info.email.blank}")
    @Email(message = "{info.email.invalid}")
    //@NotBlank(message = "O e-mail deve ser preenchido")
    //@Email(message = "O e-mail digitado é inválido")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "{info.dataNascimento.notPast}")
    //@Past(message = "A data de nascimento não está no passado")
    private LocalDate dataNascimento;

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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

}
