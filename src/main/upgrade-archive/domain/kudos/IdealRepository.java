package za.co.yellowfire.threesixty.domain.kudos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IdealRepository extends JpaRepository<Ideal, String>, JpaSpecificationExecutor<Ideal> {
	
//	@Query("{active: {$eq: ?0}}")
//	List<Ideal> findByActive(final boolean active);
}
