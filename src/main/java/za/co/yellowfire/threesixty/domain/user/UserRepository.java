package za.co.yellowfire.threesixty.domain.user;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {
	
	@Query("{id : {$ne : ?0}}")
	List<User> findByIdNot(final String id);
}
