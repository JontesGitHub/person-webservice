package com.example.personwebservice.controller;

import com.example.personwebservice.hateoas_assembler.PersonModelAssembler;
import com.example.personwebservice.entity.Person;
import com.example.personwebservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonRESTController {
    private PersonService service;
    private PersonModelAssembler assembler;

    @Autowired
    public PersonRESTController(PersonService service, PersonModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/people")
    public ResponseEntity<CollectionModel<EntityModel<Person>>> getPeople() {
        List<EntityModel<Person>> people = service.getPeople().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(
                people,
                linkTo(methodOn(PersonRESTController.class).getPeople()).withSelfRel(),
                linkTo(methodOn(PersonRESTController.class).addPerson(new Person())).withRel("people:add")
        ));
    }

    @GetMapping("people/get/{id}")
    public ResponseEntity<?> getPerson(@PathVariable UUID id) {
        try {
            Person person = service.findPersonById(id);
            return ResponseEntity.ok(assembler.toModel(person));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/people/add")
    public ResponseEntity<?> addPerson(@RequestBody Person person) {
        try {
            Person savedPerson = service.addPerson(person);
            EntityModel<Person> personEntityModel = assembler.toModel(savedPerson);
            return ResponseEntity.created(
                    personEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(personEntityModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/people/update/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable UUID id, @RequestBody Person person) {
        try {
            Person updatedPerson = service.updatePerson(id, person);
            EntityModel<Person> personEntityModel = assembler.toModel(updatedPerson);
            return ResponseEntity.created(
                    personEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(personEntityModel);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("people/delete/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable UUID id) {
        try {
            service.deletePerson(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
