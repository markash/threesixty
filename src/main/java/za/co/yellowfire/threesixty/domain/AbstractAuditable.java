package za.co.yellowfire.threesixty.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Auditable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@MappedSuperclass
public abstract class AbstractAuditable implements Auditable<String, Serializable, LocalDateTime> {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private boolean active = true;
	private String createdBy;
	private String modifiedBy;

	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public AbstractAuditable() {}

	public AbstractAuditable(final String id) {
		this.id = id;
	}

	@Override
	public String getId() { return this.id; }
	public void setId(String id) { this.id = id; }

	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	@NotNull
	@Override
	public Optional<String> getCreatedBy() { return Optional.ofNullable(this.createdBy); }

	@Override
	public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

	@NotNull
	@Override
	public Optional<LocalDateTime> getCreatedDate() { return Optional.ofNullable(this.createdDate); }

	@Override
	public void setCreatedDate(LocalDateTime creationDate) { this.createdDate = creationDate; }

	@NotNull
	@Override
	public Optional<String> getLastModifiedBy() { return Optional.ofNullable(this.modifiedBy); }

	@Override
	public void setLastModifiedBy(final String lastModifiedBy) { this.modifiedBy = lastModifiedBy; }

	@Override
	public Optional<LocalDateTime> getLastModifiedDate() { return Optional.ofNullable(this.modifiedDate); }

	@Override
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) { this.modifiedDate = lastModifiedDate; }

	@Override
	public boolean isNew() { return StringUtils.isBlank(this.id); }

//	public void auditChangedBy(final User user) {
//		if (isNew()) {
//			setCreatedBy(user.getId());
//			setCreatedDate(LocalDateTime.now());
//		} else {
//			setLastModifiedBy(user.getId());
//			setLastModifiedDate(LocalDateTime.now());
//		}
//	}
}
