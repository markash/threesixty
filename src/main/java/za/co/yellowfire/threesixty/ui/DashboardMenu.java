package za.co.yellowfire.threesixty.ui;

import org.springframework.util.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.DashboardEvent.NotificationsCountUpdatedEvent;
import za.co.yellowfire.threesixty.ui.DashboardEvent.PostViewChangeEvent;
import za.co.yellowfire.threesixty.ui.DashboardEvent.ProfileUpdatedEvent;
import za.co.yellowfire.threesixty.ui.DashboardEvent.ReportsCountUpdatedEvent;
import za.co.yellowfire.threesixty.ui.DashboardEvent.UserLogoutEvent;
import za.co.yellowfire.threesixty.ui.component.ByteArrayStreamResource;
import za.co.yellowfire.threesixty.ui.view.UserEditView;

@SuppressWarnings("serial")
public class DashboardMenu extends CustomComponent {
	private static final long serialVersionUID = 1L;

	public static final String ID = "dashboard-menu";
    public static final String RATING_BADGE_ID = "dashboard-menu-reports-badge";
    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private Label notificationsBadge;
    private Label reportsBadge;
    private MenuItem settingsItem;
    
    private final UserService userService;
    
	public DashboardMenu(final UserService userService) {
		this.userService = userService;
		
		setPrimaryStyleName("valo-menu"); 
		setId(ID); 
		setSizeUndefined(); 
		
		// There's only one DashboardMenu per UI so this doesn't need to be 
		// unregistered from the UI-scoped DashboardEventBus. 
		DashboardEventBus.register(this); 
		setCompositionRoot(buildContent()); 
	} 
	
	private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label(FontAwesome.ROCKET.getHtml(), ContentMode.HTML);
        Label name = new Label("&nbsp;&nbsp;Three <strong>Sixty</strong>", ContentMode.HTML);
        HorizontalLayout logoWrapper = new HorizontalLayout(logo, name);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        final User user = getCurrentUser();
        if (user.hasPicture()) {
        	settingsItem = settings.addItem("", new ByteArrayStreamResource(user.getPictureContent(), user.getPictureName()), null);
        } else {
        	settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
        }
        updateUserName(null);
        settingsItem.addItem("Edit Profile", new NavigateToProfileCommand());
        settingsItem.addItem("Preferences", new NavigateToProfileCommand());
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", new SignoutCommand());
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

        for (final DashboardViewType view : DashboardViewType.values()) {
            
        	if (!view.isAccessibleBy(getCurrentUser())) {
        		continue;
        	}
        	
        	Component menuItemComponent = new ValoMenuItemButton(view);

            
//            if (view == DashboardViewType.REPORTS) {
//                // Add drop target to reports button
//                DragAndDropWrapper reports = new DragAndDropWrapper(
//                        menuItemComponent);
//                reports.setSizeUndefined();
//                reports.setDragStartMode(DragStartMode.NONE);
//                reports.setDropHandler(new DropHandler() {
//
//                    @Override
//                    public void drop(final DragAndDropEvent event) {
//                        UI.getCurrent()
//                                .getNavigator()
//                                .navigateTo(
//                                        DashboardViewType.REPORTS.getViewName());
//                        Table table = (Table) event.getTransferable()
//                                .getSourceComponent();
//                        DashboardEventBus.post(new TransactionReportEvent(
//                                (Collection<Transaction>) table.getValue()));
//                    }
//
//                    @Override
//                    public AcceptCriterion getAcceptCriterion() {
//                        return AcceptItem.ALL;
//                    }
//
//                });
//                menuItemComponent = reports;
//            }

            if (view == DashboardViewType.DASHBOARD) {
                notificationsBadge = new Label();
                notificationsBadge.setId(NOTIFICATIONS_BADGE_ID);
                menuItemComponent = buildBadgeWrapper(menuItemComponent,
                        notificationsBadge);
            }
            //if (view == DashboardViewType.RATING) {
            //    reportsBadge = new Label();
            //    reportsBadge.setId(RATING_BADGE_ID);
            //    menuItemComponent = buildBadgeWrapper(menuItemComponent,
            //            reportsBadge);
            //}
            
            menuItemsLayout.addComponent(menuItemComponent);
        }
        return menuItemsLayout;

    }

    private Component buildBadgeWrapper(final Component menuItemButton,
            final Component badgeLabel) {
        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
        badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
        badgeLabel.setWidthUndefined();
        badgeLabel.setVisible(false);
        dashboardWrapper.addComponent(badgeLabel);
        return dashboardWrapper;
    }

    @Override
    public void attach() {
        super.attach();
        updateNotificationsCount(null);
    }

    @Subscribe
    public void postViewChange(final PostViewChangeEvent event) {
        // After a successful view change the menu can be hidden in mobile view.
        getCompositionRoot().removeStyleName(STYLE_VISIBLE);
    }

    @Subscribe
    public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
    	
        int unreadNotificationsCount = this.userService.getUnreadNotificationsCount(getCurrentUser());
        notificationsBadge.setValue(String.valueOf(unreadNotificationsCount));
        notificationsBadge.setVisible(unreadNotificationsCount > 0);
    }

    @Subscribe
    public void updateReportsCount(final ReportsCountUpdatedEvent event) {
        reportsBadge.setValue(String.valueOf(event.getCount()));
        reportsBadge.setVisible(event.getCount() > 0);
    }

    @Subscribe
    public void updateUserName(final ProfileUpdatedEvent event) {
        User user = getCurrentUser();
        
        String profile;
        if ((user.getFirstName() == null || user.getLastName() == null) && user.isAdmin()) {
        	profile = "Administrator";
        } else {
        	profile = user.getFirstName() + " " + user.getLastName();
        }
        settingsItem.setText(profile);
    }

    public final class ValoMenuItemButton extends Button {
		private static final long serialVersionUID = 1L;

		private static final String STYLE_SELECTED = "selected";

        private final DashboardViewType view;

        public ValoMenuItemButton(final DashboardViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
            setCaption(StringUtils.capitalize(StringUtils.replace(view.getViewName(), "-", " ")));
            DashboardEventBus.register(this);
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().getNavigator()
                            .navigateTo(view.getViewName());
                }
            });

        }
        
        @Subscribe
        public void postViewChange(final PostViewChangeEvent event) {
            removeStyleName(STYLE_SELECTED);
            if (event.getView() == view) {
                addStyleName(STYLE_SELECTED);
            }
        }
    }

    public class NavigateToProfileCommand implements Command {
		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().getNavigator().navigateTo(UserEditView.VIEW(getCurrentUser().getId()));
		}
    }
    
    public class SignoutCommand implements Command {
		@Override
		public void menuSelected(MenuItem selectedItem) {
			DashboardEventBus.post(new UserLogoutEvent());
		}
    }
    
    public User getCurrentUser() {
    	return (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
    }
}
