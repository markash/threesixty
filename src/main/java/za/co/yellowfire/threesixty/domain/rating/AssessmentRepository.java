package za.co.yellowfire.threesixty.domain.rating;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AssessmentRepository extends MongoRepository<Assessment, String> {
	@Query("{$or: [{employee.id: {$eq : ?0} }, {manager.id: {$eq : ?0}} ]}")
	List<Assessment> findByAccessTo(final String userName);
}
