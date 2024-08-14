package com.github.markash.threesixty.assessment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
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
public class Timeline extends AbstractLongAuditable {
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
	private List<Activity> activities = new ArrayList<>();

	@Column(name = "active")
	private boolean active = true;

	public static Timeline EMPTY() {
		return new Timeline();
	}

	public static Timeline ACTIVE() {
		Timeline timeline = new Timeline();
		timeline.setActive(true);
		return timeline;
	}

	public static Timeline starts(LocalDate date) {
		Timeline timeline = new Timeline();
		timeline.setStart(date);
		return timeline;
	}
	
	public static Timeline starts(Date date) {
		Timeline timeline = new Timeline();
		timeline.setStart(LocalDate.ofEpochDay(date.getTime()));
		return timeline;
	}
	
	public Timeline() { }

	public Timeline(final Long id) {
		super(id);
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public LocalDate getStart() { return start; }
	//public void setStart(LocalDate start) { this.start = Date.from(start.atStartOfDay().atOffset(ZoneOffset.UTC).toInstant()); }
	public void setStart(final LocalDate start) { this.start = start; }
	
	public LocalDate getEnd() { return end; }
	//public void setEnd(LocalDate end) { this.end = Date.from(end.atStartOfDay().atOffset(ZoneOffset.UTC).toInstant()); }
	public void setEnd(final LocalDate end) { this.end = end; }

	@NonNull
	public List<Activity> getActivities() { return activities; }
	public void setActivities(List<Activity> activities) { this.activities = activities; }

	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

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

	public Timeline ends(LocalDate date) {
		this.setEnd(date);
		return this;
	}

	public Timeline ends(Date date) {
		this.setEnd(LocalDate.ofEpochDay(date.getTime()));
		return this;
	}

	@Override
	public String toString() {
		return Optional.ofNullable(start).map(start -> start.format(DateTimeFormatter.ISO_DATE)).orElse("")  +
				" - " +
				Optional.ofNullable(end).map(end -> end.format(DateTimeFormatter.ISO_DATE)).orElse("");
	}
}
