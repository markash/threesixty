package za.co.yellowfire.threesixty.ui.component;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.DashboardEvent.NotificationsCountUpdatedEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;

public class NotificationsButton extends Button {
	private static final long serialVersionUID = 1L;
	
	private static final String STYLE_UNREAD = "unread";
    public static final String ID = "dashboard-notifications";

    private User currentUser;
    private final UserService userService;
    
    public NotificationsButton(final UserService userService, final User currentUser) {
    	this.userService = userService;
    	this.currentUser = currentUser;
    	
        setIcon(FontAwesome.BELL);
        setId(ID);
        addStyleName("notifications");
        addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        DashboardEventBus.register(this);
    }

    @Subscribe
    public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
        setUnreadCount(this.userService.getUnreadNotificationsCount(this.currentUser));
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