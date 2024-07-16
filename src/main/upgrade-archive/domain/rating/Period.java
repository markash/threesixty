package za.co.yellowfire.threesixty.domain.rating;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.domain.Auditable;
import za.co.yellowfire.threesixty.domain.user.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

/**
 * An assessment period
 * @author Mark P Ashworth
 */
@Entity
@AccessType(Type.FIELD)
public class Period implements Auditable<User, Serializable, LocalDateTime> {
	public static final String FIELD_ID = "id";
	public static final String FIELD_START = "start";
	public static final String FIELD_END = "end";
	public static final String FIELD_DEADLINE_PUBLISH = "deadline.publishAssessment";
	public static final String FIELD_DEADLINE_COMPLETE = "deadline.completeAssessment";
	public static final String FIELD_DEADLINE_SELF_ASSESSMENT = "deadline.selfAssessment";
	public static final String FIELD_DEADLINE_ASSESSOR_ASSESSMENT = "deadline.managerAssessment";
	public static final String FIELD_REGISTERED_ASSESSMENTS = "registeredAssessments";
	public static final String FIELD_PUBLISHED_ASSESSMENTS = "publishedAssessments";
	public static final String FIELD_EMPLOYEE_ASSESSMENTS = "employeeAssessments";
	public static final String FIELD_COMPLETED_ASSESSMENTS = "completedAssessments";
	public static final String FIELD_ACTIVE = "active";

	public static Period ACTIVE() {
		Period period = new Period();
		period.setActive(true);
		return period;
	}

	@Id
	private String id;
	
	private LocalDate start;
	private LocalDate end;
	private PeriodDeadline deadline = new PeriodDeadline();
	private boolean active = true;
	//@DBRef
	private User createdBy;
	//@DBRef
	private User modifiedBy;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	
//	@Transient
//	private MStatsModel registeredAssessments = new MStatsModel();
//	@Transient
//	private MStatsModel publishedAssessments = new MStatsModel();
//	@Transient
//	private MStatsModel employeeAssessments = new MStatsModel();
//	@Transient
//	private MStatsModel completedAssessments = new MStatsModel();
	
	public static Period EMPTY() {
		return new Period();
	}
	
	public static Period starts(LocalDate date) {
		Period period = new Period();
		period.setStart(date);
		return period;
	}
	
	public static Period starts(Date date) {
		Period period = new Period();
		period.setStart(LocalDate.ofEpochDay(date.getTime()));
		return period;
	}
	
	public Period() { }

	public Period(final String id) {
		this.id = id;
	}

	@Override
	public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    
	public LocalDate getStart() { return start; }
	//public void setStart(LocalDate start) { this.start = Date.from(start.atStartOfDay().atOffset(ZoneOffset.UTC).toInstant()); }
	public void setStart(final LocalDate start) { this.start = start; }
	
	public LocalDate getEnd() { return end; }
	//public void setEnd(LocalDate end) { this.end = Date.from(end.atStartOfDay().atOffset(ZoneOffset.UTC).toInstant()); }
	public void setEnd(final LocalDate end) { this.end = end; }
	
	public PeriodDeadline getDeadline() { if (deadline == null) { this.deadline = new PeriodDeadline(); } return deadline; }
	public void setDeadline(PeriodDeadline deadline) { this.deadline = deadline; }

	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	@Override
	public boolean isNew() { return StringUtils.isBlank(this.id); }

	@Override
	public Optional<User> getCreatedBy() { return Optional.ofNullable(this.createdBy); }

	@Override
	public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

	@Override
	public Optional<LocalDateTime> getCreatedDate() { return Optional.ofNullable(this.createdDate); }

	@Override
	public void setCreatedDate(LocalDateTime creationDate) { this.createdDate = creationDate; }

	@Override
	public Optional<User> getLastModifiedBy() { return Optional.ofNullable(this.modifiedBy); }

	@Override
	public void setLastModifiedBy(User lastModifiedBy) { this.modifiedBy = lastModifiedBy; }

	@Override
	public Optional<LocalDateTime> getLastModifiedDate() { return Optional.ofNullable(this.modifiedDate); }

	@Override
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.modifiedDate = lastModifiedDate; }
	
//	public MStatsModel getRegisteredAssessments() { return registeredAssessments; }
//	public MStatsModel getPublishedAssessments() { return publishedAssessments; }
//	public MStatsModel getEmployeeAssessments() { return employeeAssessments; }
//	public MStatsModel getCompletedAssessments() { return completedAssessments; }
//
//	public void setRegisteredAssessments(MStatsModel registeredAssessments) { this.registeredAssessments = registeredAssessments; }
//	public void setPublishedAssessments(MStatsModel publishedAssessments) { this.publishedAssessments = publishedAssessments; }
//	public void setEmployeeAssessments(MStatsModel employeeAssessments) { this.employeeAssessments = employeeAssessments; }
//	public void setCompletedAssessments(MStatsModel completedAssessments) { this.completedAssessments = completedAssessments; }
	
	public void auditChangedBy(final User user) {
		if (isNew()) {
			setCreatedBy(user);
			setCreatedDate(LocalDateTime.now());
		} else {
			setLastModifiedBy(user);
			setLastModifiedDate(LocalDateTime.now());
		}
	}
	
	public Period ends(LocalDate date) {
		this.setEnd(date);
		return this;
	}

	public Period ends(Date date) {
		this.setEnd(LocalDate.ofEpochDay(date.getTime()));
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Period other = (Period) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Optional.ofNullable(start).map(start -> start.format(DateTimeFormatter.ISO_DATE)).orElse("")  +
				" - " +
				Optional.ofNullable(end).map(end -> end.format(DateTimeFormatter.ISO_DATE)).orElse("");
	}
}
