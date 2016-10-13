package za.co.yellowfire.threesixty.domain.rating;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PeriodRepository extends MongoRepository<Period, String> {
	@Query("{active: {$eq: ?0}}")
	List<Period> findByActive(final boolean active);
	@Query("{active: {$eq: ?0}}")
	List<Period> findByActive(final boolean active, final Sort sort);
	@Query("{start: {$eq: ?0}, end: {$eq: ?1}, active: {$eq: ?2}}")
	List<Period> findByStartEndActive(final Date start, final Date end, final boolean active);
	@Query(value = "{active: {$eq: true}}", count = true)
	int countActive();
}
