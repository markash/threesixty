package za.co.yellowfire.threesixty.domain.user;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {
	
	@Query("{id: {$ne: ?0}}")
	List<User> findByIdNot(final String id);
	
	@Query("{active: {$eq: ?0}}")
	List<User> findByActive(final boolean active);
	
	@Query(value = "{active: {$eq: true}}", count = true)
	int countActive();
	
	@Query("{$and: [{active: {$eq: true}}, {department.id: {$eq: ?0}}]}")
	List<User> findByDepartment(final String departmentId);
}
