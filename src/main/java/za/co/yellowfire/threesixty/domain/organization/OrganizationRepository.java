package za.co.yellowfire.threesixty.domain.organization;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface OrganizationRepository extends MongoRepository<Organization, String> {
	
	@Query("{active: {$eq: ?0}}")
	List<Organization> findByActive(final boolean active);
	
	@Query("{$and: [{active: {$eq: ?0}}, {parent: {$eq: null}}]}")
	List<Organization> findRoot(final boolean active);
	
	@Query("{$and: [{active: {$eq: ?1}}, {parent.id: {$eq: ?0}}]}")
	List<Organization> findChildren(final String parentId, final boolean active);
}
