package za.co.yellowfire.threesixty.domain.rating;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;

import za.co.yellowfire.threesixty.domain.Period;
import za.co.yellowfire.threesixty.domain.user.User;

@AccessType(Type.FIELD)
public class Assessment implements Auditable<User, String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@DBRef @NotNull(message = "{assessment.employee.NotNull.message}")
	private User employee;
	@DBRef @NotNull(message = "{assessment.manager.NotNull.message}")
	private User manager;
	@NotNull(message = "{assessment.period.NotNull.message}")
	private Period period;
	private Set<AssessmentRating> ratings;
	private double score = 0.0;
	private boolean active = true;
	private AssessmentStatus status = AssessmentStatus.Created;
	
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
    public void setId(String id) { this.id = id; }
    
	public User getEmployee() { return employee; }
	public void setEmployee(User employee) { this.employee = employee; }

	public User getManager() { return manager; }
	public void setManager(User manager) { this.manager = manager; }

	public Period getPeriod() { return period; }
	public void setPeriod(Period period) { this.period = period; }

	public double getTotal() {
		double total = 0.0;
		for(AssessmentRating rating : ratings) {
			total += rating.getWeight();
		}
		return total;
	}
	
	public Set<AssessmentRating> getAssessmentRatings() { 
		double total = getTotal();
		for(AssessmentRating rating : ratings) {
			rating.setTotal(total);
		}
		return ratings;
	}

	public void addAssessmentRating(final AssessmentRating rating) {
		this.ratings.add(rating);
		calculate();
	}
	
	public void removeAssessmentRating(final AssessmentRating rating) {
		this.ratings.remove(rating);
		calculate();
	}
	
	public double getScore() { return score; }
	
	public void calculate() {
		this.score = 0.0;
		Set<AssessmentRating> list = getAssessmentRatings();
		for(AssessmentRating rating : list) {
			this.score += rating.getScore();
		}
	}
	
	public AssessmentStatus getStatus() { return status; }
	public void setStatus(AssessmentStatus status) { this.status = status; }

	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

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
