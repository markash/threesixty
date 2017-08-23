package za.co.yellowfire.threesixty.ui.view.kudos;

import com.vaadin.ui.Label;
import org.vaadin.viritin.fields.MTextField;
import za.co.yellowfire.threesixty.domain.kudos.Ideal;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class IdealEntityEditForm extends AbstractEntityEditForm<Ideal> {

	private MTextField descriptionField = 
		new MTextField(I8n.Fields.DESCRIPTION)
			.withFullWidth();

	IdealEntityEditForm() {
		super(Ideal.class);

		getBinder().bind(descriptionField, "description");
	}

	@Override
	protected void internalLayout() {
		
		addComponent(PanelBuilder.FORM(
        		idField, descriptionField,
        		PanelBuilder.HORIZONTAL(new Label(""), new Label(""))
        ));
        addComponent(new Label(""));
	}
	
//	@Override
//	protected void updateFieldConstraints() {
//		super.updateFieldConstraints();
//	}

	@Override
	protected Ideal buildEmpty() {
		return Ideal.EMPTY();
	}	
}
