/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.senac.tads.dsw.exemplorest.controller;

import br.senac.tads.dsw.exemplorest.dominio.DadosPessoais;
import br.senac.tads.dsw.exemplorest.dominio.Interesse;
import br.senac.tads.dsw.exemplorest.dominio.InteresseRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/rest-hateoas/interesses")
public class InteresseRestHateoasController {

    private InteresseRepository interesseRepository;

    public InteresseRestHateoasController(InteresseRepository interesseRepository) {
        this.interesseRepository = interesseRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<Interesse>> listar() {
        List<Interesse> resultados = interesseRepository.findAll();

        List<EntityModel<Interesse>> entityModels = new ArrayList<>();
        for (Interesse interesse : resultados) {
            EntityModel emi = EntityModel.of(interesse);
            emi.add(linkTo(methodOn(InteresseRestHateoasController.class).findById(interesse.getId()))
                    .withSelfRel());
            entityModels.add(emi);
        }
        CollectionModel<EntityModel<Interesse>> collectionModel = CollectionModel.of(entityModels);
        return collectionModel;
    }

    @GetMapping("/{id}")
    public EntityModel<Interesse> findById(@PathVariable("id") Integer id) {
        Optional<Interesse> optInteresse = interesseRepository.findById(id);
        if (!optInteresse.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interesse com ID " + id + " não encontrado");
        }
        Interesse interesse = optInteresse.get();
        EntityModel emi = EntityModel.of(interesse);
        emi.add(linkTo(methodOn(InteresseRestHateoasController.class).findById(interesse.getId()))
                .withSelfRel());
        return emi;

    }

}
