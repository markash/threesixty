package com.github.markash.threesixty.assessment.model;

import org.springframework.stereotype.Service;

@Service
public class TimelineService /*implements za.co.yellowfire.threesixty.domain.question.Service<Period>*/ {

//	private PeriodRepository periodRepository;
//	private final CurrentUserProvider<String> currentUserProvider;
//
//	@Autowired
//	public PeriodService(
//			final PeriodRepository periodRepository,
//			final CurrentUserProvider<User> currentUserProvider) {
//		super();
//		this.periodRepository = periodRepository;
//		this.currentUserProvider = currentUserProvider;
//	}
//
//	@Override
//	public Optional<Period> findById(String id) {
//		return periodRepository.findOne(Example.of(new Period(id)));
//	}
//
//	public List<Period> findActive() {
//		return periodRepository.findAll(Example.of(Period.ACTIVE()), Sort.by(Sort.Order.asc(Period.FIELD_START)));
//	}
//
//	@Override
//	public Period save(Period period) throws PersistenceException {
//        Objects.requireNonNull(period, "The period to save is required");
//
//        List<Range<LocalDate>> overlapping = retrieveOverlappingPeriodDates(period);
//        if (overlapping.size() > 0) {
//            StringBuilder b = new StringBuilder();
//            overlapping.forEach(range -> b.append(range.toString()).append(" "));
//            throw new PersistenceException("The period overlaps the following: " + b.toString());
//        }
//		this.currentUserProvider.get().ifPresent(period::auditChangedBy);
//		return periodRepository.save(period);
//	}
//
//	@Override
//	public void delete(Period period) {
//		Objects.requireNonNull(period, "The period to save is required");
//
//		period.setActive(false);
//		this.currentUserProvider.get().ifPresent(period::auditChangedBy);
//		periodRepository.save(period);
//	}
//
//	/**
//	 * Counts the number of stored period ranges [getStart(), getEnd()] that are connected to the period range and
//	 * if this is greater than zero then the period is overlapping.
//	 * <a href="https://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html#isConnected(com.google.common.collect.Range)">isConnected</a>
//	 * @param period The period to test whether it overlaps any of the stored periods
//	 * @return The range of [start, end] dates that overlap
//	 */
//	public List<Range<LocalDate>> retrieveOverlappingPeriodDates(final Period period) {
//		Range<LocalDate> o = Range.closed(period.getStart(), period.getEnd());
//
//		return periodRepository
//				.findAll(Example.of(Period.ACTIVE()), Sort.by(Period.FIELD_START))
//				.stream()
//				.map(p -> Range.closed(p.getStart(), p.getEnd()))
//				.filter(range -> range.isConnected(o))
//				.collect(Collectors.toList());
//	}
//
//	public PeriodRepository getPeriodRepository() {
//		return periodRepository;
//	}
//
//	public void setPeriodRepository(PeriodRepository periodRepository) {
//		this.periodRepository = periodRepository;
//	}
}
