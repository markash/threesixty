package za.co.yellowfire.threesixty.domain.organization;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface IdentityRepository extends MongoRepository<Identity, String> {
	
	@Query("{active: {$eq: ?0}}")
	List<Identity> findByActive(final boolean active);

	@Query("{parent: {$eq: null}}")
	List<Identity> findRoot();

	@Query("{$and: [{active: {$eq: ?0}}, {parent: {$eq: null}}]}")
	List<Identity> findRoot(final boolean active);

	@Query("{parentId: {$eq: ?0}}")
	List<Identity> findChildren(final String parentId);

	@Query("{$and: [{active: {$eq: ?1}}, {parentId: {$eq: ?0}}]}")
	List<Identity> findChildren(final String parentId, final boolean active);

	@Query("{$and: [{active: {$eq: ?1}}, {type: {$eq: ?0}}]}")
	List<Identity> findByType(final IdentityType type, final boolean active);
}
