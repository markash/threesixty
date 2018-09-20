package za.co.yellowfire.threesixty.ui.view.notification;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.view.AbstractEntityEditView;
import com.vaadin.spring.annotation.SpringView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;

@SpringView(name = UserNotificationEditView.VIEW_NAME)
public final class UserNotificationEditView extends AbstractEntityEditView<UserNotification> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Notifications.Views.Edit.TITLE;
	public static final String VIEW_NAME = "notification";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static String VIEW(final String entity) { return VIEW_NAME + (StringUtils.isBlank(entity) ? "" : "/" + entity); }

    @Autowired
    public UserNotificationEditView(
			final EntitySupplier<UserNotification, Serializable> notificationSupplier,
			final BlankSupplier<UserNotification> blankNotificationSupplier,
			final EntityPersistFunction<UserNotification> notificationPersistFunction,
			final UserNotificationEntityEditForm notificationEntityEditForm) {

    	super(TITLE, notificationEntityEditForm, notificationSupplier, blankNotificationSupplier, notificationPersistFunction);
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
}

