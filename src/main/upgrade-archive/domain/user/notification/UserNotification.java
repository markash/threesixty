package za.co.yellowfire.threesixty.domain.user.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

import za.co.yellowfire.threesixty.domain.AbstractAuditable;
import za.co.yellowfire.threesixty.domain.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@AccessType(Type.FIELD)
public final class UserNotification extends AbstractAuditable {
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

	@Transient
	public static UserNotification EMPTY() { return new UserNotification(); }

	private NotificationCategory category;
    private String content;
    private boolean read = false;

	@ManyToOne
	@JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime time;
    private String action;


	public UserNotification() {}
	public UserNotification(final User user) { this(user, null); }
	public UserNotification(final User user, final String content) { 
		this.user = user; 
		this.content = content; 
		this.time = LocalDateTime.now();
	}

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

    public LocalDateTime getTime() { return this.time; }
    public void setTime(final LocalDateTime time) { this.time = time; }
    public String getTimeAsIso() { return this.time.format(DateTimeFormatter.ISO_DATE_TIME); }
    
    public String getAction() { return action; }
    public void setAction(final String action) { this.action = action; }

	/**
	 * Determines whether the notification is address to the user
	 * @param user The user to test
	 * @return Whether the user is addressed by the notification
	 */
	public boolean addressedTo(
			final User user) {

		return this.user != null && this.user.equals(user);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		if (getId() == null) {
			return other.getId() == null;
		} else return getId().equals(other.getId());
	}
	
	@Override
	public String toString() {
		return Objects.toString(getId());
	}
	
	public static UserNotification to(final User user) {
		return new UserNotification(user);
	}
	
	public UserNotification at(final LocalDateTime time) {
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
			this.setCreatedBy(user.getId());
			this.setCreatedDate(LocalDateTime.now());
		}
		return this;
	}
}
