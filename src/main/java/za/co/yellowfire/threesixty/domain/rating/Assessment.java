package za.co.yellowfire.threesixty.domain.rating;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;

import za.co.yellowfire.threesixty.domain.user.User;

@AccessType(Type.FIELD)
public class Assessment implements Auditable<User, Serializable> {
	private static final long serialVersionUID = 1L;
	public static final String FIELD_ID = "id";
	public static final String FIELD_EMPLOYEE = "employee";
	public static final String FIELD_MANAGER = "manager";
	public static final String FIELD_PERIOD = "period";
	public static final String FIELD_RATINGS = "assessmentRatings";
	public static final String FIELD_SCORE = "score";
	public static final String FIELD_ACTIVE = "active";
	public static final String FIELD_STATUS = "status";
	
	@Id
	private String id;
	@DBRef @NotNull(message = "{assessment.employee.NotNull.message}")
	private User employee;
	@DBRef @NotNull(message = "{assessment.manager.NotNull.message}")
	private User manager;
	@DBRef @NotNull(message = "{assessment.period.NotNull.message}")
	private Period period;
	private Set<AssessmentRating> ratings = new HashSet<AssessmentRating>();
	private double score = 0.0;
	private boolean active = true;
	private AssessmentStatus status = AssessmentStatus.Creating;
	
	@DBRef
	private User createdBy;
	@DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	
	public static Assessment EMPTY() {
		return new Assessment();
	}
	
	public Assessment() {
		super();
	}
	
	@Override
	public String getId() { return this.id; }

	public User getEmployee() { return employee; }
	public void setEmployee(User employee) { this.employee = employee; }

	public User getManager() { return manager; }
	public void setManager(User manager) { this.manager = manager; }

	public Period getPeriod() { return period; }
	public void setPeriod(Period period) { this.period = period; }

	public double getWeightingTotal() {
		double total = 0.0;
		for(AssessmentRating rating : ratings) {
			total += rating.getWeight();
		}
		return total;
	}
	
	public Set<AssessmentRating> getAssessmentRatings() { 
		double total = getWeightingTotal();
		for(AssessmentRating rating : ratings) {
			rating.setWeightingTotal(total);
			rating.setAssessment(this);
		}
		return ratings;
	}
	
	public AssessmentRating addAssessmentRating() {
		return addAssessmentRating(new AssessmentRating());
	}
	
	public AssessmentRating addAssessmentRating(final AssessmentRating rating) {
		rating.setAssessment(this);
		this.ratings.add(rating);
		calculate();
		return rating;
	}
	
	public void removeAssessmentRating(final AssessmentRating rating) {
		rating.setAssessment(null);
		this.ratings.remove(rating);
		calculate();
	}
	
	public double getScore() { return score; }
	
	public int getNoOfRatings() { return getAssessmentRatings().size(); }
	
	public void calculate() {
		double total = getWeightingTotal();
		
		this.score = 0.0;
		Set<AssessmentRating> list = getAssessmentRatings();
		for(AssessmentRating rating : list) {
			rating.setWeightingTotal(total);
			this.score += rating.getScore();
		}
	}
	
	public boolean isComplete() {
		Set<AssessmentRating> list = getAssessmentRatings();
		for(AssessmentRating rating : list) {
			if (rating.getRating() <= 0.0) {
				return false;
			};
		}
		return true;
	}
	
	public AssessmentStatus getStatus() { return status; }
	public void setStatus(AssessmentStatus status) { this.status = status; }
	public Assessment progressStatus() { this.status = this.status.getNextStatus(); return this;}
	
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	/**
	 * Returns whether the employee and manager (i.e. participants) for the assessment has been set
	 * @return true if both the employee and manager are not null
	 */
	public boolean hasParticipants() {
		return this.employee != null && this.manager != null;
	}
	
	@Override
	public boolean isNew() { return StringUtils.isBlank(this.id); }
	
	@Override
	public User getCreatedBy() { return this.createdBy; }
	
	@Override
	public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
	
	@Override
	public DateTime getCreatedDate() { return this.createdDate; }
	
	@Override
	public void setCreatedDate(DateTime creationDate) { this.createdDate = creationDate; }
	
	@Override
	public User getLastModifiedBy() { return this.modifiedBy; }
	
	@Override
	public void setLastModifiedBy(User lastModifiedBy) { this.modifiedBy = lastModifiedBy; }
	
	@Override
	public DateTime getLastModifiedDate() { return this.modifiedDate; }
	
	@Override
	public void setLastModifiedDate(DateTime lastModifiedDate) { this.modifiedDate = lastModifiedDate; }
	
	public void auditChangedBy(final User user) {
		if (isNew()) {
			setCreatedBy(user);
			setCreatedDate(DateTime.now());
		} else {
			setLastModifiedBy(user);
			setLastModifiedDate(DateTime.now());
		}
	}
}
