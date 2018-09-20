package za.co.yellowfire.threesixty.ui.view.notification;

import com.github.markash.ui.view.AbstractEntityEditForm;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;
import za.co.yellowfire.threesixty.domain.user.notification.NotificationCategory;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.ui.I8n;

import java.util.Arrays;

@SuppressWarnings("serial")
public class UserNotificationEntityEditForm extends AbstractEntityEditForm<UserNotification> {
	
	private ComboBox<User> createdByField;
	private ComboBox<User> modifiedByField;
	private DateField createdAtField = new DateField(I8n.Fields.CREATED_TIME);
	private DateField modifiedAtField = new DateField(I8n.Fields.MODIFIED_TIME);
	private ComboBox<NotificationCategory> categoryField;
	private MTextField actionField;
	private DateField timeField;
	private TextArea contentField;
	private ComboBox<User> userField;

	UserNotificationEntityEditForm(
			final UserRepository userRepository) {

		super(UserNotification.class);

		ListDataProvider<User> userDataProvider = new ListDataProvider<>(userRepository.findByActive(true));
		ListDataProvider<NotificationCategory> categoryDataProvider = new ListDataProvider<>(Arrays.asList(NotificationCategory.values()));

		this.userField = new ComboBox<>(I8n.Notifications.Columns.USER);
		this.userField.setDataProvider(userDataProvider);
		this.userField.setWidth(100.0f, Unit.PERCENTAGE);
		this.userField.setReadOnly(true);
		this.userField.setEnabled(false);

		this.createdByField = new ComboBox<>(I8n.Fields.CREATED_BY);
		this.createdByField.setDataProvider(userDataProvider);
		this.createdByField.setWidth(100.0f, Unit.PERCENTAGE);
		this.createdByField.setReadOnly(true);
		this.createdByField.setEnabled(false);

		this.modifiedByField = new ComboBox<>(I8n.Fields.MODIFIED_BY);
		this.modifiedByField.setDataProvider(userDataProvider);
		this.modifiedByField.setWidth(100.0f, Unit.PERCENTAGE);
		this.modifiedByField.setReadOnly(true);
		this.modifiedByField.setEnabled(false);

		this.categoryField = new ComboBox<>(I8n.Notifications.Columns.CATEGORY);
		this.categoryField.setDataProvider(categoryDataProvider);
		this.categoryField.setWidth(100.0f, Unit.PERCENTAGE);
		this.categoryField.setReadOnly(true);
		this.categoryField.setEnabled(false);

		this.timeField = new DateField(I8n.Notifications.Columns.TIME);
		this.timeField.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		this.timeField.setWidth(100.0f, Unit.PERCENTAGE);
		this.timeField.setEnabled(false);
		
		this.contentField = new TextArea(I8n.Notifications.Columns.CONTENT);
		this.contentField.setWidth(100.0f, Unit.PERCENTAGE);
		this.contentField.setEnabled(false);

		this.actionField = new MTextField(I8n.Notifications.Columns.ACTION).withFullWidth();
		this.actionField.setEnabled(false);
			
		this.modifiedAtField.setEnabled(false);
		this.createdAtField.setEnabled(false);

		getBinder().forField(createdByField).bind(UserNotification.FIELD_CREATED_BY);
//		getBinder().forField(createdAtField).bind(UserNotification.FIELD_CREATED_DATE);
		getBinder().forField(modifiedByField).bind(UserNotification.FIELD_LAST_MODIFIED_BY);
//		getBinder().forField(modifiedAtField).bind(UserNotification.FIELD_LAST_MODIFIED_DATE);
		getBinder().forField(contentField).bind(UserNotification.FIELD_CONTENT);
		getBinder().forField(categoryField).bind(UserNotification.FIELD_CATEGORY);
//		getBinder().forField(timeField).bind(UserNotification.FIELD_TIME);
		getBinder().forField(actionField).bind(UserNotification.FIELD_ACTION);
		getBinder().forField(userField).bind(UserNotification.FIELD_USER);
	}

	@Override
	protected void internalLayout() {

		addComponent(new MVerticalLayout()
				.withSpacing(true)
				.withMargin(false)
				.withWidth(100.0f, Unit.PERCENTAGE)
				.with(
						getIdField(),
						new MHorizontalLayout()
								.withSpacing(true)
								.withMargin(false)
								.withWidth(100.0f, Unit.PERCENTAGE)
								.with(categoryField, actionField),
						new MHorizontalLayout()
								.withSpacing(true)
								.withMargin(false)
								.withWidth(100.0f, Unit.PERCENTAGE)
								.with(timeField, userField),
						contentField,
						new MHorizontalLayout()
								.withSpacing(true)
								.withMargin(false)
								.withWidth(100.0f, Unit.PERCENTAGE)
								.with(createdByField, createdAtField),
						new MHorizontalLayout()
								.withSpacing(true)
								.withMargin(false)
								.withWidth(100.0f, Unit.PERCENTAGE)
								.with(modifiedByField, modifiedAtField)
				)
		);
	}
}
