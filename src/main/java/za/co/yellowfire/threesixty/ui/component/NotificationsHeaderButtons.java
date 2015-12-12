package za.co.yellowfire.threesixty.ui.component;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.DashboardNotification;
import za.co.yellowfire.threesixty.ui.DashboardEvent.NotificationsCountUpdatedEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;

public class NotificationsHeaderButtons extends HorizontalLayout {
	private static final long serialVersionUID = 1L;

	private final NotificationsButton notificationsButton;
	private final Button editButton;
	private Window notificationsWindow;
	
	/**
	 * 
	 * @param editButtonId The id of the edit button. If null then the button is not added to the header
	 */
	public NotificationsHeaderButtons(final String editButtonId) {
		this.notificationsButton = buildNotificationsButton();
        this.editButton = StringUtils.isNotBlank(editButtonId) ? buildEditButton(editButtonId) : null;
        
        addComponent(notificationsButton);
        
        if (this.editButton != null) {
        	addComponent(editButton);
        }
        
        setSpacing(true);
        addStyleName("toolbar");
	}
	
	private NotificationsButton buildNotificationsButton() {
        NotificationsButton result = new NotificationsButton();
        result.addClickListener(event -> { openNotificationsPopup(event); });
        return result;
    }
	
	private Button buildEditButton(final String editButtonId) {
        Button result = new Button();
        result.setId(editButtonId);
        result.setIcon(FontAwesome.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("Edit Dashboard");
//        result.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(final ClickEvent event) {
//                getUI().addWindow(
//                        new DashboardEdit(DashboardView.this, titleLabel
//                                .getValue()));
//            }
//        });
        return result;
    }
	
	private void openNotificationsPopup(final ClickEvent event) {
        VerticalLayout notificationsLayout = new VerticalLayout();
        notificationsLayout.setMargin(true);
        notificationsLayout.setSpacing(true);

        Label title = new Label("Notifications");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        notificationsLayout.addComponent(title);

        Collection<DashboardNotification> notifications = MainUI.getDataProvider().getNotifications();
        DashboardEventBus.post(new NotificationsCountUpdatedEvent());

        for (DashboardNotification notification : notifications) {
            VerticalLayout notificationLayout = new VerticalLayout();
            notificationLayout.addStyleName("notification-item");

            Label titleLabel = new Label(notification.getFirstName() + " "
                    + notification.getLastName() + " "
                    + notification.getAction());
            titleLabel.addStyleName("notification-title");

            Label timeLabel = new Label(notification.getPrettyTime());
            timeLabel.addStyleName("notification-time");

            Label contentLabel = new Label(notification.getContent());
            contentLabel.addStyleName("notification-content");

            notificationLayout.addComponents(titleLabel, timeLabel,
                    contentLabel);
            notificationsLayout.addComponent(notificationLayout);
        }

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        Button showAll = new Button("View All Notifications", e -> { Notification.show("Not implemented in this demo"); });
        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAll.addStyleName(ValoTheme.BUTTON_SMALL);
        footer.addComponent(showAll);
        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        notificationsLayout.addComponent(footer);

        if (notificationsWindow == null) {
            notificationsWindow = new Window();
            notificationsWindow.setWidth(300.0f, Unit.PIXELS);
            notificationsWindow.addStyleName("notifications");
            notificationsWindow.setClosable(false);
            notificationsWindow.setResizable(false);
            notificationsWindow.setDraggable(false);
            notificationsWindow.addCloseShortcut(KeyCode.ESCAPE, null);
            notificationsWindow.setContent(notificationsLayout);
        }

        if (!notificationsWindow.isAttached()) {
            notificationsWindow.setPositionY(event.getClientY()
                    - event.getRelativeY() + 40);
            getUI().addWindow(notificationsWindow);
            notificationsWindow.focus();
        } else {
            notificationsWindow.close();
        }
    }
}
