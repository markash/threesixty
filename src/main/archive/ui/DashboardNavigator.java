package za.co.yellowfire.threesixty.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
//import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

@SuppressWarnings("serial")
public class DashboardNavigator extends Navigator {
	private static final long serialVersionUID = 1L;
    private static final DashboardViewType ERROR_VIEW = DashboardViewType.DASHBOARD;
    //private ViewProvider errorViewProvider;

    //private GoogleAnalyticsTracker tracker;
    
    public DashboardNavigator(
    		final ComponentContainer container, 
    		final ViewProvider viewProvider/*,
    		final GoogleAnalyticsTracker tracker*/) {
        super(UI.getCurrent(), container);
        //this.tracker = tracker;
        
        initViewChangeListener();
        initViewProviders(viewProvider);
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                //DashboardViewType view = DashboardViewType.getByViewName(event
                //        .getViewName());
                // Appropriate events get fired after the view is changed.
                //DashboardEventBus.post(new PostViewChangeEvent(view));
                //DashboardEventBus.post(new BrowserResizeEvent());
                //DashboardEventBus.post(new CloseOpenWindowsEvent());

                //if (tracker != null) {
                //    tracker.trackPageview("/dashboard/" + event.getViewName());
                //}
            }
        });
    }

    private void initViewProviders(final ViewProvider viewProvider) {
        addProvider(new DashboardViewProvider(viewProvider));
        setErrorProvider(new ErrorViewProvider(viewProvider));
    }
    
    /**
     * The DashboardViewProvider uses the Spring View Provider but whenever the main view is requested
     * then the dashboard view is returned instead. This is to fix the problem when the broker refresh
     * button is pressed.
     */
    private static class DashboardViewProvider implements ViewProvider {

    	private ViewProvider viewProvider;
    	
		public DashboardViewProvider(ViewProvider viewProvider) {
			super();
			this.viewProvider = viewProvider;
		}

		@Override
		public String getViewName(String viewAndParameters) {
			return viewAndParameters.contains("main") ? ERROR_VIEW.getViewName() : viewProvider.getViewName(viewAndParameters);
		}

		@Override
		public View getView(String viewName) {
			return viewName.equals("main") ? viewProvider.getView(ERROR_VIEW.getViewName()) : viewProvider.getView(viewName);
		}
    }
    
    /**
     * A fallback view provider that always shows the DashBoard
     */
    private static class ErrorViewProvider implements ViewProvider {
    	
    	private ViewProvider viewProvider;
    	
		public ErrorViewProvider(ViewProvider viewProvider) {
			super();
			this.viewProvider = viewProvider;
		}
		
		@Override
		public String getViewName(String viewAndParameters) {
			return ERROR_VIEW.getViewName();
		}

		@Override
		public View getView(String viewName) {
			return viewProvider.getView(ERROR_VIEW.getViewName());
		}
    }
}

