package za.co.yellowfire.threesixty.domain.kudos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BadgeRepository extends JpaRepository<Badge, String>, JpaSpecificationExecutor<Badge> {
	
//	@Query("{active: {$eq: ?0}}")
//	List<Badge> findByActive(final boolean active);
}
