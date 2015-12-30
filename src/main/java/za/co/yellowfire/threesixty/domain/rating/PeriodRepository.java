package za.co.yellowfire.threesixty.domain.rating;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PeriodRepository extends MongoRepository<Period, String> {
	@Query("{active: {$eq: ?0}}")
	List<Period> findByActive(final boolean active);
	@Query("{active: {$eq: ?0}}")
	List<Period> findByActive(final boolean active, final Sort sort);
	@Query("{startDate: {$eq: ?0}, endDate: {$eq: ?1}, active: {$eq: ?2}}")
	List<Period> findByDate(final LocalDate startDate, final LocalDate endDate, final boolean active);
}
