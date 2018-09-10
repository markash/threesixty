package za.co.yellowfire.threesixty.ui.view.org;

import com.vaadin.icons.VaadinIcons;
import za.co.yellowfire.threesixty.domain.organization.OrganizationLevelMetadata;
import za.co.yellowfire.threesixty.ui.I8n;

class OrganizationIconResolver {

	VaadinIcons getIcon(OrganizationLevelMetadata metadata) {
		if (metadata != null) {
			switch (metadata.getType()) {
				case Organization: return I8n.Identity.Level.ORGANIZATION;
				case Region: return I8n.Identity.Level.REGION;
				case Group: return I8n.Identity.Level.GROUP;
				case Division: return I8n.Identity.Level.DIVISION;
				case Department: return I8n.Identity.Level.DEPARTMENT;
				case Team: return I8n.Identity.Level.TEAM;
				case Individual: return I8n.Identity.Level.INDIVIDUAL;
				default: return I8n.Identity.Level.OTHER;
			}
		} else {
			return I8n.Identity.Level.OTHER;
		}
	}
}
