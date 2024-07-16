package za.co.yellowfire.threesixty.domain.rating;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.apache.commons.lang3.StringUtils;
import za.co.yellowfire.threesixty.domain.AbstractAuditable;
import za.co.yellowfire.threesixty.domain.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Objective extends AbstractAuditable {
	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_TEXT = "text";
	public static final String FIELD_ACTIVE = "active";

	@Id
	private String id;
	private String name;
	private String text;
	private boolean active = true;
	//@DBRef
	private User createdBy;
	//@DBRef
	private User modifiedBy;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	
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

	public String getName() { return name; }
	public void setName(final String name) { this.name = name; }

	public boolean isActive() { return active; }
	public void setActive(final boolean active) { this.active = active; }

	@Override
	public boolean isNew() { return StringUtils.isBlank(this.id); }

	@Override
	public Optional<User> getCreatedBy() { return Optional.ofNullable(this.createdBy); }

	@Override
	public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

	@Override
	public Optional<LocalDateTime> getCreatedDate() { return Optional.ofNullable(this.createdDate); }

	@Override
	public void setCreatedDate(LocalDateTime creationDate) { this.createdDate = creationDate; }

	@Override
	public Optional<User> getLastModifiedBy() { return Optional.ofNullable(this.modifiedBy); }

	@Override
	public void setLastModifiedBy(User lastModifiedBy) { this.modifiedBy = lastModifiedBy; }

	@Override
	public Optional<LocalDateTime> getLastModifiedDate() { return Optional.ofNullable(this.modifiedDate); }

	@Override
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.modifiedDate = lastModifiedDate; }
	
	public void auditChangedBy(final User user) {
		if (isNew()) {
			setCreatedBy(user);
			setCreatedDate(LocalDateTime.now());
		} else {
			setLastModifiedBy(user);
			setLastModifiedDate(LocalDateTime.now());
		}
	}
}
