package za.co.yellowfire.threesixty.domain.organization;

import java.util.Arrays;
import java.util.List;

public enum OrganizationType {
	Organization,
	Region,
	Division,
	Department,
	Team,
	Group;
	
	public static List<OrganizationType> getValues() {
		return Arrays.asList(
				OrganizationType.Organization,
				OrganizationType.Region,
				OrganizationType.Division,
				OrganizationType.Department,
				OrganizationType.Team,
				OrganizationType.Group
				);
	}
}
