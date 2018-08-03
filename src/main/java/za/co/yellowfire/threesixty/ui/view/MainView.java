package za.co.yellowfire.threesixty.ui.view;

import com.github.markash.ui.view.ErrorView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import za.co.yellowfire.threesixty.domain.user.UserService;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@UIScope
@SpringComponent
public class MainView extends HorizontalLayout implements View {
	private static final long serialVersionUID = 1L;
    public static final String VIEW_NAME = "main";
    static final String VIEW_CAPTION = "Home";
    static final int VIEW_ORDER = 1;

	@Autowired
    public MainView(
            final UserService userService,
            final VaadinSecurity vaadinSecurity,
            final SpringViewProvider springViewProvider,
            final ValoSideBar sideBar) {
		
        setSizeFull();
        //addStyleName("mainview");

        //addComponent(new DashboardMenu(userService));

        //ComponentContainer content = new CssLayout();
        //content.addStyleName("view-content");
        //content.setSizeFull();
        //addComponent(content);
        //setExpandRatio(content, 1.0f);

        /* Create the navigator for the view content and navigate to the dashboard */
        //DashboardNavigator navigator = new DashboardNavigator(content, viewProvider/*, MainUI.getTracker()*/);
        //navigator.navigateTo("!#dashboard");


        // By adding a security item filter, only views that are accessible to the user will show up in the side bar.
        //sideBar.setItemFilter(new VaadinSecurityItemFilter(vaadinSecurity));
        addComponent(sideBar);

        CssLayout viewContainer = new CssLayout();
        viewContainer.setSizeFull();
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1f);

        Navigator navigator = new Navigator(UI.getCurrent(), viewContainer);
        // Without an AccessDeniedView, the view provider would act like the restricted views did not exist at all.
        springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        navigator.addProvider(springViewProvider);
        navigator.setErrorView(ErrorView.class);
        navigator.navigateTo(navigator.getState());
    }
    
    @Override
    public void enter(ViewChangeEvent event) { }
}