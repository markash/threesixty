package za.co.yellowfire.threesixty.ui.view.discipline;

import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.view.AbstractEntityEditForm;
import com.github.markash.ui.view.AbstractEntityEditView;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CheckBox;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.rating.Discipline;
import za.co.yellowfire.threesixty.domain.rating.DisciplineRepository;
import za.co.yellowfire.threesixty.ui.I8n;

import java.util.Optional;


@SpringView(name = DisciplineEditView.VIEW_NAME)
public class DisciplineEditView extends AbstractEntityEditView<String, Discipline> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Discipline.SINGULAR;
	public static final String VIEW_NAME = "discipline";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); }


    @Autowired
    public DisciplineEditView(
            final DisciplineRepository disciplineRepository) {

        super(TITLE, new DisciplineEntityEditForm());

        setBlankSupplier(Discipline::new);
        setEntitySupplier(id -> Optional.ofNullable(disciplineRepository.findOne((String) id)));
        setEntityPersistFunction(entity -> {
            try {
                return disciplineRepository.save(entity);
            } catch (Throwable e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return entity;
        });
    }

    @Override
    protected String successfulPersistNotification(
            final Discipline entity) {

        return entity.getName() + " successfully persisted.";
    }

    private static class DisciplineEntityEditForm extends AbstractEntityEditForm<String, Discipline> {


        DisciplineEntityEditForm() {

            super(Discipline.class, String.class);

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

