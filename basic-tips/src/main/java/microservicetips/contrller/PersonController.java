package microservicetips.contrller;

import microservicetips.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import microservicetips.repository.PersonRepository;

import java.util.Collection;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/{id}")
    Person findById(@PathVariable long id) {
        return personRepository.findById(id);
    }

    @GetMapping
    Collection<Person> findAll() {
        return personRepository.findAll();
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable long id) {
        personRepository.delete(id);
    }

    @PutMapping
    void update(@RequestBody Person person) {
        personRepository.update(person);
    }

    @PostMapping
    void add(@RequestBody Person person) {
        personRepository.add(person);
    }
}
