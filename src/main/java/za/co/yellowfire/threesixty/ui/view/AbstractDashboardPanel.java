package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.ui.DashboardEvent.CloseOpenWindowsEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;
import za.co.yellowfire.threesixty.ui.Style;

@SuppressWarnings("serial")
public abstract class AbstractDashboardPanel extends Panel implements View {
    private final VerticalLayout root;

    protected static final String STYLE_DASHBOARD_VIEW = "dashboard-view";
    
	protected abstract String getTitle();
	protected abstract String getTitleId();
	protected abstract String getEditId();
	protected abstract Component buildContent();
	protected Component buildHeaderButtons() { return null; } 
	
	public AbstractDashboardPanel() {
    	this.root = new VerticalLayout();
    }
	
	protected boolean isBuilt() {
		return root.getStyleName().contains(STYLE_DASHBOARD_VIEW);
	}
	
	protected void build() {		
		if (!isBuilt()) {
			addStyleName(ValoTheme.PANEL_BORDERLESS);
	        setSizeFull();
	        DashboardEventBus.register(this);
	
	        root.setSizeFull();
	        root.setMargin(true);
	        root.addStyleName(STYLE_DASHBOARD_VIEW);
	        setContent(root);
	        Responsive.makeResponsive(root);
	
	        root.addComponent(buildHeader());
	
	        Component content = buildContent();
	        root.addComponent(content);
	        root.setExpandRatio(content, 1);
	
	        // All the open sub-windows should be closed whenever the root layout
	        // gets clicked.
	        root.addLayoutClickListener(new LayoutClickListener() {
	            @Override
	            public void layoutClick(final LayoutClickEvent event) {
	                DashboardEventBus.post(new CloseOpenWindowsEvent());
	            }
	        });
		}
	}
	
	protected Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        Label titleLabel = new Label(getTitle());
        titleLabel.setId(getTitleId());
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        Component headerButtons = buildHeaderButtons();
        if (headerButtons != null) {
        	header.addComponent(headerButtons);
        }

        return header;
    }
	
	protected Layout buildPanel(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName(Style.Rating.FIELDS);
		
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	protected Layout buildVerticalPanel(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName(Style.Rating.FIELDS);
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	protected Layout buildHorizontalPanel(final Component...components) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);
		
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
		
	@Override
    public void enter(final ViewChangeEvent event) {
    }
}
