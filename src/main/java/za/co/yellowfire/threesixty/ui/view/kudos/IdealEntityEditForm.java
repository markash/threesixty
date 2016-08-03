package za.co.yellowfire.threesixty.ui.view.kudos;

import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.Label;

import za.co.yellowfire.threesixty.domain.kudos.Ideal;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class IdealEntityEditForm extends AbstractEntityEditForm<Ideal> {
	
	@PropertyId("description")
	private MTextField descriptionField = 
		new MTextField(I8n.Fields.DESCRIPTION)
			.withNullRepresentation("")
			.withFullWidth();
	
	@Override
	protected void internalLayout() {
		
		addComponent(PanelBuilder.FORM(
        		idField, descriptionField,
        		PanelBuilder.HORIZONTAL(new Label(""), new Label(""))
        ));
        addComponent(new Label(""));
	}
	
	@Override
	protected void updateFieldContraints() {
		super.updateFieldContraints();
	}

	@Override
	protected Ideal buildEmpty() {
		return Ideal.EMPTY();
	}	
}
