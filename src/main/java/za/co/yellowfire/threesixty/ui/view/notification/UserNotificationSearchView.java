package za.co.yellowfire.threesixty.ui.view.notification;

import com.github.markash.ui.security.CurrentUserProvider;
import com.github.markash.ui.view.AbstractTableSearchView;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.AbstractDataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotificationRepository;
import za.co.yellowfire.threesixty.ui.I8n;

@SideBarItem(sectionId = Sections.PROFILE, caption = UserNotificationSearchView.TITLE, order = 4)
@VaadinFontIcon(VaadinIcons.MAILBOX)
@SpringView(name = UserNotificationSearchView.VIEW_NAME)
public final class UserNotificationSearchView extends AbstractTableSearchView<UserNotification, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Notifications.Views.Search.TITLE;
	public static final String VIEW_NAME = "notifications";

	private final String currentUser;
	private final UserNotificationRepository repository;
	private final ListDataProvider<UserNotification> dataProvider;

    @Autowired
    public UserNotificationSearchView(
			final ListDataProvider<UserNotification> notificationListDataProvider,
			final TableDefinition<UserNotification> notificationTableDefinition,
			final CurrentUserProvider<User> currentUserProvider,
			final UserNotificationRepository repository) {

    	super(UserNotification.class, TITLE, notificationListDataProvider, notificationTableDefinition);

    	this.repository = repository;
		this.currentUser = currentUserProvider.get().map(User::getId).orElse("");
		this.dataProvider = notificationListDataProvider;

		getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
		getToolbar().addAction(new MButton(I8n.Button.DELETE, this::onDelete));
    }

	@SuppressWarnings("unused")
	public void onCreate(Button.ClickEvent event) { UI.getCurrent().getNavigator().navigateTo(UserNotificationEditView.VIEW("/new-entity")); }

	@SuppressWarnings("unused")
	private void onDelete(
			final Button.ClickEvent event) {

    	this.repository.delete(this.repository.findNotifications(this.currentUser));
		this.dataProvider.getItems().clear();
		this.dataProvider.refreshAll();
	}
}

