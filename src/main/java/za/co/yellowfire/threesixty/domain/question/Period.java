package za.co.yellowfire.threesixty.domain.question;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A date range
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class Period implements Serializable {
	private static final long serialVersionUID = 1L;

	private LocalDate start;
	private LocalDate end;
	
	public static Period starts(LocalDate date) {
		Period period = new Period();
		period.setStart(date);
		return period;
	}
		
	public Period() {}
	
	public LocalDate getStart() { return start; }
	public void setStart(LocalDate start) { this.start = start; }
	
	public LocalDate getEnd() { return end; }
	public void setEnd(LocalDate end) { this.end = end; }
	
	public Period ends(LocalDate date) {
		this.setEnd(date);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		
		Period other = (Period) obj;
		
		boolean endEquals = (end != null && other.end != null && end.equals(other.end)) ||
							(end == null && other.end == null);
		
		boolean startEquals = (start != null && other.start != null && start.equals(other.start)) ||
							  (start == null && other.start == null);
		
		return endEquals && startEquals;
	}
	
	@Override
	public String toString() {
		return String.format("Period [start=%s, end=%s]", start, end);
	}
}
