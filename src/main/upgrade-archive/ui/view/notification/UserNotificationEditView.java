package za.co.yellowfire.threesixty.ui.view.notification;

import com.github.markash.ui.view.AbstractEntityEditForm;
import com.github.markash.ui.view.AbstractEntityEditView;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextArea;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;
import za.co.yellowfire.threesixty.domain.user.notification.NotificationCategory;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotificationRepository;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.MongoDbEntityFindByIdSupplier;
import za.co.yellowfire.threesixty.ui.component.MongoDbEntityPersistFunction;

import java.util.Arrays;

@Route(value = UserNotificationEditView.VIEW_NAME)
public final class UserNotificationEditView extends AbstractEntityEditView<UserNotification> {

	public static final String TITLE = I8n.Notifications.Views.Edit.TITLE;
	public static final String VIEW_NAME = "notification";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static String VIEW(final String entity) { return VIEW_NAME + (StringUtils.isBlank(entity) ? "" : "/" + entity); }

    @Autowired
    public UserNotificationEditView(
    		final UserRepository userRepository,
    		final UserNotificationRepository userNotificationRepository) {

    	super(
				TITLE,
			    new UserNotificationEntityEditForm(userRepository),
			    new MongoDbEntityFindByIdSupplier<>(userNotificationRepository),
			    UserNotification::new,
			    new MongoDbEntityPersistFunction<>(userNotificationRepository));
    }

	@Override
	protected String successfulPersistNotification(
			final UserNotification entity) {

		return "Notification created for " +
				entity.getAction() +
				" in category " +
				entity.getCategory() +
				" successfully persisted.";
	}

	private static class UserNotificationEntityEditForm extends AbstractEntityEditForm<UserNotification> {


		UserNotificationEntityEditForm(
				final UserRepository userRepository) {

			super(UserNotification.class);

			ListDataProvider<User> userDataProvider = new ListDataProvider<>(userRepository.findByActive(true));
			ListDataProvider<NotificationCategory> categoryDataProvider = new ListDataProvider<>(Arrays.asList(NotificationCategory.values()));

			ComboBox<User> createdByField;
			ComboBox<User> modifiedByField;
			DateField createdAtField = new DateField(I8n.Fields.CREATED_TIME);
			DateField modifiedAtField = new DateField(I8n.Fields.MODIFIED_TIME);
			ComboBox<NotificationCategory> categoryField;
			MTextField actionField;
			DateField timeField;
			TextArea contentField;
			ComboBox<User> userField;

			userField = new ComboBox<>(I8n.Notifications.Columns.USER);
			userField.setDataProvider(userDataProvider);
			userField.setWidth(100.0f, Unit.PERCENTAGE);
			userField.setReadOnly(true);
			userField.setEnabled(false);

			createdByField = new ComboBox<>(I8n.Fields.CREATED_BY);
			createdByField.setDataProvider(userDataProvider);
			createdByField.setWidth(100.0f, Unit.PERCENTAGE);
			createdByField.setReadOnly(true);
			createdByField.setEnabled(false);

			modifiedByField = new ComboBox<>(I8n.Fields.MODIFIED_BY);
			modifiedByField.setDataProvider(userDataProvider);
			modifiedByField.setWidth(100.0f, Unit.PERCENTAGE);
			modifiedByField.setReadOnly(true);
			modifiedByField.setEnabled(false);

			categoryField = new ComboBox<>(I8n.Notifications.Columns.CATEGORY);
			categoryField.setDataProvider(categoryDataProvider);
			categoryField.setWidth(100.0f, Unit.PERCENTAGE);
			categoryField.setReadOnly(true);
			categoryField.setEnabled(false);

			timeField = new DateField(I8n.Notifications.Columns.TIME);
			timeField.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			timeField.setWidth(100.0f, Unit.PERCENTAGE);
			timeField.setEnabled(false);

			contentField = new TextArea(I8n.Notifications.Columns.CONTENT);
			contentField.setWidth(100.0f, Unit.PERCENTAGE);
			contentField.setEnabled(false);

			actionField = new MTextField(I8n.Notifications.Columns.ACTION).withFullWidth();
			actionField.setEnabled(false);

			modifiedAtField.setEnabled(false);
			createdAtField.setEnabled(false);

			getBinder().forField(createdByField).bind(UserNotification.FIELD_CREATED_BY);
//		getBinder().forField(createdAtField).bind(UserNotification.FIELD_CREATED_DATE);
			getBinder().forField(modifiedByField).bind(UserNotification.FIELD_LAST_MODIFIED_BY);
//		getBinder().forField(modifiedAtField).bind(UserNotification.FIELD_LAST_MODIFIED_DATE);
			getBinder().forField(contentField).bind(UserNotification.FIELD_CONTENT);
			getBinder().forField(categoryField).bind(UserNotification.FIELD_CATEGORY);
//		getBinder().forField(timeField).bind(UserNotification.FIELD_TIME);
			getBinder().forField(actionField).bind(UserNotification.FIELD_ACTION);
			getBinder().forField(userField).bind(UserNotification.FIELD_USER);

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
}


