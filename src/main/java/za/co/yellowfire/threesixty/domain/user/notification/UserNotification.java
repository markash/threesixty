package za.co.yellowfire.threesixty.domain.user.notification;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;

import za.co.yellowfire.threesixty.domain.user.User;

import java.io.Serializable;

@SuppressWarnings("unused")
@AccessType(Type.FIELD)
public final class UserNotification implements Auditable<User, Serializable> {
	private static final long serialVersionUID = 1L;

	public static final String FIELD_ID = "id";
	public static final String FIELD_CATEGORY = "category";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_READ = "read";
	public static final String FIELD_USER = "user";
	public static final String FIELD_TIME = "time";
	public static final String FIELD_ACTION = "action";
	public static final String FIELD_ACTIVE = "active";
	public static final String FIELD_CREATED_BY = "createdBy";
	public static final String FIELD_CREATED_DATE = "createdDate";
	public static final String FIELD_LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String FIELD_LAST_MODIFIED_DATE = "lastModifiedDate";

	public static UserNotification EMPTY() { return new UserNotification(); }
	
	@Id
	private String id;
	private NotificationCategory category;
    private String content;
    private boolean read = false;
    
    @DBRef
    private User user;
    private DateTime time;
    private String action;

    private boolean active = true;
    @DBRef
	private User createdBy;
	@DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	
	public UserNotification() {}
	public UserNotification(final User user) { this(user, null); }
	public UserNotification(final User user, final String content) { 
		this.user = user; 
		this.content = content; 
		this.time = DateTime.now();
	}
	
    public String getId() { return id; }

    public NotificationCategory getCategory() { return category; }
	public void setCategory(NotificationCategory category) { this.category = category; }
	
	public String getContent() { return content; }
    public void setContent(final String content) { this.content = content; }

    public boolean isRead() { return read; }
    public void setRead(final boolean read) { this.read = read; }

    public User getUser() { return this.user; }
    public void setUser(final User user) { this.user = user; }
    
    public String getFirstName() { return getUser() != null ? getUser().getFirstName() : null; }
    public String getLastName() { return getUser() != null ? getUser().getLastName() : null; }

    public DateTime getTime() { return this.time; }
    public void setTime(final DateTime time) { this.time = time; }
    public String getTimeAsIso() { return this.time.toString("yyyy-MM-dd hh:mm"); }
    
    public String getAction() { return action; }
    public void setAction(final String action) { this.action = action; }
    
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
		UserNotification other = (UserNotification) obj;
		if (id == null) {
			return other.id == null;
		} else return id.equals(other.id);
	}
	
	@Override
	public String toString() {
		return this.id;
	}
	
	public static UserNotification to(final User user) {
		return new UserNotification(user);
	}
	
	public UserNotification at(final DateTime time) {
		this.setTime(time);
		return this;
	}
	
	public UserNotification category(final NotificationCategory category) {
		this.setCategory(category);
		return this;
	}
	
	public UserNotification content(final String message) {
		this.setContent(message);
		return this;
	}

	public UserNotification action(final String action) {
		this.setAction(action);
		return this;
	}

	public UserNotification from(final User user) {
		if (user != null) {
			this.setCreatedBy(user);
			this.setCreatedDate(DateTime.now());
		}
		return this;
	}
}
