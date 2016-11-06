package za.co.yellowfire.threesixty.domain.organization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class OrganizationMetadata {
	private List<OrganizationLevelMetadata> levels;
	private OrganizationLevelMetadata defaultLevel;
	
	public OrganizationMetadata() {
		this.levels = new ArrayList<>();
		this.levels.add(new OrganizationLevelMetadata("Organization", OrganizationType.Organization, this));
		this.levels.add(new OrganizationLevelMetadata("Region", OrganizationType.Region, this));
		this.levels.add(new OrganizationLevelMetadata("Division", OrganizationType.Division, this));
		this.levels.add(new OrganizationLevelMetadata("Department", OrganizationType.Department, this));
		this.levels.add(new OrganizationLevelMetadata("Team", OrganizationType.Team, this));
		this.levels.add(new OrganizationLevelMetadata("Group", OrganizationType.Group, this));
	}
	
	public int getNumberOflevels() {
		return this.levels.size();
	}
	
	public List<OrganizationLevelMetadata> getLevels() {
		return levels;
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
	
	public Optional<OrganizationLevelMetadata> getPreviousLevelMetadata(OrganizationLevelMetadata level) {
		
		OrganizationLevelMetadata previous = null;
		Iterator<OrganizationLevelMetadata> i = this.levels.iterator();
		while (i.hasNext()) {
			OrganizationLevelMetadata meta = i.next();
			if (meta.getName().equals(level.getName()) && meta.getType() == level.getType()) {
				return Optional.ofNullable(previous);
			}
			previous = meta;
		}
		return Optional.empty();
	}

	public Optional<OrganizationLevelMetadata> getNextLevelMetadata(OrganizationLevelMetadata level) {
		
		Iterator<OrganizationLevelMetadata> i = this.levels.iterator();
		while (i.hasNext()) {
			OrganizationLevelMetadata meta = i.next();
			if (meta.getName().equals(level.getName()) && meta.getType() == level.getType()) {
				return (i.hasNext() ? Optional.of(i.next()) : Optional.empty());
			}
		}
		return Optional.empty();
	}
}
