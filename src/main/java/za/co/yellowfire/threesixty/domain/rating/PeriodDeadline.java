package za.co.yellowfire.threesixty.domain.rating;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * The deadlines for an assessment period
 * @author Mark P Ashworth
 */
public class PeriodDeadline implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LocalDate publishAssessment;
	private LocalDate selfAssessment;
	private LocalDate managerAssessment;
	private LocalDate completeAssessment;
	
	public PeriodDeadline() { }

	public PeriodDeadline(
			final LocalDate publishAssessment,
			final LocalDate selfAssessment,
			final LocalDate managerAssessment,
			final LocalDate completeAssessment) {
		this.publishAssessment = publishAssessment;
		this.selfAssessment = selfAssessment;
		this.managerAssessment = managerAssessment;
		this.completeAssessment = completeAssessment;
	}
	
	public LocalDate getPublishAssessment() { return publishAssessment; }
	public LocalDate getSelfAssessment() { return selfAssessment; }
	public LocalDate getManagerAssessment() { return managerAssessment; }
	public LocalDate getCompleteAssessment() { return completeAssessment; }
	
	public void setPublishAssessment(final LocalDate publishAssessment) { this.publishAssessment = publishAssessment; }
	public void setSelfAssessment(final LocalDate selfAssessment) { this.selfAssessment = selfAssessment; }
	public void setManagerAssessment(final LocalDate managerAssessment) { this.managerAssessment = managerAssessment; }
	public void setCompleteAssessment(final LocalDate completeAssessment) { this.completeAssessment = completeAssessment; }
}
