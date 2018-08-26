package za.co.yellowfire.threesixty.domain.rating;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DisciplineRepository extends MongoRepository<Discipline, String> {
	
	@Query(value = "{active: {$eq: true}}", count = true)
	int countActive();

	@Query("{active: {$eq: ?0}}")
	List<Period> findByActive(final boolean active);
}
