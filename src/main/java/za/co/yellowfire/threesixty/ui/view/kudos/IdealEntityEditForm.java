package za.co.yellowfire.threesixty.ui.view.kudos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import za.co.yellowfire.threesixty.domain.kudos.Ideal;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@Component
@SuppressWarnings("serial")
public class IdealEntityEditForm extends AbstractEntityEditForm<Ideal> {
	
	@Autowired
	@PropertyId("description")
	private TextField descriptionField;
	
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
