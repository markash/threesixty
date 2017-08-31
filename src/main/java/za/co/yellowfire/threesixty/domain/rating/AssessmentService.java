package za.co.yellowfire.threesixty.domain.rating;

import io.threesixty.ui.component.card.CounterStatisticModel;
import io.threesixty.ui.security.CurrentUserProvider;
import io.threesixty.ui.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;

import java.util.*;

@Service
public class AssessmentService implements za.co.yellowfire.threesixty.domain.question.Service<Assessment> {
	
	//private static final Logger LOG = LoggerFactory.getLogger(AssessmentService.class);
	
	private final AssessmentRepository assessmentRepository;
	private final PerformanceAreaRepository performanceAreaRepository;
	private final UserRepository userRepository;
	private final PeriodRepository periodRepository;
	private final ArrayList<Double> possibleRatings;
	private final ArrayList<Double> possibleWeightings;
    private CurrentUserProvider<User> currentUserProvider;

	@Autowired
	public AssessmentService(
			final AssessmentRepository assessmentRepository,
			final PerformanceAreaRepository performanceAreaRepository,
			final PeriodRepository periodRepository,
			final UserRepository userRepository,
            final CurrentUserProvider<User> currentUserProvider) {
		
		this.assessmentRepository = assessmentRepository;
		this.userRepository = userRepository;
		this.periodRepository = periodRepository;
		this.performanceAreaRepository = performanceAreaRepository;
		this.currentUserProvider = currentUserProvider;

		this.possibleRatings = new ArrayList<>(Arrays.asList(new Double[] {1.0, 2.0, 3.0, 4.0, 5.0}));
		this.possibleWeightings = new ArrayList<>(Arrays.asList(new Double[] {0.0, 10.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 70.0, 75.0, 80.0, 90.0, 100.0}));
	}
		
	public List<User> findActiveUsers() {
		return userRepository.findByActive(true);
	}
	
	public List<Period> findActivePeriods() {
		return periodRepository.findByActive(true, new Sort(Direction.DESC, Period.FIELD_START));
	}
	
	public List<Period> findAvailablePeriodsForEmployee(final User user, final Assessment assessment) {
		Set<Period> periods = new HashSet<>(periodRepository.findByActive(true, new Sort(Direction.DESC, Period.FIELD_START)));
		
		for (Period assessedPeriod : findAssessmentPeriodsForEmployee(user, assessment)) {
			periods.remove(assessedPeriod);
		}
		return new ArrayList<Period>(periods);
	}
	
	public List<Period> findAssessmentPeriodsForEmployee(final User user, final Assessment assessment) {
		
		List<Assessment> assessments = new ArrayList<>();
		if (assessment.getId() != null) {
			assessments = assessmentRepository.findByEmployeeExcludingAssessment(user.getId(), assessment.getId());
		} else if (user != null) {
			assessments = assessmentRepository.findByEmployeeId(user.getId());
		}
		
		Set<Period> periods = new HashSet<>();
		for (Assessment value : assessments) {
			periods.add(value.getPeriod());
		}
		return new ArrayList<Period>(periods);
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
	
	public Assessment save(final Assessment assessment) {
		Objects.requireNonNull(assessment, "The assessment is required");
		UserPrincipal<User> principal = this.currentUserProvider.get();
		assessment.auditChangedBy(principal.getUser());
		return assessmentRepository.save(assessment);
	}
	
	public void delete(final Assessment assessment) {
        Objects.requireNonNull(assessment, "The assessment is required");

		assessment.setActive(false);
		assessment.auditChangedBy(this.currentUserProvider.get().getUser());
		assessmentRepository.save(assessment);
	}
	
	public long countAssessmentsFor(final Period period) {
		if (period != null) {
			return assessmentRepository.countByPeriod(period.getId());
		}
		return 0L;
	}
	
	public CounterStatisticModel getAssessmentsCounterStatistic() {
		return new CounterStatisticModel("AssessmentsCounter", assessmentRepository.countActive());
	}
	
	public CounterStatisticModel getAssessmentsDueCounterStatistic(final String userName) {
		return new CounterStatisticModel("AssessmentsDueCounter", assessmentRepository.countActiveDue(userName));
	}
	
	public CounterStatisticModel getPerformanceAreasCounterStatistic() {
		return new CounterStatisticModel("PerformanceAreasCounter", performanceAreaRepository.countActive());
	}
	
	public Map<AssessmentStatus, AssessmentStatusCount> countAssessmentsStatusFor(final Period period) {
		return assessmentRepository.countAssessmentsFor(period);
	}
	
	public AssessmentRepository getAssessmentRepository() { return this.assessmentRepository; }
	public PeriodRepository getPeriodRepository() { return this.periodRepository; }
}
