package za.co.yellowfire.threesixty.domain.rating;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;

import za.co.yellowfire.threesixty.domain.user.User;

@AccessType(Type.FIELD)
public class AssessmentRating implements Auditable<User, String> {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@NotNull(message = "{assessment.performanceArea.NotNull.message}")
	private String performanceArea;
	@NotNull(message = "{assessment.measurement.NotNull.message}")
	private String measurement;
	private String managerComment;
	private String employeeComment;
	private double weight = 1.0;
	private double rating = 0.0;
	@Transient
	private double total = 0.0;
	
	@DBRef
	private User createdBy;
	@DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	
	@Override
	public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    
	public String getPerformanceArea() { return performanceArea; }
	public void setPerformanceArea(String performanceArea) { this.performanceArea = performanceArea; }

	public String getMeasurement() { return measurement; }
	public void setMeasurement(String measurement) { this.measurement = measurement; }

	public String getManagerComment() { return managerComment; }
	public void setManagerComment(String managerComment) { this.managerComment = managerComment; }

	public String getEmployeeComment() { return employeeComment; }
	public void setEmployeeComment(String employeeComment) { this.employeeComment = employeeComment; }

	public double getWeight() { return weight; }
	public void setWeight(double weight) { this.weight = weight; }

	public double getRating() { return rating; }
	public void setRating(double rating) { this.rating = rating; }

	public double getScore() { return getTotal() != 0.0 ?  getWeight() * getRating() / getTotal() : 0.0; }
	
	protected void setTotal(final double total) { this.total = total; }
	protected double getTotal() { return total; }
	
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
