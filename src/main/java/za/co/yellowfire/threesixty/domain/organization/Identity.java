package za.co.yellowfire.threesixty.domain.organization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;

import za.co.yellowfire.threesixty.domain.user.User;


@AccessType(Type.FIELD)
public class Identity implements Auditable<User, String> {
	private static final long serialVersionUID = 8006445483328831553L;

	@Id
	private String id;
	private String name;
	private String parentId;
	private IdentityType type;
	
	@DBRef
	private Identity parent = null;
	@Transient
	private OrganizationLevelMetadata metadata = null;
	@Transient
	private List<Identity> children = new ArrayList<>();
	private boolean active = true;
	@DBRef
	private User createdBy;
	@DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;

	public Identity() { }
	
	public Identity(
			final String name,
			final IdentityType type) {

		this.name = name;
		this.type = type;
	}

	public Identity(
			final String name,
			final Identity parent) {

		this.name = name;
		this.setParent(parent);
	}
	
	public Identity child(
			final Identity child) {

		child.setParent(this);
		this.children.add(child);
		return this;
	}
	
	public Identity child(
			final Identity...children) {

		for(Identity child : children) {
			child.setParent(this);
			this.children.add(child);
		}
		return this;
	}
	
	@Override
	public String getId() { return this.id; }
	public void setId(final String id) { this.id = id; }
	public String getName() { return this.name; }
	public void setName(final String name) { this.name = name; }
	public String getParentId() { return this.parentId; }
	public void setParentId(final String parentId) { this.parentId = parentId; }
	public Identity getParent() { return this.parent; }
	public void setParent(final Identity parent) {
		this.parent = parent; 
		this.parentId = this.parent.getId();
	}
	public boolean hasChildren() { return !this.children.isEmpty(); }
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
	public void setChildren(final List<Identity> children) { this.children =  children; }
	public List<Identity> getChildren() { return this.children; }
	public Optional<OrganizationLevelMetadata> getMetadata() { return Optional.ofNullable(metadata); }
	
	public void setMetadata(
			final OrganizationLevelMetadata metadata) {

		this.metadata = metadata; 
		if (this.metadata != null) {
			if ((this.type == null && this.metadata.getType() !=  null) || (this.type != this.metadata.getType())) {
				this.type = this.metadata.getType();
			}
		}
	}
	
	public IdentityType getType() { return type; }
	public void setType(IdentityType type) { this.type = type; }

	@Override
	public boolean isNew() {
		return StringUtils.isBlank(this.id);
	}

	@Override
	public User getCreatedBy() {
		return this.createdBy;
	}

	@Override
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public DateTime getCreatedDate() {
		return this.createdDate;
	}

	@Override
	public void setCreatedDate(DateTime creationDate) {
		this.createdDate = creationDate;
	}

	@Override
	public User getLastModifiedBy() {
		return this.modifiedBy;
	}

	@Override
	public void setLastModifiedBy(User lastModifiedBy) {
		this.modifiedBy = lastModifiedBy;
	}

	@Override
	public DateTime getLastModifiedDate() {
		return this.modifiedDate;
	}

	@Override
	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.modifiedDate = lastModifiedDate;
	}

	public void auditChangedBy(final User user) {
		if (isNew()) {
			setCreatedBy(user);
			setCreatedDate(DateTime.now());
		} else {
			setLastModifiedBy(user);
			setLastModifiedDate(DateTime.now());
		}
	}
	
	public String toString() {
		return this.name;
	}
}
