package za.co.yellowfire.threesixty.domain.rating;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;

@Service
public class AssessmentService implements za.co.yellowfire.threesixty.domain.question.Service<Assessment> {
	
	private static final Logger LOG = LoggerFactory.getLogger(AssessmentService.class);
	private final AssessmentRepository assessmentRepository;
	private final UserRepository userRepository;
	
	@Autowired
	public AssessmentService(
			final AssessmentRepository assessmentRepository,
			final UserRepository userRepository) {
		this.assessmentRepository = assessmentRepository;
		this.userRepository = userRepository;
	}
	
	public List<User> findActiveUsers() {
		return userRepository.findByActive(true);
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
}
