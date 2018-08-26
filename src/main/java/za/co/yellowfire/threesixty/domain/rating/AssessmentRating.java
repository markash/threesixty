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
	
	@DBRef @NotNull(message = "{assessment.discipline.NotNull.message}")
	private Discipline discipline;
	
	@NotNull(message = "{assessment.measurement.NotNull.message}")
	private String measurement;
	
	private String managerComment;
	private String employeeComment;
	private double weight = 1.0;
	private double rating = 0.0;
	private double employeeRating = 0.0;
	private double managerRating = 0.0;
	private double reviewRating = 0.0;
	@Transient
	private double weightingTotal = 0.0;
	@Transient
	private Assessment assessment;
	@DBRef
	private User createdBy;
	@DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	
	@Override
	public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    
	public Discipline getDiscipline() { return discipline; }
	public void setDiscipline(final Discipline discipline) { this.discipline = discipline; }

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

	/**
	 * The self assessment rating the employee
	 * @return Employee rating
	 */
	public double getEmployeeRating() { return employeeRating; }
	public void setEmployeeRating(double employeeRating) { this.employeeRating = employeeRating; }
	
	/**
	 * The manager assessment rating of the employee 
	 * @return Manager rating
	 */
	public double getManagerRating() { return managerRating; }
	public void setManagerRating(double managerRating) { this.managerRating = managerRating; }
	
	/**
	 * The rating the employee and the manager agreed upon during the final assessment review
	 * @return Review rating
	 */
	public double getReviewRating() { return reviewRating; }
	public void setReviewRating(double reviewRating) { this.reviewRating = reviewRating; }
	
	public double getScore() { return getWeightingTotal() != 0.0 ?  getWeight() * getRating() / getWeightingTotal() : 0.0; }

	protected void setWeightingTotal(final double weightingTotal) { this.weightingTotal = weightingTotal; }
	protected double getWeightingTotal() { return weightingTotal; }
	
	public Assessment getAssessment() { return this.assessment; }
	public void setAssessment(final Assessment assessment) { this.assessment = assessment; }
	
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
