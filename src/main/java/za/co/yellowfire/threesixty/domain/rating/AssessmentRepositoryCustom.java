package za.co.yellowfire.threesixty.domain.rating;

import java.util.Map;

public interface AssessmentRepositoryCustom {
	/**
	 * Counts the assessments for the period grouped by period
	 * @param period The period to count by
	 * @return The map of assessment statuses and the associated counts
	 */
	Map<AssessmentStatus, AssessmentStatusCount> countAssessmentsFor(final Period period);
}
