package za.co.yellowfire.threesixty.ui.view.org;

import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.fieldgroup.PropertyId;

import za.co.yellowfire.threesixty.domain.organization.Organization;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

public class OrganizationEntityEditForm extends AbstractEntityEditForm<Organization> {
	private static final long serialVersionUID = 1L;

	@PropertyId("name")
	private MTextField nameField = 
		new MTextField(I8n.Organization.Fields.NAME)
			.withFullWidth()
			.withNullRepresentation("");
	
	@Override
	protected Organization buildEmpty() {
		return new Organization();
	}

	@Override
	protected void internalLayout() {
		addComponent(PanelBuilder.FORM(nameField));
	}
}
