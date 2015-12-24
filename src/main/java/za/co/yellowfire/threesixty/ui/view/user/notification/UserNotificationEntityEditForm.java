package za.co.yellowfire.threesixty.ui.view.user.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@Component
@SuppressWarnings("serial")
public class UserNotificationEntityEditForm extends AbstractEntityEditForm<UserNotification> {
	
	@Autowired
	@PropertyId("createdBy")
	private ComboBox createdByField;
	
	@Autowired
	@PropertyId("createdAt")
	private DateField createdAtField;
	
	@Autowired
	@PropertyId("lastModifiedBy")
	private ComboBox modifiedByField;
	
	@Autowired
	@PropertyId("lastModifiedAt")
	private DateField modifiedAtField;
	
	@Autowired @Qualifier("notification")
	@PropertyId("category")
	private ComboBox categoryField;
	
	@Autowired @Qualifier("notification")
	@PropertyId("action")
	private TextField actionField;
	
	@Autowired @Qualifier("notification")
	@PropertyId("time")
	private DateField timeField;
	
	@Autowired @Qualifier("notification")
	@PropertyId("content")
	private TextArea contentField;
	
	@Autowired @Qualifier("notification")
	@PropertyId("user")
	private ComboBox userField;
		
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
	protected void updateFieldContraints() {
		super.updateFieldContraints();
	
	
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
