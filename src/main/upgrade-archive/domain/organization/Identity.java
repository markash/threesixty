package za.co.yellowfire.threesixty.domain.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

import za.co.yellowfire.threesixty.domain.AbstractAuditable;

@Entity
@AccessType(Type.FIELD)
public class Identity extends AbstractAuditable {

	private String name;
	private String parentId;
	private IdentityType type;

//	@ManyToOne
//	@JoinColumn(name="parentId")
	@Transient
	private Identity parent = null;
	@Transient
	private OrganizationLevelMetadata metadata = null;
	@Transient
	private List<Identity> children = new ArrayList<>();

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


	
	public String toString() {
		return Objects.toString(this.name);
	}
}
