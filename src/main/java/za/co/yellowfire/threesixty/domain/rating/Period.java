package za.co.yellowfire.threesixty.domain.rating;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;

import za.co.yellowfire.threesixty.domain.user.User;

/**
 * A date range
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@AccessType(Type.FIELD)
public class Period implements Auditable<User, String> {
	private static final long serialVersionUID = 1L;
	public static final String FIELD_START = "start";
	public static final String FIELD_END = "end";
	
	@Id
	private String id;
	
	private LocalDate start;
	private LocalDate end;
	private boolean active = true;
	
	@DBRef
	private User createdBy;
	@DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	
	public static Period starts(LocalDate date) {
		Period period = new Period();
		period.setStart(date);
		return period;
	}
		
	public Period() {}
	
	@Override
	public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    
	public LocalDate getStart() { return start; }
	public void setStart(LocalDate start) { this.start = start; }
	
	public LocalDate getEnd() { return end; }
	public void setEnd(LocalDate end) { this.end = end; }
	
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
	
	public Period ends(LocalDate date) {
		this.setEnd(date);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Period other = (Period) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final String startDate = start != null ? DateTimeFormatter.ISO_DATE.format(start) : "";
		final String endDate = end != null ? DateTimeFormatter.ISO_DATE.format(end) : "";
		return startDate + " - " + endDate;
	}
}
