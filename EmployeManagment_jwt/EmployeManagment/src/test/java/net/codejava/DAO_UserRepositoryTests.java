package net.codejava;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class DAO_UserRepositoryTests {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private DAO_UserRepository repo;
	
	@Test
	public void testCreateUser() {
		Entity_User entity_User = new Entity_User();
		entity_User.setEmail("ravikumar@gmail.com");
		entity_User.setPassword("ravi2020");
		entity_User.setFirstName("Ravi");
		entity_User.setLastName("Kumar");
		
		Entity_User savedUser = repo.save(entity_User);
		
		Entity_User existUser = entityManager.find(Entity_User.class, savedUser.getId());
		
		assertThat(entity_User.getEmail()).isEqualTo(existUser.getEmail());
		
	}
	
	@Test
	public void testFindByEmail() {
		String email = "codejava.net@gmail.com";
		Entity_User entity_User = repo.findByEmail(email);
		
		assertThat(entity_User.getEmail()).isEqualTo(email);
	}
}
