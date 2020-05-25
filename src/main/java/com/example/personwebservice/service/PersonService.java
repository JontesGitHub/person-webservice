package com.example.personwebservice.service;

import com.example.personwebservice.entity.Person;
import com.example.personwebservice.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.UUID;

@Service
public class PersonService {

    private PersonRepository repository;
    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    private void validatePerson(Person person) throws ValidationException {
        if (person.getFirstname() == null ||
                person.getDob() == null) {
            throw new ValidationException("A Person must have firstname and dob (yyyy-MM-dd).");
        }
    }

    @Transactional
    public Person addPerson(Person person) throws Exception {
        validatePerson(person);

        try {
            return repository.save(person);
        } catch (Exception e) {
            throw new Exception("Could not persist Person to Database.");
        }
    }


    public List<Person> getPeople() {
        return repository.findAll();
    }

    @Transactional
    public void deletePerson(UUID id) throws Exception {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("No Person with that ID exists.");
        }
    }

    public Person findPersonById(UUID id) throws Exception {
        return repository.findById(id)
                .orElseThrow(() -> new Exception("No person exists with that ID.")); // Här används Optional hantering
    }

    @Transactional
    public Person updatePerson(UUID id, Person newPerson) throws Exception {
        validatePerson(newPerson);

        Person oldPerson = findPersonById(id);
        oldPerson.setFirstname(newPerson.getFirstname());
        oldPerson.setLastname(newPerson.getLastname());
        oldPerson.setEmail(newPerson.getEmail());
        oldPerson.setDob(newPerson.getDob());
        oldPerson.setGender(newPerson.getGender());

        try {
            repository.save(oldPerson);
            return oldPerson;
        } catch (Exception e) {
            throw new Exception("Could not persist Person to database.");
        }

    }
}
