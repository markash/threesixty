package za.co.yellowfire.threesixty.ui.view.org;

import com.vaadin.icons.VaadinIcons;
import za.co.yellowfire.threesixty.domain.organization.OrganizationLevelMetadata;
import za.co.yellowfire.threesixty.ui.I8n;

class OrganizationIconResolver {

	VaadinIcons getIcon(OrganizationLevelMetadata metadata) {
		if (metadata != null) {
			switch (metadata.getType()) {
			case Organization: return I8n.Organization.Level.ORGANIZATION;
			case Region: return I8n.Organization.Level.REGION;
			case Group: return I8n.Organization.Level.GROUP;
			case Division: return I8n.Organization.Level.DIVISION;
			case Department: return I8n.Organization.Level.DEPARTMENT;
			case Team: return I8n.Organization.Level.TEAM;
			default: return I8n.Organization.Level.OTHER;
			}
		} else {
			return I8n.Organization.Level.OTHER;
		}
	}
}
