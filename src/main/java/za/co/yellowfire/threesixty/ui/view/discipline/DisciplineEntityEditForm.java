package za.co.yellowfire.threesixty.ui.view.discipline;

import com.github.markash.ui.view.AbstractEntityEditForm;
import com.vaadin.ui.CheckBox;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.rating.Discipline;
import za.co.yellowfire.threesixty.ui.I8n;

@SuppressWarnings("serial")
public class DisciplineEntityEditForm extends AbstractEntityEditForm<Discipline> {

	private final MTextField nameField = new MTextField(I8n.Discipline.Columns.NAME).withFullWidth();
	private final MTextField textField = new MTextField(I8n.Discipline.Columns.TEXT).withFullWidth();
	private CheckBox activeField = new CheckBox(I8n.Discipline.Columns.ACTIVE);

	DisciplineEntityEditForm() {

		super(Discipline.class);

        getBinder().forField(nameField).asRequired(I8n.Discipline.Validation.NAME_REQUIRED).bind(Discipline.FIELD_NAME);
        getBinder().forField(textField).bind(Discipline.FIELD_TEXT);
        getBinder().forField(activeField).bind(Discipline.FIELD_ACTIVE);

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
