package za.co.yellowfire.threesixty;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.converter.ConverterFactory;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.DashboardEvent.BrowserResizeEvent;
import za.co.yellowfire.threesixty.ui.DashboardEvent.CloseOpenWindowsEvent;
import za.co.yellowfire.threesixty.ui.DashboardEvent.ProfileUpdatedEvent;
import za.co.yellowfire.threesixty.ui.DashboardEvent.UserLoginEvent;
import za.co.yellowfire.threesixty.ui.DashboardEvent.UserLogoutEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;
import za.co.yellowfire.threesixty.ui.view.LoginView;
import za.co.yellowfire.threesixty.ui.view.MainView;

@SuppressWarnings("serial")
@Theme("dashboard")
@SpringUI
@PreserveOnRefresh 
public class MainUI extends UI {
	private static final long serialVersionUID = 1L;

	@Autowired
	private ConverterFactory converterFactory;
    @Autowired
    private SpringViewProvider viewProvider;   
    @Autowired
    private DashboardEventBus dashboardEventbus;
    
    @Override
    protected void init(VaadinRequest request) {
    	Navigator navigator = new Navigator(this, this);
    	navigator.addProvider(viewProvider);
    	setNavigator(navigator);
    	
    	getSession().setConverterFactory(converterFactory);
    	DashboardEventBus.register(this);
    	
    	Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);
        
        updateContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(
                new BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            final BrowserWindowResizeEvent event) {
                        DashboardEventBus.post(new BrowserResizeEvent());
                    }
                });
    }

    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    private void updateContent() {
        
        if (getCurrentUser() != null) {
            getNavigator().navigateTo(MainView.VIEW_NAME);
        } else {
        	getNavigator().navigateTo(LoginView.VIEW_NAME);
        }
    }
    
    @Subscribe
    public void userLogin(final UserLoginEvent event) {
        VaadinSession.getCurrent().setAttribute(User.class, event.getUser());
        updateContent();
    }

    @Subscribe
    public void userLogout(final UserLogoutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void userProfileUpdate(final ProfileUpdatedEvent event) {
    	VaadinSession.getCurrent().setAttribute(User.class, event.getUser());
    }
    
    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }
    
    public User getCurrentUser() {
    	return (User) VaadinSession.getCurrent().getAttribute(User.class);
    }
    
    public static DashboardEventBus getDashboardEventbus() { 
    	return ((MainUI) getCurrent()).dashboardEventbus; 
    } 
    
    public static ViewProvider getViewProvider() { 
    	return ((MainUI) getCurrent()).viewProvider; 
    }
}