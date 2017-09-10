package za.co.yellowfire.threesixty.domain.rating;

import io.threesixty.ui.security.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.yellowfire.threesixty.domain.PersistenceException;
import za.co.yellowfire.threesixty.domain.user.User;

import java.util.Objects;

@Service
public class OutcomeService implements za.co.yellowfire.threesixty.domain.question.Service<Outcome> {
	private OutcomeRepository outcomeRepository;
    private CurrentUserProvider<User> currentUserProvider;

	@Autowired
	public OutcomeService(
			final OutcomeRepository outcomeRepository,
            final CurrentUserProvider<User> currentUserProvider) {
		this.outcomeRepository = outcomeRepository;
        this.currentUserProvider = currentUserProvider;
	}

    public OutcomeRepository getRepository() {
        return outcomeRepository;
    }
	
	public Outcome findById(final String id) {
		return outcomeRepository.findOne(id);
	}
	
	public Outcome save(final Outcome objective) throws PersistenceException {
        Objects.requireNonNull(objective, "The outcome is required");

		this.currentUserProvider.get().ifPresent(p -> objective.auditChangedBy(p.getUser()));
		return outcomeRepository.save(objective);
	}
	
	public void delete(final Outcome objective) {
        Objects.requireNonNull(objective, "The objective is required");

        objective.setActive(false);
        this.currentUserProvider.get().ifPresent(p -> objective.auditChangedBy(p.getUser()));
		outcomeRepository.save(objective);
	}
}
