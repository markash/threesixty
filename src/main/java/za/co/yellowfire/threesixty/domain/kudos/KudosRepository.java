package za.co.yellowfire.threesixty.domain.kudos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import za.co.yellowfire.threesixty.domain.user.User;

public interface KudosRepository extends MongoRepository<Kudos, String> {
	
	List<Kudos> findByRecipient(final User user, Sort sort);
	
	@Query("{createdBy: {$eq: ?0}, active: true}")
	List<Kudos> findByCreatedBy(final User user, Sort sort);
	
	@Query("{active: {$eq: ?0}}")
	List<Kudos> findByActive(final boolean active);
}
