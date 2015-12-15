package za.co.yellowfire.threesixty.domain.kudos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface BadgeRepository extends MongoRepository<Badge, String> {
	
	@Query("{active: {$eq: ?0}}")
	List<Badge> findByActive(final boolean active);
}
