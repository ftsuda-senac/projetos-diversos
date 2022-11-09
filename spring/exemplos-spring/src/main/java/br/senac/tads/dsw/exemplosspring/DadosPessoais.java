package br.senac.tads.dsw.exemplosspring;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import br.senac.tads.dsw.exemplosspring.validator.SenhasConfirmadas;
import javax.validation.constraints.NotNull;

@SenhasConfirmadas
public class DadosPessoais implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotBlank(message = "POR FAVOR PREENCHER O NOME COMPLETO")
    @Size(max = 100)
    private String nome;
    
    @NotBlank
    @Size(max = 100)
    private String apelido;

    @Size(max = 1000)
    private String descricao;

    @PastOrPresent
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // ISO-8601
    private LocalDate dataNascimento;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String telefone;

    // REGEX para senha https://stackoverflow.com/a/59317682
    @NotBlank
    // @Pattern(regexp = "^(?=(.*[a-z]){3,})(?=(.*[A-Z]){2,})(?=(.*[0-9]){2,})(?=(.*[!@#$%^&*()\\-__+.]){1,}).{8,}$")
    private String senha;

    @NotBlank
    // @Pattern(regexp = "^(?=(.*[a-z]){3,})(?=(.*[A-Z]){2,})(?=(.*[0-9]){2,})(?=(.*[!@#$%^&*()\\-__+.]){1,}).{8,}$")
    private String senhaRepetida;

    @Min(1)
    @Max(99)
    private int numero;

    @Min(0)
    @Max(3)
    @Digits(integer = 1, fraction = 2)
    private BigDecimal altura;

    @Min(0)
    @Max(500)
    @Digits(integer = 3, fraction = 1)
    private BigDecimal peso;

    // 0 - FEMININO
    // 1 - MASCULINO
    // 2 - NENHUM
    private int genero;

    @NotEmpty
    private List<String> interesses;

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

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenhaRepetida() {
        return senhaRepetida;
    }

    public void setSenhaRepetida(String senhaRepetida) {
        this.senhaRepetida = senhaRepetida;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numeroSorte) {
        this.numero = numeroSorte;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int sexo) {
        this.genero = sexo;
    }

    public List<String> getInteresses() {
        return interesses;
    }

    public void setInteresses(List<String> interesses) {
        this.interesses = interesses;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public long getIdade() {
        if (this.dataNascimento != null) {
            return ChronoUnit.YEARS.between(this.dataNascimento, LocalDate.now());
        }
        return 0;
    }

    /*
     * IMC = peso / (altura * altura)
     * Magreza, quando o resultado é menor que 18,5 kg/m2;
     * Normal, quando o resultado está entre 18,5 e 24,9 kg/m2;
     * Sobrepeso, quando o resultado está entre 24,9 e 30 kg/m2;
     * Obesidade, quando o resultado é maior que 30 kg/m2.
     */
    public BigDecimal getImc() {
        BigDecimal basePeso = new BigDecimal(1).multiply(this.peso);
        BigDecimal baseAltura = new BigDecimal(1).multiply(this.altura).multiply(this.altura);
        if (this.peso != null && this.altura != null) {
            return basePeso.divide(baseAltura, RoundingMode.HALF_UP);
        }
        return new BigDecimal(0);
    }

    public String getImcTexto() {
        BigDecimal imc = getImc();
        if (imc.compareTo(new BigDecimal(0)) == 0) {
            return "NÃO CALCULADO";
        } else if (imc.compareTo(new BigDecimal("18.5")) == -1) {
            return "MAGREZA";
        } else if (imc.compareTo(new BigDecimal("24.9")) == -1) {
            return "NORMAL";
        } else if (imc.compareTo(new BigDecimal("30")) == -1) {
            return "SOBREPESO";
        }
        return "OBESIDADE";
    }
}
