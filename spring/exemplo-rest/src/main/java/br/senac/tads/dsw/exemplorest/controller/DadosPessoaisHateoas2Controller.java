/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.senac.tads.dsw.exemplorest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import br.senac.tads.dsw.exemplorest.dominio.DadosPessoais;
import br.senac.tads.dsw.exemplorest.dominio.DadosPessoaisService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/hateoas2/pessoas")
public class DadosPessoaisHateoas2Controller {

    @Autowired
    private DadosPessoaisService service;

    @Autowired
    private PagedResourcesAssembler<DadosPessoais> pageResourceAssembler;

    @GetMapping
    public PagedModel<EntityModel<DadosPessoais>> listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "qtde", defaultValue = "10") int qtde,
            @RequestParam(name = "interessesIds", required = false) List<Integer> interessesIds) {
        Page<DadosPessoais> dadosPaged = service.findAll(page, qtde, interessesIds);
        Page<EntityModel<DadosPessoais>> pagedEntityModels = dadosPaged.map(dado -> EntityModel.of(dado, linkTo(methodOn(DadosPessoaisHateoas2Controller.class).findById(dado.getId())).withSelfRel()));
        Link link = linkTo(methodOn(DadosPessoaisHateoas2Controller.class).listar(page, qtde, interessesIds)).withSelfRel()
                .andAffordance(afford(methodOn(DadosPessoaisHateoas2Controller.class).addNew(null)));
        return pageResourceAssembler.toModel(dadosPaged, link);
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<DadosPessoais>> listarTodos() {
        List<DadosPessoais> dadosAll = service.findAll();
        List<EntityModel<DadosPessoais>> dadosHateoas = dadosAll.stream().map(dado -> EntityModel.of(dado, linkTo(methodOn(DadosPessoaisHateoas2Controller.class).findById(dado.getId())).withSelfRel())).collect(Collectors.toList());
        return CollectionModel.of(dadosHateoas, linkTo(methodOn(DadosPessoaisHateoas2Controller.class).listarTodos()).withSelfRel());
    }
    
    @PostMapping
    public ResponseEntity<?> addNew(@RequestBody DadosPessoais p) {
        service.save(p);
        Link linkSelf = linkTo(methodOn(DadosPessoaisHateoas2Controller.class).findById(p.getId())).withSelfRel();
        EntityModel dadoHateoas = EntityModel.of(p,
                linkSelf
                .andAffordance(afford(methodOn(DadosPessoaisHateoas2Controller.class).update(p.getId(), null))
                )
                .andAffordance(afford(methodOn(DadosPessoaisHateoas2Controller.class).delete(p.getId()))
                )
        );
        return ResponseEntity.created(linkSelf.toUri()).body(dadoHateoas);
    }

    @GetMapping("/{id}")
    public EntityModel<DadosPessoais> findById(@PathVariable Integer id) {
        DadosPessoais dado = service.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa ID " + id + " não encontrada"));
        EntityModel dadoHateoas = EntityModel.of(dado,
                linkTo(methodOn(DadosPessoaisHateoas2Controller.class).findById(id)).withSelfRel()
                .andAffordance(afford(methodOn(DadosPessoaisHateoas2Controller.class).update(id, null))
                )
                .andAffordance(afford(methodOn(DadosPessoaisHateoas2Controller.class).delete(id))
                )
        );
        return dadoHateoas;
    }
    
    @PutMapping("/{id}")
    public EntityModel<DadosPessoais> update(Integer id, @RequestBody DadosPessoais p) {
        service.save(p);
        EntityModel dadoHateoas = EntityModel.of(p, linkTo(methodOn(DadosPessoaisHateoas2Controller.class, findById(p.getId()))).withSelfRel());
        return dadoHateoas;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
