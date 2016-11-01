package za.co.yellowfire.threesixty.ui.view.org;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.server.Resource;
import com.vaadin.ui.Layout;

import za.co.yellowfire.threesixty.domain.organization.Organization;

public class OrganizationModel {

	private final Organization organization;
	private OrganizationIconResolver iconResolver = new OrganizationIconResolver();
	private Layout form;
	
	public OrganizationModel(final Organization organization) {
		this.organization = organization;
	}
	
	public void setForm(final Layout form) {
		this.form = form;
	}
	
	public Layout getForm() {
		return this.form;
	}
	
	public String getName() {
		return organization.getName();
	}
	
	public boolean hasChildren() {
		return organization.hasChildren();
	}
	
	public Organization getOrganization() {
		return organization;
	}

	public List<OrganizationModel> getChildren() {
		return organization.getChildren()
				.stream()
				.map(o -> new OrganizationModel(o))
				.collect(Collectors.toList());
	}
	
	public Resource getIcon() {
		return iconResolver.getIcon(organization.getMetadata().orElse(null));
	}
	
	public String toString() {
		return this.organization.getName();
	}
}
