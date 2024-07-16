package za.co.yellowfire.threesixty.domain.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Discipline, String>, JpaSpecificationExecutor<Discipline> {
	
//	@Query(value = "{active: {$eq: true}}", count = true)
//	int countActive();
//
//	@Query("{active: {$eq: ?0}}")
//	List<Period> findByActive(final boolean active);
}
