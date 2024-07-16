package za.co.yellowfire.threesixty.domain.kudos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import za.co.yellowfire.threesixty.domain.user.User;

public interface KudosRepository extends JpaRepository<Kudos, String>, KudosRepositoryCustom, JpaSpecificationExecutor<Kudos> {
	
	List<Kudos> findByRecipient(final User user, Sort sort);
	
//	@Query("{createdBy: {$eq: ?0}, active: true}")
//	List<Kudos> findByCreatedBy(final User user, Sort sort);
//
//	@Query("{active: {$eq: ?0}}")
//	List<Kudos> findByActive(final boolean active);
}
