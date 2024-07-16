package za.co.yellowfire.threesixty.domain.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
	
	@Query("SELECT u FROM User u WHERE u.id <> :id")
	List<User> findByIdNot(final String id);
//
//	@Query("{active: {$eq: ?0}}")
//	List<User> findByActive(final boolean active);
//
//	@Query(value = "{active: {$eq: true}}", count = true)
//	int countActive();
//
//	@Query("{$and: [{active: {$eq: true}}, {department.id: {$eq: ?0}}]}")
//	List<User> findByDepartment(final String departmentId);
}
