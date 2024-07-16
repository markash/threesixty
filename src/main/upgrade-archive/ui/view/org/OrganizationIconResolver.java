package za.co.yellowfire.threesixty.ui.view.org;

import com.vaadin.icons.VaadinIcons;
import za.co.yellowfire.threesixty.domain.organization.OrganizationLevelMetadata;
import za.co.yellowfire.threesixty.ui.I8n;

public class OrganizationIconResolver {

	VaadinIcons getIcon(OrganizationLevelMetadata metadata) {
		if (metadata != null) {
			switch (metadata.getType()) {
				case IdentityType.Organization: return I8n.Identity.Level.ORGANIZATION;
				case IdentityType.Region: return I8n.Identity.Level.REGION;
				case IdentityType.Group: return I8n.Identity.Level.GROUP;
				case IdentityType.Division: return I8n.Identity.Level.DIVISION;
				case IdentityType.Department: return I8n.Identity.Level.DEPARTMENT;
				case IdentityType.Team: return I8n.Identity.Level.TEAM;
				case IdentityType.Individual: return I8n.Identity.Level.INDIVIDUAL;
				default: return I8n.Identity.Level.OTHER;
			}
		} else {
			return I8n.Identity.Level.OTHER;
		}
	}
}
