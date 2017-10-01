package za.co.yellowfire.threesixty.domain.rating;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import za.co.yellowfire.threesixty.domain.user.User;

import java.io.Serializable;

public class Objective implements Auditable<User, Serializable> {
	private static final long serialVersionUID = 1L;

	public static final String FIELD_ID = "id";
	public static final String FIELD_ACTIVE = "active";

	@Id
	private String id;
	private String text;
	private boolean active = true;
	@DBRef
	private User createdBy;
	@DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	
	public static Objective EMPTY() {
		return new Objective();
	}
	
	public Objective() {
		super();
	}

	public Objective(String id) {
		super();
		this.id = id;
	}
	
	@Override
	public String getId() { return this.id; }

	public String getText() { return text; }
	public void setText(final String text) { this.text = text; }

	public boolean isActive() { return active; }
	public void setActive(final boolean active) { this.active = active; }

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
