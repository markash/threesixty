package za.co.yellowfire.threesixty.ui.view.org;

import com.vaadin.server.Resource;
import com.vaadin.ui.Layout;
import za.co.yellowfire.threesixty.domain.organization.Identity;

import java.util.List;
import java.util.stream.Collectors;

public class IdentityModel {

	private final Identity identity;
	private OrganizationIconResolver iconResolver;
	private Layout form;
	
	IdentityModel(final Identity identity, final OrganizationIconResolver iconResolver) {
		this.identity = identity;
		this.iconResolver = iconResolver;
	}
	
	public void setForm(final Layout form) {
		this.form = form;
	}
	
	public Layout getForm() {
		return this.form;
	}
	
	public String getName() {
		return identity.getName();
	}
	
	boolean hasChildren() {
		return identity.hasChildren();
	}
	
	public Identity getIdentity() {
		return identity;
	}

	List<IdentityModel> getChildren() {
		return identity.getChildren()
				.stream()
				.map(o -> new IdentityModel(o, this.iconResolver))
				.collect(Collectors.toList());
	}
	
	public Resource getIcon() {
		return iconResolver.getIcon(identity.getMetadata().orElse(null));
	}
	
	public String toString() {
		return this.identity.getName();
	}
}
