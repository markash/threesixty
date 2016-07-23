package za.co.yellowfire.threesixty.domain.rating;

import java.io.Serializable;

/**
 * The assessment count per status
 * @author Mark P Ashworth
 */
public class AssessmentStatusCount implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_COUNT = "count";
	
	private AssessmentStatus status;
	private Long count;
	
	public AssessmentStatusCount(AssessmentStatus status, Long count) {
		this.status = status;
		this.count = count;
	}
	public AssessmentStatus getStatus() { return status; }
	public void setStatus(AssessmentStatus status) { this.status = status; }
	
	public Long getCount() { return count; }
	public void setCount(Long count) { this.count = count; }
	
	public Long addCount(Long count) { 
		if (count != null) {
			this.count = (this.count != null ? this.count + count : count);
		}
		return this.count;
	}
}