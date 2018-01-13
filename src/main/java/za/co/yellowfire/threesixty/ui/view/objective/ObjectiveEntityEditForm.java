package za.co.yellowfire.threesixty.ui.view.objective;

import com.vaadin.ui.CheckBox;
import io.threesixty.ui.view.AbstractEntityEditForm;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.rating.Objective;
import za.co.yellowfire.threesixty.ui.I8n;

@SuppressWarnings("serial")
public class ObjectiveEntityEditForm extends AbstractEntityEditForm<Objective>  {

    private final MTextField nameField = new MTextField(I8n.Objective.Columns.NAME).withFullWidth();
    private final MTextField textField = new MTextField(I8n.Objective.Columns.TEXT).withFullWidth();
    private CheckBox activeField = new CheckBox(I8n.Period.Fields.ACTIVE);

	ObjectiveEntityEditForm() {
		super(Objective.class);

        getBinder().forField(nameField).asRequired(I8n.Objective.Validation.NAME_REQUIRED).bind(Objective.FIELD_NAME);
        getBinder().forField(textField).bind(Objective.FIELD_TEXT);
        getBinder().forField(activeField).bind(Objective.FIELD_ACTIVE);

        nameField.setRequiredIndicatorVisible(true);
	}

	@Override
	protected void internalLayout() {

		addComponent(new MVerticalLayout()
				.withSpacing(true)
				.withMargin(false)
				.withWidth(100.0f, Unit.PERCENTAGE)
				.with(nameField, textField, activeField));
	}
}
