package za.co.yellowfire.threesixty.domain.organization;

import java.util.Arrays;
import java.util.List;

public enum IdentityType {
	Global,
	Tenant,
	Organization,
	Region,
	Division,
	Department,
	Team,
	Group,
	Individual;
	
	public static List<IdentityType> getValues() {
		return Arrays.asList(
				IdentityType.Global,
				IdentityType.Tenant,
				IdentityType.Organization,
				IdentityType.Region,
				IdentityType.Division,
				IdentityType.Department,
				IdentityType.Team,
				IdentityType.Group,
				IdentityType.Individual
				);
	}
}
