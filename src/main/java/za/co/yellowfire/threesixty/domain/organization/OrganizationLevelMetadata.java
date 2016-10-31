package za.co.yellowfire.threesixty.domain.organization;

public class OrganizationLevelMetadata {
	private String name;
	private OrganizationType type;
	
	public OrganizationLevelMetadata() {
	}

	public OrganizationLevelMetadata(String name, OrganizationType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public OrganizationType getType() {
		return type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(OrganizationType type) {
		this.type = type;
	}
}
