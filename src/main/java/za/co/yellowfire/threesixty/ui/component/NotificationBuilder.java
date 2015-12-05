package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

/**
 * Builds the notifications used by the application
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class NotificationBuilder {    
    public static void showNotification(final String caption, final String description, final int delayMsec) {
    	showNotification(caption, description, delayMsec, false);
    }
    
    public static void showNotification(final String caption, final String description, final int delayMsec, final boolean htmlContentAllowed) {
    	Notification notification = new Notification(caption);
    	notification.setDescription(description);
    	notification.setStyleName("tray dark small closable login-help");
    	notification.setPosition(Position.BOTTOM_CENTER);
    	notification.setDelayMsec(delayMsec);
    	notification.setHtmlContentAllowed(htmlContentAllowed);
    	notification.show(Page.getCurrent());
    }
}
