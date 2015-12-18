package za.co.yellowfire.threesixty.ui.component.notification;

import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.domain.user.notification.NotificationSummary;
import za.co.yellowfire.threesixty.ui.DashboardEvent.NotificationsCountUpdatedEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;

public class NotificationsButton extends Button {
	private static final long serialVersionUID = 1L;
	
	private static final String STYLE_UNREAD = "unread";
    public static final String ID = "dashboard-notifications";
    
    private final UserService userService;
    private ClickListener viewNotificationsClickListner;
    private Window notificationsWindow;
    
    protected NotificationsButton(final UserService userService, final ClickListener viewNotificationsClickListner) {
    	this.userService = userService;
    	this.viewNotificationsClickListner = viewNotificationsClickListner;
    	this.addClickListener(this::onOpenNotificationsPopup);
    	
        DashboardEventBus.register(this);
    }

    @Subscribe
    public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
        setUnreadCount(this.userService.getUnreadNotificationsCount(this.userService.getCurrentUser()));
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
    
    public static NotificationsButton build(
    		final String id, 
    		final UserService userService, 
    		final String caption, 
    		final FontAwesome fontIcon, 
    		final ClickListener listener) {
    	NotificationsButton button = new NotificationsButton(userService, listener);
    	button.setId(id);
		button.setIcon(fontIcon);
        return button;
	}
    
    public static NotificationsButton build(final String id, 
    		final UserService userService, 
    		final String caption, 
    		final FontAwesome fontIcon, 
    		final ClickListener listener, 
    		final String...styles) {
    	NotificationsButton button = build(id, userService, caption, fontIcon, listener);
		if (styles != null && styles.length > 0) {
			for(String style : styles) {
				button.addStyleName(style);
			}
		}
		
		return button;
    }
		
    public static NotificationsButton BELL(final UserService userService, final ClickListener listener) {
		return BELL(ID, userService, listener);
	}
    
    public static NotificationsButton BELL(final String id, final UserService userService, final ClickListener listener) {
		return build(
				id, 
				userService, 
				BUTTON_BELL, 
				FontAwesome.BELL, 
				listener, 
				"notifications", ValoTheme.BUTTON_ICON_ONLY);	
	}
    
    protected static final String BUTTON_BELL = "Notifications";
    
    protected void onOpenNotificationsPopup(final ClickEvent event) {
        VerticalLayout notificationsLayout = new VerticalLayout();
        notificationsLayout.setMargin(true);
        notificationsLayout.setSpacing(true);

        Label title = LabelBuilder.build("Notifications", ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
        notificationsLayout.addComponent(title);
        
        DashboardEventBus.post(new NotificationsCountUpdatedEvent());

        List<NotificationSummary> summaries = this.userService.getNotificationSummaries(this.userService.getCurrentUser());
        for (NotificationSummary summary : summaries) {

        	FontAwesome font = FontAwesome.BOOKMARK_O;
        	if (summary.getCategory() != null) {
	        	switch (summary.getCategory()) {
		        	case System: font = FontAwesome.SERVER; break;
		        	case Rating: font = FontAwesome.QUESTION_CIRCLE; break;
		        	case Kudos: font = FontAwesome.DIAMOND; break;
		        	case Alarm: font = FontAwesome.CLOCK_O; break;
		        	case Error: font = FontAwesome.AMBULANCE; break;
		        	case Message: font = FontAwesome.ENVELOPE_O; break;
		        	case Service: font = FontAwesome.SERVER; break;
		        	default: font = FontAwesome.BOOKMARK_O; break;
	        	}
        	}
        	
        	Label icon = LabelBuilder.build(font.getHtml(), ContentMode.HTML, "notification-icon");
        	
        	Layout content = PanelBuilder.VERTICAL(
    				LabelBuilder.build(summary.getTitle(), "notification-title"),
    				LabelBuilder.build("Read more", "notification-content")
    				);
        	
        	HorizontalLayout notification = PanelBuilder.HORIZONTAL("notification-item", icon, content);
        	notification.setExpandRatio(icon, 1.0f);
        	notification.setExpandRatio(content, 5.0f);
        	
        	notificationsLayout.addComponent(notification);
        	
        }

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        
        Button showAll = ButtonBuilder.build(
        		"View All Notifications", 
        		null, 
        		viewNotificationsClickListner, 
        		ValoTheme.BUTTON_BORDERLESS_COLORED, ValoTheme.BUTTON_SMALL);
        
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
            notificationsWindow.setPositionY(event.getClientY() - event.getRelativeY() + 40);
            notificationsWindow.setPositionX(event.getClientX() - event.getRelativeX() - 300);
            getUI().addWindow(notificationsWindow);
            notificationsWindow.focus();
        } else {
            notificationsWindow.close();
        }
    }
}