package za.co.yellowfire.threesixty.domain.organization;

import java.util.Optional;

public class OrganizationLevelMetadata {
	private String name;
	private IdentityType type;
	private IdentityMetadata identityMetadata;
	
	public OrganizationLevelMetadata() {
	}

	public OrganizationLevelMetadata(
			final String name,
			final IdentityType type,
			final IdentityMetadata identityMetadata) {

		this.name = name;
		this.type = type;
		this.identityMetadata = identityMetadata;
	}

	public String getName() { return name; }
	public IdentityType getType() { return type; }
	public IdentityMetadata getIdentityMetadata() { return identityMetadata; }
	
	public void setName(final String name) { this.name = name; }
	public void setType(final IdentityType type) { this.type = type; }
	public void setIdentityMetadata(final IdentityMetadata identityMetadata) { this.identityMetadata = identityMetadata; }

	public Optional<OrganizationLevelMetadata> getParent() {
		if (this.identityMetadata == null) {
			return Optional.empty();
		}
		
		return this.identityMetadata.getPreviousLevelMetadata(this);
	}
	
	public Optional<OrganizationLevelMetadata> getChild() {
		if (this.identityMetadata == null) {
			return Optional.empty();
		}
		
		return this.identityMetadata.getNextLevelMetadata(this);
	}
	
	public boolean hasParent() { return getParent().isPresent(); }
	public boolean hasChild() { return getChild().isPresent(); }
}
