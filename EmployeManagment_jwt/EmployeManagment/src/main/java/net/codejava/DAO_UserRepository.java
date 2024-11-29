package net.codejava;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DAO_UserRepository extends JpaRepository<Entity_User, Long> {
	public Entity_User findByEmail(String email);	
	   
}
