package br.senac.tads.dsw.exemplosspring.pessoas.service;

import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.DadosPessoais;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.FotoPessoa;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.entidade.Interesse;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.repositorio.DadosPessoaisRepository;
import br.senac.tads.dsw.exemplosspring.pessoas.dominio.repositorio.InteresseRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class DadosPessoaisService {

    private DadosPessoaisRepository dadosPessoaisRepository;

    private InteresseRepository interesseRepository;

    public DadosPessoaisService(DadosPessoaisRepository dadosPessoaisRepository, InteresseRepository interesseRepository) {
        this.dadosPessoaisRepository = dadosPessoaisRepository;
        this.interesseRepository = interesseRepository;
    }

    public List<DadosPessoais> findAll(int page, int qtde) {
        return dadosPessoaisRepository.findAll(page, qtde);
    }

    public Optional<DadosPessoais> findById(Integer id) {
        Optional<DadosPessoais> optDados = dadosPessoaisRepository.findById(id);
        if (optDados.isPresent()) {
            // OBS: Trecho abaixo pode ser substituido pelo handler @PostLoad na classe de entidade
            DadosPessoais dados = optDados.get();
            List<Integer> interessesIds = new ArrayList<>();
            for (Interesse interesse : dados.getInteresses()) {
                interessesIds.add(interesse.getId());
            }
            dados.setInteressesIds(interessesIds);
            if (!dados.getFotos().isEmpty()) {
                for (FotoPessoa foto : dados.getFotos()) {
                    // Simplificando para pegar sempre primeira foto caso tenha mais de uma
                    dados.setArquivoFoto(foto.getNomeArquivo());
                    break;
                }
            }
            return Optional.of(dados);
        }
        return Optional.empty();
    }

    public DadosPessoais save(DadosPessoais dados) {
        if (dados.getId() != null) {
            // Se existir ID, ajusta alguns campos que não foram informados 
            // a partir do que já estava cadastrado anteriormente
            Optional<DadosPessoais> optDados = dadosPessoaisRepository.findById(dados.getId());
            if (optDados.isPresent()) {
                DadosPessoais dadosAntigos = optDados.get();
                dados.setSenha(dadosAntigos.getSenha());
                dados.setSenhaRepetida(dadosAntigos.getSenha());
                dados.setFotos(dadosAntigos.getFotos());
                if (!dados.getFotos().isEmpty()) {
                    for (FotoPessoa foto : dados.getFotos()) {
                        // Simplificando para pegar sempre primeira foto caso tenha mais de uma
                        dados.setArquivoFoto(foto.getNomeArquivo());
                        break;
                    }
                }
                dados.setDataCadastro(dadosAntigos.getDataCadastro());
                dados.setCadastroAtivo(dadosAntigos.isCadastroAtivo());
            }
        } else {
            // Caso não exista, incluir informações adicionais padrão
            String arquivoFoto = "avatar-padrao.jpg";
            if (dados.getArquivoFoto() != null && dados.getArquivoFoto().length() > 0) {
                arquivoFoto = dados.getArquivoFoto();
            }
            Set<FotoPessoa> fotos = new LinkedHashSet<>();
            FotoPessoa foto = new FotoPessoa(arquivoFoto, "Foto de " + dados.getNome());
            foto.setPessoa(dados);
            fotos.add(foto);
            dados.setFotos(fotos);

            dados.setDataCadastro(LocalDateTime.now());
            dados.setCadastroAtivo(true);

        }
        // OBS: Trecho abaixo pode ser substituido pelos handlers @PrePersist
        // e/ou @PreMerge na classe de entidade
        Set<DadosPessoais> pessoas = new HashSet<>();
        pessoas.add(dados);
        
        Set<Interesse> interesses = new LinkedHashSet<>();
        for (Integer interesseId : dados.getInteressesIds()) {
            Optional<Interesse> optInteresse = interesseRepository.findById(interesseId);
            if (optInteresse.isPresent()) {
                Interesse interesse = optInteresse.get();
                // Mapeamento bidirecional
                interesse.setPessoas(pessoas);
                interesses.add(interesse);
            }
        }
        dados.setInteresses(interesses);

        return dadosPessoaisRepository.save(dados);
    }

    public void deleteById(Integer id) {
        dadosPessoaisRepository.deleteById(id);
    }

}