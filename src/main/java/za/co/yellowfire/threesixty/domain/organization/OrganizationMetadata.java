package za.co.yellowfire.threesixty.domain.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrganizationMetadata {
	private int numberOflevels = 4;
	private List<OrganizationLevelMetadata> levels;
	private OrganizationLevelMetadata defaultLevel;
	
	public OrganizationMetadata() {
		this.numberOflevels = 4;
		
		this.levels = new ArrayList<>();
		this.levels.add(new OrganizationLevelMetadata("Organization", OrganizationType.Organization));
		this.levels.add(new OrganizationLevelMetadata("Division", OrganizationType.Division));
		this.levels.add(new OrganizationLevelMetadata("Department", OrganizationType.Department));
		this.levels.add(new OrganizationLevelMetadata("Team", OrganizationType.Team));
	}
	
	public int getNumberOflevels() {
		return numberOflevels;
	}
	
	public List<OrganizationLevelMetadata> getLevels() {
		return levels;
	}
	
	public void setNumberOflevels(int numberOflevels) {
		this.numberOflevels = numberOflevels;
	}
	
	public void setLevels(List<OrganizationLevelMetadata> levels) {
		this.levels = levels;
	}
	
	public OrganizationLevelMetadata getRootMetadata() {
		return this.levels.get(0);
	}
	
	public Optional<OrganizationLevelMetadata> getMetadata(OrganizationType type) {
		return this.levels.stream().filter(o -> o.getType() == type).findFirst();
	}
}
