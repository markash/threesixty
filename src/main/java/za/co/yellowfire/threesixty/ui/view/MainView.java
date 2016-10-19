package za.co.yellowfire.threesixty.ui.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.DashboardMenu;
import za.co.yellowfire.threesixty.ui.DashboardNavigator;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SpringView(name = MainView.VIEW_NAME, ui = MainUI.class)
public class MainView extends HorizontalLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "main";
	
	@Autowired
    public MainView(final UserService userService) {
		
        setSizeFull();
        addStyleName("mainview");

        addComponent(new DashboardMenu(userService));

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        /* Create the navigator for the view content and navigate to the dashboard */
        DashboardNavigator navigator = new DashboardNavigator(content, MainUI.getViewProvider(), MainUI.getTracker());
        navigator.navigateTo("!#dashboard");
    }
    
    @Override
    public void enter(ViewChangeEvent event) { }
}