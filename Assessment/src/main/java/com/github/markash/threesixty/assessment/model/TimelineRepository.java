package com.github.markash.threesixty.assessment.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TimelineRepository extends JpaRepository<Timeline, String>, JpaSpecificationExecutor<Timeline> {
    Timeline findByName(final String name);

//	@Query("{active: {$eq: ?0}}")
//	List<Period> findByActive(final boolean active);
//	@Query("{active: {$eq: ?0}}")
//	List<Period> findByActive(final boolean active, final Sort sort);
//	@Query("{start: {$eq: ?0}, end: {$eq: ?1}, active: {$eq: ?2}}")
//	List<Period> findByStartEndActive(final Date start, final Date end, final boolean active);
//	@Query(value = "{active: {$eq: true}}", count = true)
//	int countActive();
}
