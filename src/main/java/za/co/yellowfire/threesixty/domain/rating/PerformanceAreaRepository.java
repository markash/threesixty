package za.co.yellowfire.threesixty.domain.rating;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PerformanceAreaRepository extends MongoRepository<PerformanceArea, String> {
	
	@Query(value = "{active: {$eq: true}}", count = true)
	int countActive();
}
