package za.co.yellowfire.threesixty.ui.component;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;
import za.co.yellowfire.threesixty.ui.DashboardEvent.NotificationsCountUpdatedEvent;

public class NotificationsButton extends Button {
	private static final long serialVersionUID = 1L;
	
	private static final String STYLE_UNREAD = "unread";
    public static final String ID = "dashboard-notifications";

    public NotificationsButton() {
        setIcon(FontAwesome.BELL);
        setId(ID);
        addStyleName("notifications");
        addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        DashboardEventBus.register(this);
    }

    @Subscribe
    public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
        setUnreadCount(MainUI.getDataProvider().getUnreadNotificationsCount());
    }

    public void setUnreadCount(final int count) {
        setCaption(String.valueOf(count));

        String description = "Notifications";
        if (count > 0) {
            addStyleName(STYLE_UNREAD);
            description += " (" + count + " unread)";
        } else {
            removeStyleName(STYLE_UNREAD);
        }
        setDescription(description);
    }
}