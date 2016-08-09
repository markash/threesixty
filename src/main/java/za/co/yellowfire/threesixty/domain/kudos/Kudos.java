package za.co.yellowfire.threesixty.domain.kudos;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;

import za.co.yellowfire.threesixty.domain.user.User;

@AccessType(Type.FIELD)
public class Kudos implements Auditable<User, String> {
	private static final long serialVersionUID = 1L;
	
	public static final Kudos EMPTY() { return new Kudos(); }
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_MESSAGE = "message";
	public static final String FIELD_BADGE = "badge";
	public static final String FIELD_DONOR = "donor";
	public static final String FIELD_RECIPIENT = "recipient";
	public static final String FIELD_ACTIVE = "active";
	
	@Id private String id;
	private String message;
	@DBRef private Badge badge;
	/* The donor of the kudos */
	@DBRef private User donor;
	/* The recipient of the kudos */
	@DBRef private User recipient;
	
	/* Auditing & Existence */
	private boolean active = true;
	@DBRef private User createdBy;
	@DBRef private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	
	@Override 
	public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    
    public String getMessage() { return this.message; }
	public void setMessage(final String message) { this.message = message; }
	
	public Badge getBadge() { return badge; }
	public void setBadge(final Badge badge) { this.badge = badge; }
	
	public User getDonor() { return this.donor; }
	public void setDonor(final User donor) { this.donor = donor; }
	
	public User getRecipient() { return this.recipient; }
	public void setRecipient(final User recipient) { this.recipient = recipient; }
	
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	@Override public boolean isNew() { return StringUtils.isBlank(this.id); }
	@Override public User getCreatedBy() { return this.createdBy; }
	@Override public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
	@Override public DateTime getCreatedDate() { return this.createdDate; }
	@Override public void setCreatedDate(DateTime creationDate) { this.createdDate = creationDate; }
	@Override public User getLastModifiedBy() { return this.modifiedBy; }
	@Override public void setLastModifiedBy(User lastModifiedBy) { this.modifiedBy = lastModifiedBy; }
	@Override public DateTime getLastModifiedDate() { return this.modifiedDate; }
	@Override public void setLastModifiedDate(DateTime lastModifiedDate) { this.modifiedDate = lastModifiedDate; }
	
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
