package za.co.yellowfire.threesixty.ui.view.discipline;

import com.github.markash.ui.view.AbstractEntityEditForm;
import com.github.markash.ui.view.AbstractEntityEditView;
import com.vaadin.flow.router.Route;
import com.vaadin.ui.CheckBox;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.rating.Discipline;
import za.co.yellowfire.threesixty.domain.rating.DisciplineRepository;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.MongoDbEntityFindByIdSupplier;
import za.co.yellowfire.threesixty.ui.component.MongoDbEntityPersistFunction;


@Route(value = DisciplineEditView.VIEW_NAME)
public class DisciplineEditView extends AbstractEntityEditView<Discipline> {

	public static final String TITLE = I8n.Discipline.SINGULAR;
	public static final String VIEW_NAME = "discipline";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); }

    @Autowired
    public DisciplineEditView(
            final DisciplineRepository disciplineRepository) {

        super(
                TITLE,
                new DisciplineEntityEditForm(),
                new MongoDbEntityFindByIdSupplier<>(disciplineRepository),
		        Discipline::new,
                new MongoDbEntityPersistFunction<>(disciplineRepository)
        );
    }

    @Override
    protected String successfulPersistNotification(
            final Discipline entity) {

        return entity.getName() + " successfully persisted.";
    }

    private static class DisciplineEntityEditForm extends AbstractEntityEditForm<Discipline> {


        DisciplineEntityEditForm() {

            super(Discipline.class, true);

            MTextField nameField = new MTextField(I8n.Discipline.Columns.NAME).withFullWidth();
            nameField.setRequiredIndicatorVisible(true);

            MTextField textField = new MTextField(I8n.Discipline.Columns.TEXT).withFullWidth();

            CheckBox activeField = new CheckBox(I8n.Discipline.Columns.ACTIVE);

            getBinder().forField(nameField).asRequired(I8n.Discipline.Validation.NAME_REQUIRED).bind(Discipline.FIELD_NAME);
            getBinder().forField(textField).bind(Discipline.FIELD_TEXT);
            getBinder().forField(activeField).bind(Discipline.FIELD_ACTIVE);

            addComponent(new MVerticalLayout()
                    .withSpacing(true)
                    .withMargin(false)
                    .withWidth(100.0f, Unit.PERCENTAGE)
                    .with(nameField, textField, activeField));
        }
    }
}

