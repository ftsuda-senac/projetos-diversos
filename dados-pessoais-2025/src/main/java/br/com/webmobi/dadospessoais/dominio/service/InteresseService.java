package br.com.webmobi.dadospessoais.dominio.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import br.com.webmobi.dadospessoais.dominio.dto.InteresseDto;
import br.com.webmobi.dadospessoais.dominio.entity.InteresseEntity;
import br.com.webmobi.dadospessoais.dominio.exception.NaoEncontradoException;
import br.com.webmobi.dadospessoais.dominio.repository.InteresseRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class InteresseService implements CrudService<InteresseDto, InteresseDto, InteresseDto, Integer> {

    private final InteresseRepository interesseRepository;

    private InteresseEntity createEntityFromDto(InteresseDto dto) {
        InteresseEntity entity = new InteresseEntity();
        entity.setNome(dto.nome());
        return entity;
    }

    @Override
    public Page<InteresseDto> listar(Pageable pageable) {
        throw new UnsupportedOperationException("Unimplemented method 'listar'");
    }

    @Override
    public List<InteresseDto> listarTudo() {
        return interesseRepository.findAll().stream()
                .map(InteresseDto::new)
                .toList();
    }

    @Override
    public InteresseDto buscarPorId(Integer id) {
        return interesseRepository.findById(id)
                .map(InteresseDto::new)
                .orElseThrow(() -> new NaoEncontradoException("Interesse não encontrado"));
    }

    @Override
    @Transactional
    public InteresseDto incluirNovo(@Valid InteresseDto inputDto) {
        return new InteresseDto(interesseRepository.save(createEntityFromDto(inputDto)));
    }

    @Override
    public InteresseDto alterar(Integer publicId, @Valid InteresseDto dto) {
        throw new UnsupportedOperationException("Unimplemented method 'alterar'");
    }

    @Override
    public void excluir(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'excluir'");
    }

}
