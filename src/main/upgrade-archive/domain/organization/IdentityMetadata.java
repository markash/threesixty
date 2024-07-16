package za.co.yellowfire.threesixty.domain.organization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class IdentityMetadata {
	private final List<OrganizationLevelMetadata> levels;

	public IdentityMetadata() {
		this.levels = new ArrayList<>();
		this.levels.add(new OrganizationLevelMetadata("Global", IdentityType.Global, this));
		this.levels.add(new OrganizationLevelMetadata("Tenant", IdentityType.Tenant, this));
		this.levels.add(new OrganizationLevelMetadata("Identity", IdentityType.Organization, this));
		this.levels.add(new OrganizationLevelMetadata("Region", IdentityType.Region, this));
		this.levels.add(new OrganizationLevelMetadata("Division", IdentityType.Division, this));
		this.levels.add(new OrganizationLevelMetadata("Department", IdentityType.Department, this));
		this.levels.add(new OrganizationLevelMetadata("Team", IdentityType.Team, this));
		this.levels.add(new OrganizationLevelMetadata("Group", IdentityType.Group, this));
		this.levels.add(new OrganizationLevelMetadata("Individual", IdentityType.Individual, this));
	}
	
	public int getNumberOflevels() {
		return this.levels.size();
	}
	
	public List<OrganizationLevelMetadata> getLevels() {
		return levels;
	}

	public OrganizationLevelMetadata getRootMetadata() {
		return this.levels.get(0);
	}
	
	public Optional<OrganizationLevelMetadata> getMetadata(IdentityType type) {
		return this.levels.stream().filter(o -> o.getType() == type).findFirst();
	}
	
	public Optional<OrganizationLevelMetadata> getPreviousLevelMetadata(OrganizationLevelMetadata level) {
		
		OrganizationLevelMetadata previous = null;
		for (OrganizationLevelMetadata meta : this.levels) {
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
