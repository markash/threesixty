package za.co.yellowfire.threesixty.domain.kudos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface IdealRepository extends MongoRepository<Ideal, String> {
	
	@Query("{active: {$eq: ?0}}")
	List<Ideal> findByActive(final boolean active);
}
