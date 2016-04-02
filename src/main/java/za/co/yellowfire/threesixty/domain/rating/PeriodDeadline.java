package za.co.yellowfire.threesixty.domain.rating;

import java.io.Serializable;
import java.util.Date;

/**
 * The deadlines for an assessment period
 * @author Mark P Ashworth
 */
public class PeriodDeadline implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date publishAssessment;
	private Date selfAssessment;
	private Date managerAssessment;
	private Date completeAssessment;
	
	public PeriodDeadline() { }

	public PeriodDeadline(Date publishAssessment, Date selfAssessment, Date managerAssessment,
			Date completeAssessment) {
		this.publishAssessment = publishAssessment;
		this.selfAssessment = selfAssessment;
		this.managerAssessment = managerAssessment;
		this.completeAssessment = completeAssessment;
	}
	
	public Date getPublishAssessment() { return publishAssessment; }
	public Date getSelfAssessment() { return selfAssessment; }
	public Date getManagerAssessment() { return managerAssessment; }
	public Date getCompleteAssessment() { return completeAssessment; }
	
	public void setPublishAssessment(Date publishAssessment) { this.publishAssessment = publishAssessment; }
	public void setSelfAssessment(Date selfAssessment) { this.selfAssessment = selfAssessment; }
	public void setManagerAssessment(Date managerAssessment) { this.managerAssessment = managerAssessment; }
	public void setCompleteAssessment(Date completeAssessment) { this.completeAssessment = completeAssessment; }
}
