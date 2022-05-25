package br.senac.tads.dsw.exemplorest.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
// OBS: NOME DA TABELA NO BANCO DEVE SER "PESSOA"
@Table(name = "pessoa")
public class DadosPessoais {

    @Id
    @SequenceGenerator(name = "seq_pessoa_id",
            sequenceName = "seq_pessoa_id",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "seq_pessoa_id")
    private Integer id;

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 100)
    // NAO PODE REPETIR
    @Column(unique = true)
    private String apelido;

    @Size(max = 1000)
    private String descricao;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // ISO-8601
    @PastOrPresent
    @NotNull
    private LocalDate dataNascimento;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String telefone;

    private String senha;

    @Transient
    // NAO SALVAR
    private String senhaRepetida;

    @Min(1)
    @Max(99)
    @Column(columnDefinition = "tinyint")
    private int numero;

    @Min(0)
    @Max(3)
    @Digits(integer = 1, fraction = 2)
    private BigDecimal altura;

    @Min(0)
    @Max(500)
    @Digits(integer = 3, fraction = 1)
    private BigDecimal peso;

    // -1 - INDEFINIDO
    //  0 - FEMININO
    //  1 - MASCULINO
    //  2 - OUTRO
    @Column(columnDefinition = "tinyint")
    private int genero = -1;

     // Usando Set ("tipo de List/ArrayList") que evita objetos repetidos
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pessoa_interesse",
            joinColumns = @JoinColumn(name = "pessoa_id"),
            inverseJoinColumns = @JoinColumn(name = "interesse_id"))
    private Set<Interesse> interesses;

    // "pessoa" é o nome do atributo na classe FotoPessoa
    // onde o @ManyToOne foi configurado - associação bidirecional
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pessoa",
            cascade = CascadeType.PERSIST, orphanRemoval = true)
    // CascadeType.PERSIST -> Salva informações da foto "automaticamente"
    // no banco junto com dados da pessoa ao incluir
    // orphanRemoval -> Remove fotos "automaticamente" ao excluir a pessoa
    // Usando Set ("tipo de List/ArrayList") que evita objetos repetidos
    private Set<FotoPessoa> fotos;

    private boolean cadastroAtivo;

    private LocalDateTime dataCadastro;

    // NAO SALVAR DADOS ABAIXO - Utilitarios para funcionamento do cadastro
    @Transient
    //@NotEmpty
    private List<Integer> interessesIds;

    @Transient
    private String arquivoFoto;

    public DadosPessoais() {
        interessesIds = new ArrayList<>();
    }

    public DadosPessoais(String nome, String apelido, String descricao,
            String dataNascimentoStr, String email, String telefone, String senha,
            int numero, String alturaStr, String pesoStr, int genero,
            List<Integer> interessesIds, String arquivoFoto, String urlFotoGerada) {
        this.nome = nome;
        this.apelido = apelido;
        this.descricao = descricao;
        this.dataNascimento = LocalDate.parse(dataNascimentoStr);
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.senhaRepetida = senha;
        this.numero = numero;
        this.altura = new BigDecimal(alturaStr);
        this.peso = new BigDecimal(pesoStr);
        this.genero = genero;
        this.interessesIds = interessesIds;
        this.arquivoFoto = arquivoFoto;
    }

    public DadosPessoais(Integer id, String nome, String apelido, String descricao,
            String dataNascimento, String email, String telefone, String senha,
            int numero, String alturaStr, String pesoStr, int genero,
            List<Integer> interessesIds, String arquivoFoto, String urlFotoGerada) {
        this(nome, apelido, descricao, dataNascimento, email, telefone,
                senha, numero, alturaStr, pesoStr, genero,
                interessesIds, arquivoFoto, urlFotoGerada);
        this.id = id;
    }

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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public void setNumero(int numero) {
        this.numero = numero;
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

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public Set<Interesse> getInteresses() {
        return interesses;
    }

    public void setInteresses(Set<Interesse> interesses) {
        this.interesses = interesses;
    }

    public Set<FotoPessoa> getFotos() {
        return fotos;
    }

    public void setFotos(Set<FotoPessoa> fotos) {
        this.fotos = fotos;
    }

    public boolean isCadastroAtivo() {
        return cadastroAtivo;
    }

    public void setCadastroAtivo(boolean cadastroAtivo) {
        this.cadastroAtivo = cadastroAtivo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Integer> getInteressesIds() {
        return interessesIds;
    }

    public void setInteressesIds(List<Integer> interessesIds) {
        this.interessesIds = interessesIds;
    }

    // Tenta mapear arquivos salvos com prefixo avatar-padrao
    // para arquivo único
    // Resolve problema de duplicidade ao salvar no banco de dados sem ter 
    // q implementar upload
    public String getArquivoFoto() {
        if (arquivoFoto != null && arquivoFoto.startsWith("avatar-padrao")) {
            return "avatar-padrao.jpg";
        }
        return arquivoFoto;
    }

    public void setArquivoFoto(String arquivoFoto) {
        this.arquivoFoto = arquivoFoto;
    }

    /*
     * ######## METODOS UTILITARIOS PARA USAR NOS TEMPLATES ########
     */
    public long getIdade() {
        if (this.getDataNascimento() != null) {
            return ChronoUnit.YEARS.between(this.getDataNascimento(), LocalDate.now());
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
        if (this.getPeso() != null && this.getAltura() != null) {
            BigDecimal basePeso = new BigDecimal(1).multiply(this.getPeso());
            BigDecimal baseAltura = new BigDecimal(1).multiply(this.getAltura()).multiply(this.getAltura());
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

    public String getLinkedinUrl() {
        return "https://br.linkedin.com/in/" + apelido;
    }

    public String getGithubUrl() {
        return "https://github.com/" + apelido;
    }

}
