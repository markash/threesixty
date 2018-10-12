package za.co.yellowfire.threesixty.domain.rating;

import com.github.markash.ui.security.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import za.co.yellowfire.threesixty.domain.mail.MailingException;
import za.co.yellowfire.threesixty.domain.mail.SendGridMailingService;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;

import java.util.*;

@Service
public class AssessmentService implements za.co.yellowfire.threesixty.domain.question.Service<Assessment> {
	
	//private static final Logger LOG = LoggerFactory.getLogger(AssessmentService.class);
	
	private final AssessmentRepository assessmentRepository;
	private final DisciplineRepository disciplineRepository;
	private final UserRepository userRepository;
	private final PeriodRepository periodRepository;
	private final ArrayList<Double> possibleRatings;
	private final ArrayList<Double> possibleWeightings;
    private CurrentUserProvider<User> currentUserProvider;
    private final SendGridMailingService mailingService;

	@Autowired
	public AssessmentService(
			final AssessmentRepository assessmentRepository,
			final DisciplineRepository disciplineRepository,
			final PeriodRepository periodRepository,
			final UserRepository userRepository,
            final CurrentUserProvider<User> currentUserProvider,
            final SendGridMailingService mailingService) {
		
		this.assessmentRepository = assessmentRepository;
		this.userRepository = userRepository;
		this.periodRepository = periodRepository;
		this.disciplineRepository = disciplineRepository;
		this.currentUserProvider = currentUserProvider;
        this.mailingService = mailingService;

		this.possibleRatings = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
		this.possibleWeightings = new ArrayList<>(Arrays.asList(0.0, 10.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 70.0, 75.0, 80.0, 90.0, 100.0));
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
		return new ArrayList<>(periods);
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
		return new ArrayList<>(periods);
	}
	
	public List<Double> findPossibleRatings() {
		return this.possibleRatings;
	}
	
	public List<Double> findPossibleWeightings() {
		return this.possibleWeightings;
	}

	public List<Discipline> findDisciplines() {
		return this.disciplineRepository.findAll();
	}
	
	public Assessment findById(final String id) {
		return assessmentRepository.findOne(id);
	}
	
	public Assessment save(final Assessment assessment) {
		Objects.requireNonNull(assessment, "The assessment is required");

		this.currentUserProvider.get().ifPresent(assessment::auditChangedBy);

        try {
            mailingService.send();
        } catch (MailingException e) {
            e.printStackTrace();
        }

		return assessmentRepository.save(assessment);
	}
	
	public void delete(final Assessment assessment) {
        Objects.requireNonNull(assessment, "The assessment is required");

		assessment.setActive(false);
        this.currentUserProvider.get().ifPresent(assessment::auditChangedBy);
		assessmentRepository.save(assessment);
	}

	public Map<AssessmentStatus, AssessmentStatusCount> countAssessmentsStatusFor(final Period period) {
		return assessmentRepository.countAssessmentsFor(period);
	}
	
	public AssessmentRepository getAssessmentRepository() { return this.assessmentRepository; }
	public PeriodRepository getPeriodRepository() { return this.periodRepository; }
}
