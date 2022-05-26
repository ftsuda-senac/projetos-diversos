package br.senac.tads.dsw.exemplorest.controller;

import br.senac.tads.dsw.exemplorest.dominio.DadosPessoais;
import br.senac.tads.dsw.exemplorest.dominio.DadosPessoaisService;
import br.senac.tads.dsw.exemplorest.dominio.Interesse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/rest-hateoas/pessoas")
public class DadosPessoaisRestHateoasController {

    private DadosPessoaisService service;

    public DadosPessoaisRestHateoasController(DadosPessoaisService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<EntityModel<DadosPessoais>> listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "qtde", defaultValue = "10") int qtde,
            @RequestParam(name = "interessesIds", required = false) List<Integer> interessesIds) {
        Page<DadosPessoais> resultadosPagina = service.findAll(page, qtde, interessesIds);

        List<EntityModel<DadosPessoais>> entityModels = new ArrayList<>();
        for (DadosPessoais p : resultadosPagina.getContent()) {
            EntityModel<DadosPessoais> emp = EntityModel.of(p);
            emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).findById(p.getId()))
                    .withSelfRel()
            //.andAffordance(afford(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), null)))
            );
            emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), p))
                    .withRel("edit"));
            for (Interesse interesse : p.getInteresses()) {
                emp.add(linkTo(methodOn(InteresseRestHateoasController.class).findById(interesse.getId())).withRel("interesses"));
            }
            entityModels.add(emp);
        }
        PagedModel<EntityModel<DadosPessoais>> pageModel = PagedModel.of(entityModels,
                new PagedModel.PageMetadata(resultadosPagina.getNumberOfElements(),
                        resultadosPagina.getNumber(), resultadosPagina.getTotalElements()));
        pageModel.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).listar(0, 0, null))
                .withSelfRel()
                .andAffordance(afford(methodOn(DadosPessoaisRestHateoasController.class).addNew(null))));
        return pageModel;
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<DadosPessoais>> listarTodos() {
        List<DadosPessoais> resultados = service.findAll();

        List<EntityModel<DadosPessoais>> entityModels = new ArrayList<>();
        for (DadosPessoais p : resultados) {
            EntityModel<DadosPessoais> emp = EntityModel.of(p);
            emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).findById(p.getId()))
                    .withSelfRel()
            //.andAffordance(afford(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), null)))
            );
            emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), p))
                    .withRel("edit"));
            for (Interesse interesse : p.getInteresses()) {
                emp.add(linkTo(methodOn(InteresseRestHateoasController.class).findById(interesse.getId())).withRel("interesses"));
            }
            entityModels.add(emp);
        }
        CollectionModel<EntityModel<DadosPessoais>> collectionModel = CollectionModel.of(entityModels);
        return collectionModel;
    }

    @GetMapping("/{id}")
    public EntityModel<DadosPessoais> findById(@PathVariable("id") Integer id) {
        Optional<DadosPessoais> optPessoa = service.findById(id);
        if (!optPessoa.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com ID " + id + " não encontrada");
        }
        DadosPessoais p = optPessoa.get();

        EntityModel<DadosPessoais> emp = EntityModel.of(p);
        emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).findById(p.getId()))
                .withSelfRel()
        //.andAffordance(afford(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), null)))
        );
        emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), p))
                .withRel("edit"));
        for (Interesse interesse : p.getInteresses()) {
            emp.add(linkTo(methodOn(InteresseRestHateoasController.class).findById(interesse.getId())).withRel("interesses"));
        }
        return emp;
    }

    @PostMapping
    public EntityModel<DadosPessoais> addNew(@RequestBody DadosPessoais p) {
        p = service.save(p);

        EntityModel<DadosPessoais> emp = EntityModel.of(p);
        emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).findById(p.getId()))
                .withSelfRel()
        //.andAffordance(afford(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), null)))
        );
        emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), p))
                .withRel("edit"));
        for (Interesse interesse : p.getInteresses()) {
            emp.add(linkTo(methodOn(InteresseRestHateoasController.class).findById(interesse.getId())).withRel("interesses"));
        }
        return emp;
    }

    @PutMapping("/{id}")
    public EntityModel<DadosPessoais> update(@PathVariable("id") Integer id, @RequestBody DadosPessoais p) {
        p = service.save(p);

        EntityModel<DadosPessoais> emp = EntityModel.of(p);
        emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).findById(p.getId()))
                .withSelfRel()
                .andAffordance(afford(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), null))));
        emp.add(linkTo(methodOn(DadosPessoaisRestHateoasController.class).update(p.getId(), p))
                .withRel("edit"));
        for (Interesse interesse : p.getInteresses()) {
            emp.add(linkTo(methodOn(InteresseRestHateoasController.class).findById(interesse.getId())).withRel("interesses"));
        }
        return emp;
    }

}
