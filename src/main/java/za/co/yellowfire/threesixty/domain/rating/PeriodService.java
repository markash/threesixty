package za.co.yellowfire.threesixty.domain.rating;

import com.google.common.collect.Range;
import io.threesixty.ui.component.card.CounterStatisticModel;
import io.threesixty.ui.security.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.yellowfire.threesixty.domain.PersistenceException;
import za.co.yellowfire.threesixty.domain.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
	public Period save(Period period) throws PersistenceException {
        Objects.requireNonNull(period, "The period to save is required");

        List<Range<LocalDate>> overlapping = retrieveOverlappingPeriodDates(period);
        if (overlapping.size() > 0) {
            StringBuilder b = new StringBuilder();
            overlapping.forEach(range -> b.append(range.toString()).append(" "));
            throw new PersistenceException("The period overlaps the following: " + b.toString());
        }
		this.currentUserProvider.get().ifPresent(p -> period.auditChangedBy(p.getUser()));
		return periodRepository.save(period);
	}

	@Override
	public void delete(Period period) {
		Objects.requireNonNull(period, "The period to save is required");

		period.setActive(false);
		this.currentUserProvider.get().ifPresent(p -> period.auditChangedBy(p.getUser()));
		periodRepository.save(period);
	}

	/**
	 * Counts the number of stored period ranges [getStart(), getEnd()] that are connected to the period range and
	 * if this is greater that zero then the period is overlapping.
	 * <a href="https://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#isConnected(com.google.common.collect.Range)">isConnected</a>
	 * @param period The period to test whether it overlaps any of the stored periods
	 * @return The range of [start, end] dates that overlap
	 */
	public List<Range<LocalDate>> retrieveOverlappingPeriodDates(final Period period) {
		Range<LocalDate> o = Range.closed(period.getStart(), period.getEnd());
		return periodRepository
				.findByActive(true).stream()
				.map(p -> Range.closed(p.getStart(), p.getEnd()))
				.filter(range -> range.isConnected(o))
				.collect(Collectors.toList());


	}

	public PeriodRepository getPeriodRepository() {
		return periodRepository;
	}

	public void setPeriodRepository(PeriodRepository periodRepository) {
		this.periodRepository = periodRepository;
	}
	
	public CounterStatisticModel getPeriodCounterStatistic() {
		return new CounterStatisticModel("PeriodsCounter", periodRepository.countActive());
	}
}
