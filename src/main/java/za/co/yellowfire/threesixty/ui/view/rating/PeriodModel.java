package za.co.yellowfire.threesixty.ui.view.rating;

import java.util.Date;

import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.rating.PeriodDeadline;
import za.co.yellowfire.threesixty.ui.component.field.MStatsModel;

/**
 * A model for the Assessment Period form which wraps around the period and exposes the assessment period statistics.
 * @author Mark P Ashworth
 */
public class PeriodModel {
	private Period period;
	private MStatsModel registeredAssessments;
	private MStatsModel publishedAssessments;
	private MStatsModel employeeAssessments;
	private MStatsModel completedAssessments;
	
	public String getId() { return period.getId(); }
	public Date getStart() { return this.period.getStart(); }
	public Date getEnd() { return this.period.getEnd(); }
	public PeriodDeadline getDeadline() { return this.period.getDeadline(); }
	
	public Period getPeriod() { return period; }
	public MStatsModel getRegisteredAssessments() { return registeredAssessments; }
	public MStatsModel getPublishedAssessments() { return publishedAssessments; }
	public MStatsModel getEmployeeAssessments() { return employeeAssessments; }
	public MStatsModel getCompletedAssessments() { return completedAssessments; }
	
	public void setId(String id) { this.period.setId(id); }
	public void setStart(Date start) { this.period.setStart(start); }
	public void setEnd(Date end) { this.period.setEnd(end); }
	public void setDeadline(PeriodDeadline deadline) { this.period.setDeadline(deadline); }
	public boolean isActive() { return this.period.isActive(); }
	public void setActive(boolean active) { this.period.setActive(active); }
	
	public void setPeriod(Period period) { this.period = period; }
	public void setRegisteredAssessments(MStatsModel registeredAssessments) { this.registeredAssessments = registeredAssessments; }
	public void setPublishedAssessments(MStatsModel publishedAssessments) { this.publishedAssessments = publishedAssessments; }
	public void setEmployeeAssessments(MStatsModel employeeAssessments) { this.employeeAssessments = employeeAssessments; }
	public void setCompletedAssessments(MStatsModel completedAssessments) { this.completedAssessments = completedAssessments; }
}
