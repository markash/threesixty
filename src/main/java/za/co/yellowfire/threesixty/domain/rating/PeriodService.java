package za.co.yellowfire.threesixty.domain.rating;

import io.threesixty.ui.component.card.CounterStatisticModel;
import io.threesixty.ui.security.CurrentUserProvider;
import io.threesixty.ui.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.yellowfire.threesixty.domain.user.User;

import java.util.Optional;

@Service
public class PeriodService implements za.co.yellowfire.threesixty.domain.question.Service<Period> {

	private PeriodRepository periodRepository;
	private CurrentUserProvider<User> currentUserProvider;

	@Autowired
	public PeriodService(
			final PeriodRepository periodRepository,
			final CurrentUserProvider<User> currentUserProvider) {
		super();
		this.periodRepository = periodRepository;
		this.currentUserProvider = currentUserProvider;
	}

	@Override
	public Period findById(String id) {
		return periodRepository.findOne(id);
	}

	@Override
	public Period save(Period period, User changedBy) {
		UserPrincipal<User> principal = this.currentUserProvider.get();
		User user = principal.getUser();
		period.auditChangedBy(changedBy);
		return periodRepository.save(period);
	}

	@Override
	public void delete(Period period, User changedBy) {
		period.setActive(false);
		period.auditChangedBy(changedBy);
		periodRepository.save(period);
	}

	public PeriodRepository getPeriodRepository() {
		return periodRepository;
	}

	public void setPeriodRepository(PeriodRepository periodRepository) {
		this.periodRepository = periodRepository;
	}
	
	public CounterStatisticModel getPeriodCounterStatistic() {
		return new CounterStatisticModel("PeriodsCounter", Optional.of(periodRepository.countActive()));
	}
}
