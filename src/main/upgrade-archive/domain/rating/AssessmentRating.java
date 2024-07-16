package za.co.yellowfire.threesixty.domain.rating;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

import za.co.yellowfire.threesixty.domain.AbstractAuditable;

@Entity
@AccessType(Type.FIELD)
public class AssessmentRating extends AbstractAuditable {

	public static final String FIELD_DISCIPLINE = "discipline";
	public static final String FIELD_MEASUREMENT = "measurement";
	public static final String FIELD_MANAGER_COMMENT = "managerComment";
	public static final String FIELD_EMPLOYEE_COMMENT = "employeeComment";
	public static final String FIELD_WEIGHT = "weight";
	public static final String FIELD_RATING = "rating";
	public static final String FIELD_SCORE = "score";

	@ManyToOne
	@JoinColumn(name = "disciplineId")
	@NotNull(message = "{assessment.discipline.NotNull.message}")
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
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "assessmentId", referencedColumnName = "id")
	private Assessment assessment;

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
}
