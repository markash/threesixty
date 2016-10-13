package za.co.yellowfire.threesixty.domain.rating;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.yellowfire.threesixty.domain.statistics.CounterStatistic;
import za.co.yellowfire.threesixty.domain.user.User;

@Service
public class PeriodService implements za.co.yellowfire.threesixty.domain.question.Service<Period> {

	private PeriodRepository periodRepository;
	
	@Autowired
	public PeriodService(PeriodRepository periodRepository) {
		super();
		this.periodRepository = periodRepository;
	}

	@Override
	public Period findById(String id) {
		return periodRepository.findOne(id);
	}

	@Override
	public Period save(Period period, User changedBy) {
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
	
	public CounterStatistic getPeriodCounterStatistic() {
		return new CounterStatistic("PersiodsCounter", Optional.of(periodRepository.countActive()));
	}
}
