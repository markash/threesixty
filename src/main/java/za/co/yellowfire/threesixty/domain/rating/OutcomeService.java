package za.co.yellowfire.threesixty.domain.rating;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.yellowfire.threesixty.domain.user.User;

@Service
public class OutcomeService implements za.co.yellowfire.threesixty.domain.question.Service<Outcome> {
	
	private static final Logger LOG = LoggerFactory.getLogger(OutcomeService.class);
	private OutcomeRepository outcomeRepository;
	
	@Autowired
	public OutcomeService(
			final OutcomeRepository outcomeRepository) {
		this.outcomeRepository = outcomeRepository;
	}
	
	public List<Outcome> getOutcomes() {
		return outcomeRepository.findAll();
	}
	
	public Outcome findById(final String id) {
		return outcomeRepository.findOne(id);
	}
	
	public Outcome save(final Outcome outcome, final User changedBy) {
		LOG.info("Outcome {} changed by {}", outcome != null ? outcome : "null", changedBy != null ? changedBy.getId() : "null");
		
		outcome.auditChangedBy(changedBy);
		return outcomeRepository.save(outcome);
	}
	
	public void delete(final Outcome outcome, final User changedBy) {
		LOG.info("Outcome {} deleted by {}", outcome != null ? outcome : "null", changedBy != null ? changedBy.getId() : "null");
		
		outcome.setActive(false);
		outcome.auditChangedBy(changedBy);
		outcomeRepository.save(outcome);
	}
}
