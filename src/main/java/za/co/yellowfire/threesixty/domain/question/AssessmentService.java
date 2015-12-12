package za.co.yellowfire.threesixty.domain.question;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.yellowfire.threesixty.domain.user.User;

@Service
public class AssessmentService implements za.co.yellowfire.threesixty.domain.question.Service<Outcome>{
	private static final Logger LOG = LoggerFactory.getLogger(AssessmentService.class);
	private OutcomeRepository outcomeRepository;
	
	@Autowired
	public AssessmentService(final OutcomeRepository outcomeRepository) {
		this.outcomeRepository = outcomeRepository;
	}
	
	public List<Outcome> getOutcomes() {
		return outcomeRepository.findAll();
	}
	
	public Outcome findOutcome(final String id) {
		return outcomeRepository.findOne(id);
	}
	
	public Outcome save(final Outcome outcome, final User changedBy) {
		outcome.auditChangedBy(changedBy);
		return outcomeRepository.save(outcome);
	}
	
	public void delete(final Outcome outcome, final User changedBy) {
		outcome.setActive(false);
		outcome.auditChangedBy(changedBy);
		outcomeRepository.save(outcome);
	}
}
