package za.co.yellowfire.threesixty.ui.view.user.notification;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import za.co.yellowfire.threesixty.domain.user.UserRepository;
import za.co.yellowfire.threesixty.domain.user.notification.NotificationCategory;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

import java.util.Arrays;

@SuppressWarnings("serial")
public class UserNotificationEntityEditForm extends AbstractEntityEditForm<UserNotification> {
	
	@PropertyId("createdBy")
	private ComboBox createdByField;

	@PropertyId("createdAt")
	private MDateField createdAtField = new MDateField(I8n.Fields.CREATED_TIME).withFullWidth();
	
	@PropertyId("lastModifiedBy")
	private ComboBox modifiedByField;
	
	@PropertyId("lastModifiedAt")
	private MDateField modifiedAtField = new MDateField(I8n.Fields.MODIFIED_TIME).withFullWidth();
	
	@PropertyId("category")
	private ComboBox categoryField;
	
	@PropertyId("action")
	private MTextField actionField;
	
	@PropertyId("time")
	private MDateField timeField;
	
	@PropertyId("content")
	private MTextArea contentField;

	@PropertyId("user")
	private ComboBox userField;
	
	public UserNotificationEntityEditForm(final UserRepository userRepository) {
		this.createdByField = 
				new ComboBox(I8n.Fields.CREATED_BY, new IndexedContainer(userRepository.findByActive(true)))
					.withWidth(100.0f, Unit.PERCENTAGE)
					.withDisabled();
		
		this.modifiedByField = 
				new ComboBox(I8n.Fields.MODIFIED_BY, new IndexedContainer(userRepository.findByActive(true)))
					.withWidth(100.0f, Unit.PERCENTAGE)
					.withDisabled();
		
		this.categoryField = 
				new ComboBox(I8n.Notifications.Fields.CATEGORY, new IndexedContainer(Arrays.asList(NotificationCategory.values())))
					.withWidth(Style.Percentage._100)
					.withDisabled();

		this.timeField = new MDateField(I8n.Notifications.Fields.TIME).withFullWidth();
		this.timeField.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		this.timeField.setWidth(100.0f, Unit.PERCENTAGE);
		this.timeField.setEnabled(false);
		
		this.contentField = 
				new MTextArea(I8n.Notifications.Fields.CONTENT)
					.withFullWidth()
					.withNullRepresentation("");
		this.contentField.setEnabled(false);

		
		this.actionField = 
				new MTextField(I8n.Notifications.Fields.ACTION)
					.withFullWidth()
					.withNullRepresentation("");

		this.actionField.setEnabled(false);
			
		this.modifiedAtField.setEnabled(false);
		this.createdAtField.setEnabled(false);
	}

	@Override
	protected void internalLayout() {
		
		
		addComponent(PanelBuilder.FORM(
				idField,
				PanelBuilder.HORIZONTAL(categoryField, actionField),
				PanelBuilder.HORIZONTAL(timeField, userField)
        ));
        addComponent(
        		PanelBuilder.VERTICAL(
        				contentField,
        				PanelBuilder.HORIZONTAL(createdByField, createdAtField),
        				PanelBuilder.HORIZONTAL(modifiedByField, modifiedAtField)
        		)
        );
	}
	
	@Override
	protected void updateFieldConstraints() {
		super.updateFieldConstraints();
	
	
		AbstractField<?>[] fields = 
				new AbstractField<?>[] {
						createdByField,
						createdAtField,
						modifiedByField,
						modifiedAtField,
						categoryField,
						actionField,
						timeField,
						userField,
						contentField};
		
		
		for(AbstractField<?> field : fields) {
			field.setEnabled(false);
			//field.removeValueChangeListener(this::onValueChange);
			//field.addValueChangeListener(this::onValueChange);
		}
		
	}


	@Override
	protected UserNotification buildEmpty() {
		return UserNotification.EMPTY();
	}
}
