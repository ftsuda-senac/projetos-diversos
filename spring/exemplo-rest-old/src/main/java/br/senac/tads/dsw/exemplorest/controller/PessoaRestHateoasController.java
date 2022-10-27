package br.senac.tads.dsw.exemplorest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import br.senac.tads.dsw.exemplorest.dominio.Pessoa;
import br.senac.tads.dsw.exemplorest.dominio.PessoaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/hateoas/pessoas")
public class PessoaRestHateoasController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public PagedModel<EntityModel<Pessoa>> listar(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "qtd", defaultValue = "10") int qtd) {
        Page<Pessoa> pagePessoa = pessoaRepository.findAll(PageRequest.of(pagina, qtd));

        List<EntityModel<Pessoa>> entityModels = new ArrayList<>();
        for (Pessoa p : pagePessoa.getContent()) {
            EntityModel<Pessoa> emp = EntityModel.of(p);
            emp.add(linkTo(methodOn(PessoaRestHateoasController.class).findById(p.getId()))
                    .withSelfRel());
            emp.add(linkTo(methodOn(PessoaRestHateoasController.class).update(p.getId(), p))
                    .withRel("update"));

            entityModels.add(emp);
        }
        PagedModel<EntityModel<Pessoa>> pageModel = PagedModel.of(entityModels,
                new PagedModel.PageMetadata(pagePessoa.getNumberOfElements(),
                        pagePessoa.getNumber(), pagePessoa.getTotalElements()));
        return pageModel;
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<Pessoa>> listarTodos() {
        List<Pessoa> pessoas = pessoaRepository.findAll();

        List<EntityModel<Pessoa>> entityModels = new ArrayList<>();
        for (Pessoa p : pessoas) {
            EntityModel<Pessoa> emp = EntityModel.of(p);
            emp.add(linkTo(methodOn(PessoaRestHateoasController.class).findById(p.getId()))
                    .withSelfRel());

            entityModels.add(emp);
        }
        CollectionModel<EntityModel<Pessoa>> collectionModel = CollectionModel.of(entityModels);
        return collectionModel;
    }

    @GetMapping("/{id}")
    public EntityModel<Pessoa> findById(@PathVariable("id") Integer id) {
        Optional<Pessoa> optPessoa = pessoaRepository.findById(id);
        if (optPessoa.isPresent()) {
            Pessoa pessoa = optPessoa.get();
            EntityModel<Pessoa> emp = EntityModel.of(pessoa);
            emp.add(linkTo(methodOn(PessoaRestHateoasController.class).findById(pessoa.getId()))
                    .withSelfRel());
            emp.add(linkTo(
                    methodOn(PessoaRestHateoasController.class).update(pessoa.getId(), pessoa))
                            .withRel("update"));
            return emp;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Pessoa ID " + id + " não encontrada");
    }

    @PutMapping("/{id}")
    public EntityModel<Pessoa> update(@PathVariable("id") Integer id, @RequestBody Pessoa p) {
        p = pessoaRepository.save(p);
        EntityModel<Pessoa> emp = EntityModel.of(p);
        emp.add(linkTo(methodOn(PessoaRestHateoasController.class).findById(p.getId()))
                .withSelfRel());
        return emp;
    }
}
