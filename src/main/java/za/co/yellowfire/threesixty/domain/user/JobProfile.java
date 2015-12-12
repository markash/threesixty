package za.co.yellowfire.threesixty.domain.user;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class JobProfile implements Auditable<User, String> {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	@DBRef
	private User createdBy;
	@DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	
	public JobProfile(String id) {
		super();
		this.id = id;
	}
	
	@Override
	public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    
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
