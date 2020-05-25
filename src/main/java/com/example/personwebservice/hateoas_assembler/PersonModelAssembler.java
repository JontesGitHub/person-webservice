package com.example.personwebservice.hateoas_assembler;

import com.example.personwebservice.controller.PersonRESTController;
import com.example.personwebservice.entity.Person;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PersonModelAssembler implements RepresentationModelAssembler<Person, EntityModel<Person>> {
    @Override
    public EntityModel<Person> toModel(Person person) {
        return EntityModel.of(person,
                linkTo(methodOn(PersonRESTController.class).getPerson(person.getId()))
                        .withSelfRel(),
                linkTo(methodOn(PersonRESTController.class).getPeople())
                        .withRel("people:getAll"),
                linkTo(methodOn(PersonRESTController.class).deletePerson(person.getId()))
                        .withRel("person:delete"),
                linkTo(methodOn(PersonRESTController.class).updatePerson(person.getId(), new Person()))
                        .withRel("person:update"),
                linkTo(methodOn(PersonRESTController.class).addPerson(new Person()))
                        .withRel("person:add")
                );
    }
}
