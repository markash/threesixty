package za.co.yellowfire.threesixty.domain.rating;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

public class AssessmentRepositoryImpl implements AssessmentRepositoryCustom {

	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public AssessmentRepositoryImpl(final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * Counts the assessments for the period grouped by period
	 * @param period The period to count by
	 * @return The list of assessment statuses and the associated counts
	 */
	@Override
	public Map<AssessmentStatus, AssessmentStatusCount> countAssessmentsFor(Period period) {
		
		Map<AssessmentStatus, AssessmentStatusCount> results = AssessmentStatus.stream()
				.collect(Collectors.toMap(as -> as, as -> new AssessmentStatusCount(as, 0L)));
		
		/* Initialise the summary status count to 0 */
		results.put(AssessmentStatus.All, new AssessmentStatusCount(AssessmentStatus.All, 0L));
		
		if (period == null || period.getId() == null) {
			return results;
		}
		
		Aggregation aggregation = newAggregation(
				match(Criteria.where(Assessment.FIELD_PERIOD).in(period)),
				group(Assessment.FIELD_STATUS).count().as(AssessmentStatusCount.FIELD_COUNT),
				project(AssessmentStatusCount.FIELD_COUNT).and(AssessmentStatusCount.FIELD_STATUS).previousOperation(),
				sort(Sort.Direction.DESC, AssessmentStatusCount.FIELD_STATUS)
					
			);

		//Convert the aggregation result into a List
		AggregationResults<AssessmentStatusCount> groupResults = 
				mongoTemplate.aggregate(
					aggregation, 
					Assessment.class, 
					AssessmentStatusCount.class);
		
		groupResults.getMappedResults()
				.stream()
				.forEach((assessmentCount) -> { 
					results.put(assessmentCount.getStatus(), assessmentCount);
					
					AssessmentStatusCount total = results.get(AssessmentStatus.All);
					if (total == null) {
						total = new AssessmentStatusCount(AssessmentStatus.All, 0L);
					}
					total.addCount(assessmentCount.getCount());
					results.put(AssessmentStatus.All, total);
				});	
		
		
		return results;
	}
}
