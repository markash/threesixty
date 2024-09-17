package com.github.markash.threesixty.assessment.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(itemResourceRel = "activity", path = "activity")
public interface ActivityRepository extends PagingAndSortingRepository<Activity, Long>, JpaSpecificationExecutor<Activity>, CrudRepository<Activity, Long> {

//	@Query("{active: {$eq: ?0}}")
//	List<Period> findByActive(final boolean active);
//	@Query("{active: {$eq: ?0}}")
//	List<Period> findByActive(final boolean active, final Sort sort);
//	@Query("{start: {$eq: ?0}, end: {$eq: ?1}, active: {$eq: ?2}}")
//	List<Period> findByStartEndActive(final Date start, final Date end, final boolean active);
//	@Query(value = "{active: {$eq: true}}", count = true)
//	int countActive();
}
