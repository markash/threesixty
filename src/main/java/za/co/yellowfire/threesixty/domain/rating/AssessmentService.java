package za.co.yellowfire.threesixty.domain.rating;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;

@Service
public class AssessmentService implements za.co.yellowfire.threesixty.domain.question.Service<Assessment> {
	
	private static final Logger LOG = LoggerFactory.getLogger(AssessmentService.class);
	
	private final AssessmentRepository assessmentRepository;
	private final PerformanceAreaRepository performanceAreaRepository;
	private final UserRepository userRepository;
	private final PeriodRepository periodRepository;
	private final ArrayList<Double> possibleRatings;
	private final ArrayList<Double> possibleWeightings;
	
	@Autowired
	public AssessmentService(
			final AssessmentRepository assessmentRepository,
			final PerformanceAreaRepository performanceAreaRepository,
			final PeriodRepository periodRepository,
			final UserRepository userRepository) {
		
		this.assessmentRepository = assessmentRepository;
		this.userRepository = userRepository;
		this.periodRepository = periodRepository;
		this.performanceAreaRepository = performanceAreaRepository;
		
		this.possibleRatings = new ArrayList<>(Arrays.asList(new Double[] {1.0, 2.0, 3.0, 4.0, 5.0}));
		this.possibleWeightings = new ArrayList<>(Arrays.asList(new Double[] {0.0, 10.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 70.0, 75.0, 80.0, 90.0, 100.0}));
	}
		
	public List<User> findActiveUsers() {
		return userRepository.findByActive(true);
	}
	
	public List<Period> findActivePeriods() {
		return periodRepository.findByActive(true, new Sort(Direction.DESC, Period.FIELD_START));
	}
	
	public List<Double> findPossibleRatings() {
		return this.possibleRatings;
	}
	
	public List<Double> findPossibleWeightings() {
		return this.possibleWeightings;
	}

	public List<PerformanceArea> findPerformanceAreas() {
		return this.performanceAreaRepository.findAll();
	}
	
	public Assessment findById(final String id) {
		return assessmentRepository.findOne(id);
	}
	
	public Assessment save(final Assessment assessment, final User changedBy) {
		assessment.auditChangedBy(changedBy);
		return assessmentRepository.save(assessment);
	}
	
	public void delete(final Assessment assessment, final User changedBy) {
		assessment.setActive(false);
		assessment.auditChangedBy(changedBy);
		assessmentRepository.save(assessment);
	}
	
	public AssessmentRepository getAssessmentRepository() { return this.assessmentRepository; }
	public PeriodRepository getPeriodRepository() { return this.periodRepository; }
}
