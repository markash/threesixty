package com.github.markash.threesixty.assessment.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A timeline for activities
 * @author Mark P Ashworth
 */
@Entity
@AccessType(Type.FIELD)
@EntityListeners(AuditingEntityListener.class)
public class Timeline extends AbstractAuditable<User, Long> {
	public static final String FIELD_ID = "id";
	public static final String FIELD_START = "start";
	public static final String FIELD_END = "end";
	public static final String FIELD_ACTIVE = "active";

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "start_date")
	private LocalDate start;

	@Column(name = "end_date")
	private LocalDate end;

	@OneToMany
	@JoinColumn(name="TIMELINE_ID")
	private final List<Activity> activities = new ArrayList<>();

	private boolean active = true;

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public LocalDate getStart() { return start; }
	public void setStart(final LocalDate start) { this.start = start; }
	
	public LocalDate getEnd() { return end; }
	public void setEnd(final LocalDate end) { this.end = end; }

	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	public void merge(final Timeline timeline) {

		if (Objects.isNull(getId())) {
			setId(timeline.getId());
		}

		setName(timeline.getName());
		setStart(timeline.getStart());
		setEnd(timeline.getEnd());

		List<Activity> activitiesToRemove = new ArrayList<>();
		List<Long> activitiesInOther = timeline.getActivities().stream().map(Activity::getId).toList();

		for (Activity activity : getActivities()) {

			if (!activitiesInOther.contains(activity.getId())) {
				activitiesToRemove.add(activity);
				continue;
			}



		}


	}

	@NonNull
	public List<Activity> getActivities() {
		return Collections.unmodifiableList(activities);
	}

	public void setActivities(List<Activity> activities) {
		this.activities.clear();
		this.activities.addAll(activities);
	}

	public void addActivity(@NonNull final Activity activity) {

		List<LocalDateTime> dates = activity.getTimelineDates();

		adjustStart(dates);
		adjustEnd(dates);
	}

	private void adjustStart(final List<LocalDateTime> dates) {

		List<LocalDateTime> datesBeforeStart;

		if (getStart() != null) {
			datesBeforeStart =
					dates.stream()
							.filter(date -> date.isBefore(getStart().atStartOfDay()))
							.collect(Collectors.toList());
		} else {
			datesBeforeStart = new ArrayList<>(dates);
		}

		datesBeforeStart.stream()
                .map(LocalDateTime::toLocalDate).min(LocalDate::compareTo)
				.ifPresent(this::setStart);
	}

	private void adjustEnd(final List<LocalDateTime> dates) {

		List<LocalDateTime> datesAfterEnd;

		if (getEnd() != null) {
			datesAfterEnd =
					dates.stream()
							.filter(date -> date.isAfter(getEnd().atStartOfDay()))
							.collect(Collectors.toList());
		} else {
			datesAfterEnd = new ArrayList<>(dates);
		}

		datesAfterEnd.stream()
                .map(LocalDateTime::toLocalDate)
				.min(Collections.reverseOrder(LocalDate::compareTo))
				.ifPresent(this::setEnd);
	}

	/**
	 * Retrieve the activity from the collection by the activity id.
	 * This will only work once the activities are persisted.
	 * @param id The identifier of the activity
	 * @return The activity corresponding to the id or empty
	 */
	private Optional<Activity> getActivity(final Long id) {

		return getActivities().stream().filter(activity -> activity.getId() != null && activity.getId().equals(id)).findFirst();
	}

	@Override
	@NonNull
	public String toString() {
		return Optional.ofNullable(start).map(date -> date.format(DateTimeFormatter.ISO_DATE)).orElse("")  +
				" - " +
				Optional.ofNullable(end).map(date -> date.format(DateTimeFormatter.ISO_DATE)).orElse("");
	}
}
