package za.co.yellowfire.threesixty.domain.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IdentityRepository extends JpaRepository<Identity, String>, JpaSpecificationExecutor<Identity> {
	
//	@Query("{active: {$eq: ?0}}")
//	List<Identity> findByActive(final boolean active);
//
//	@Query("{parent: {$eq: null}}")
	List<Identity> findRoot();
//
//	@Query("{$and: [{active: {$eq: ?0}}, {parent: {$eq: null}}]}")
	List<Identity> findRoot(final boolean active);
//
//	@Query("{parentId: {$eq: ?0}}")
	List<Identity> findChildren(final String parentId);
//
//	@Query("{$and: [{active: {$eq: ?1}}, {parentId: {$eq: ?0}}]}")
	List<Identity> findChildren(final String parentId, final boolean active);
//
//	@Query("{$and: [{active: {$eq: ?1}}, {type: {$eq: ?0}}]}")
	List<Identity> findByType(final IdentityType type, final boolean active);
}
