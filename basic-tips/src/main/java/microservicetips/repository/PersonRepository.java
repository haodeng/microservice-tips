package microservicetips.repository;

import microservicetips.model.Person;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PersonRepository {
    // Demo only
    private Map<Long, Person> persons = new HashMap<>();

    public Person findById(long id) {
        return persons.get(id);
    }

    public Collection<Person> findAll() {
        return persons.values();
    }

    public void delete(long id) {
        persons.remove(id);
    }

    public void add(Person person) {
        persons.putIfAbsent(person.getId(), person);
    }

    public void update(Person person) {
        persons.put(person.getId(), person);
    }
}
