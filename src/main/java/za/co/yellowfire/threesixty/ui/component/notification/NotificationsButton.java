package za.co.yellowfire.threesixty.ui.component.notification;

import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.domain.user.notification.NotificationSummary;
import za.co.yellowfire.threesixty.ui.DashboardEvent.NotificationsCountUpdatedEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;

public class NotificationsButton extends Button {
	private static final long serialVersionUID = 1L;
    public static final String ID = "dashboard-notifications";
    
    private final UserService userService;
    private ClickListener viewNotificationsClickListner;
    private Window notificationsWindow;
    
    @SuppressWarnings("serial")
	protected NotificationsButton(final UserService userService, final String notificationsView) {
    	this(
    			userService, 
    			new ClickListener() { 
    				@Override
			    	public void buttonClick(ClickEvent event) {
    					UI.getCurrent().getNavigator().navigateTo(notificationsView);
			    		
			    	}}
    			);
    }
    
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
            addStyleName(Style.Notification.STYLE_UNREAD);
            description += " (" + count + " unread)";
        } else {
            removeStyleName(Style.Notification.STYLE_UNREAD);
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
    
    public static NotificationsButton build(
    		final String id, 
    		final UserService userService, 
    		final String caption, 
    		final FontAwesome fontIcon, 
    		final String notificationsView) {
    	NotificationsButton button = new NotificationsButton(userService, notificationsView);
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
	
    public static NotificationsButton build(final String id, 
    		final UserService userService, 
    		final String caption, 
    		final FontAwesome fontIcon, 
    		final String notificationsView, 
    		final String...styles) {
    	NotificationsButton button = build(id, userService, caption, fontIcon, notificationsView);
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
    
    public static NotificationsButton BELL(final String id, final UserService userService, final String notificationsView) {
		return build(
				id, 
				userService, 
				BUTTON_BELL, 
				FontAwesome.BELL, 
				notificationsView, 
				"notifications", ValoTheme.BUTTON_ICON_ONLY);	
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
    
//    protected void onReadMore(final ClickEvent event) {
//    	
//    }
    
    protected void onClearAllNotifications(final ClickEvent event) {
    	this.userService.clearNotifications(this.userService.getCurrentUser());
    	DashboardEventBus.post(new NotificationsCountUpdatedEvent());
    }
    
    protected void onOpenNotificationsPopup(final ClickEvent event) {
        VerticalLayout notificationsLayout = new VerticalLayout();
        notificationsLayout.setMargin(true);
        notificationsLayout.setSpacing(true);

        
        Label title = LabelBuilder.build(I8n.Notifications.HEADER, ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN, Style.Notification.HEADER);
        Button clearAllButton = ButtonBuilder.CLEAR_ALL(this::onClearAllNotifications, ValoTheme.BUTTON_BORDERLESS_COLORED, ValoTheme.BUTTON_SMALL);
        
        HorizontalLayout titlePanel = PanelBuilder.HORIZONTAL(Style.Notification.HEADER, title, clearAllButton);
        titlePanel.addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR);
        titlePanel.setExpandRatio(title, 2.0f);
        titlePanel.setExpandRatio(clearAllButton, 1.0f);
    	
        notificationsLayout.addComponent(titlePanel);
        
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
        	
        	Label icon = LabelBuilder.build(font.getHtml(), ContentMode.HTML, Style.Notification.ICON);
        	
        	Layout content = PanelBuilder.VERTICAL(
    				LabelBuilder.build(summary.getTitle(), ContentMode.HTML, Style.Notification.TITLE),
    				LabelBuilder.blank()
    				//ButtonBuilder.build("Read more", null, this::onReadMore, ValoTheme.BUTTON_BORDERLESS_COLORED, ValoTheme.BUTTON_SMALL, Style.Notification.CONTENT)
    				);
        	
        	Label time = LabelBuilder.build("Multi", Style.Notification.TIME);
        	
        	HorizontalLayout notification = PanelBuilder.HORIZONTAL(Style.Notification.ITEM, icon, content, time);
        	notification.setExpandRatio(icon, 1.0f);
        	notification.setExpandRatio(content, 5.0f);
        	notification.setExpandRatio(time, 1.0f);
        	
        	notificationsLayout.addComponent(notification);
        	
        }

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        
        Button showAll = ButtonBuilder.build(
        		I8n.Notifications.BUTTON_VIEW_ALL, 
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
        } else {
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