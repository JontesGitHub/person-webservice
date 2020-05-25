package com.example.personwebservice.controller;

import com.example.personwebservice.entity.Person;
import com.example.personwebservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class PersonController {
    private PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Person> personList = service.getPeople();
        model.addAttribute("personList", personList);
        return "index";
    }

    @GetMapping("/person/add")
    public String addPage(Model model) {
        model.addAttribute("person", new Person());
        return "add";
    }

    @PostMapping("/person/adding")
    public String postPerson(Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "error";
        }
        try {
            service.addPerson(person);
            return "redirect:/";

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping("/person/update")
    public String putPerson(Person newPerson, BindingResult result) {
        if (result.hasErrors()) {
            return "error";
        }
        try {
            service.updatePerson(newPerson.getId(), newPerson);
            return "redirect:/";

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePerson(@PathVariable UUID id) {
        try {
            service.deletePerson(id);
            return "redirect:/";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    @GetMapping("/update/{id}")
    public String updatePerson(@PathVariable UUID id, Model model) {
        Person person = null;
        try {
            person = service.findPersonById(id);
            model.addAttribute("person", person);
            return "update";

        } catch (Exception e) {
           return e.getMessage();
        }
    }


}
