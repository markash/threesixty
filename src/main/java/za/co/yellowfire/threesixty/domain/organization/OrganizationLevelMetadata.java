package za.co.yellowfire.threesixty.domain.organization;

import java.util.Optional;

public class OrganizationLevelMetadata {
	private String name;
	private OrganizationType type;
	private OrganizationMetadata organizationMetadata;
	
	public OrganizationLevelMetadata() {
	}

	public OrganizationLevelMetadata(final String name, final OrganizationType type, final OrganizationMetadata organizationMetadata) {
		this.name = name;
		this.type = type;
		this.organizationMetadata = organizationMetadata;
	}

	public String getName() { return name; }
	public OrganizationType getType() { return type; }
	public OrganizationMetadata getOrganizationMetadata() { return organizationMetadata; }
	
	public void setName(String name) { this.name = name; }
	public void setType(OrganizationType type) { this.type = type; }
	public void setOrganizationMetadata(OrganizationMetadata organizationMetadata) { this.organizationMetadata = organizationMetadata; }

	public Optional<OrganizationLevelMetadata> getParent() {
		if (this.organizationMetadata == null) {
			return Optional.empty();
		}
		
		return this.organizationMetadata.getPreviousLevelMetadata(this);
	}
	
	public Optional<OrganizationLevelMetadata> getChild() {
		if (this.organizationMetadata == null) {
			return Optional.empty();
		}
		
		return this.organizationMetadata.getNextLevelMetadata(this);
	}
	
	public boolean hasParent() { return getParent().isPresent(); }
	public boolean hasChild() { return getChild().isPresent(); }
}
