package microservicetips;

import microservicetips.model.Person;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BasicTipsApplicationTests {

	@Autowired
	TestRestTemplate template;

	@Test
	@Order(1)
	void testAdd() {
		Person person = new Person(1, "John", "Smith");
		HttpEntity entity = new HttpEntity(person);
		ResponseEntity response = template.exchange("/persons", HttpMethod.POST, entity, Void.class);
		Assertions.assertEquals(200, response.getStatusCode().value());
	}

	@Test
	@Order(2)
	void testFindById() {
		Person person = template.getForObject("/persons/{id}", Person.class, 1);
		Assertions.assertNotNull(person);
		Assertions.assertEquals(1, person.getId());
	}

	@Test
	@Order(3)
	void testPut() {
		Person person = new Person(1, "John", "Smith");
		HttpEntity entity = new HttpEntity(person);
		ResponseEntity response = template.exchange("/persons", HttpMethod.PUT, entity, Void.class);
		Assertions.assertEquals(200, response.getStatusCode().value());
	}

	@Test
	@Order(4)
	void testDelete() {
		template.delete("/persons/{id}", 1);
	}

}
