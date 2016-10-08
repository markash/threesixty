package za.co.yellowfire.threesixty.ui.view;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.DashboardEvent.CloseOpenWindowsEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;
import za.co.yellowfire.threesixty.ui.DashboardViewType;
import za.co.yellowfire.threesixty.ui.component.chart.TestChart;
import za.co.yellowfire.threesixty.ui.component.field.MCard;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationsButton;
import za.co.yellowfire.threesixty.ui.view.user.UserSearchView;
import za.co.yellowfire.threesixty.ui.view.user.notification.UserNotificationSearchView;

@SuppressWarnings("serial")
@SpringView(name = DashboardView.VIEW_NAME)
public final class DashboardView extends Panel implements View /*, DashboardEditListener*/ {
	private static final long serialVersionUID = 1L;
	
	public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";
    public static final String VIEW_NAME = "dashboard";
    
    private Label titleLabel;
    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;
    
    
    @Autowired
    public DashboardView(final UserService userService) {
        this.notificationsButton = NotificationsButton.BELL("dashboard-notifications", userService, this::onViewNotifications);
        
    	addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());

        //root.addComponent(buildSparklines());

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

//    private Component buildSparklines() {
//        CssLayout sparks = new CssLayout();
//        sparks.addStyleName("sparks");
//        sparks.setWidth("100%");
//        Responsive.makeResponsive(sparks);
//
//        SparklineChart s = new SparklineChart("Traffic", "K", "",
//                DummyDataGenerator.chartColors[0], 22, 20, 80);
//        sparks.addComponent(s);
//
//        s = new SparklineChart("Revenue / Day", "M", "$",
//                DummyDataGenerator.chartColors[2], 8, 89, 150);
//        sparks.addComponent(s);
//
//        s = new SparklineChart("Checkout Time", "s", "",
//                DummyDataGenerator.chartColors[3], 10, 30, 120);
//        sparks.addComponent(s);
//
//        s = new SparklineChart("Theater Fill Rate", "%", "",
//                DummyDataGenerator.chartColors[5], 50, 34, 100);
//        sparks.addComponent(s);
//
//        return sparks;
//    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label("Dashboard");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        HorizontalLayout tools = new HorizontalLayout(notificationsButton, buildEditButton());
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Component buildEditButton() {
        Button result = new Button();
        result.setId(EDIT_ID);
        result.setIcon(FontAwesome.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("Edit Dashboard");
//        result.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(final ClickEvent event) {
//                getUI().addWindow(
//                        new DashboardEdit(DashboardView.this, titleLabel
//                                .getValue()));
//            }
//        });
        return result;
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(buildUserCard());
        dashboardPanels.addComponent(buildPeriodsCard());
        dashboardPanels.addComponent(buildAssessmentsCard());
        dashboardPanels.addComponent(buildPerformanceAreasCard());

        //dashboardPanels.addComponent(buildTopGrossingMovies());
        //dashboardPanels.addComponent(buildNotes());
        //dashboardPanels.addComponent(buildChart());
        //dashboardPanels.addComponent(buildTop10TitlesByRevenue());
        //dashboardPanels.addComponent(buildPopularMovies());

        return dashboardPanels;
    }

    public Component buildUserCard() {
    	MCard card = new MCard("Users", FontAwesome.USERS, "The number of active users registered within the system.", "354", UserSearchView.VIEW_NAME);
    	return card;
    }
    
    public Component buildPeriodsCard() {
    	DashboardViewType type = DashboardViewType.PERIOD_SEARCH;
    	MCard card = new MCard("Periods", type.getIcon(), "The number of assessment period.", "1", type.getViewName());
    	return card;
    }
    
    public Component buildAssessmentsCard() {
    	DashboardViewType type = DashboardViewType.ASSESSMENT_SEARCH;
    	MCard card = new MCard("Assessments", type.getIcon(), "The number of assessment.", "12", type.getViewName());
    	return card;
    }
    
    public Component buildPerformanceAreasCard() {
    	DashboardViewType type = DashboardViewType.PERFORMANCE_AREA_SEARCH;
    	MCard card = new MCard("KPAs", type.getIcon(), "The number of key performance areas tracked by the application.", "34543", type.getViewName());
    	return card;
    }
    
//    private Component buildTopGrossingMovies() {
//        TopGrossingMoviesChart topGrossingMoviesChart = new TopGrossingMoviesChart();
//        topGrossingMoviesChart.setSizeFull();
//        return createContentWrapper(topGrossingMoviesChart);
//    }
//
//    private Component buildNotes() {
//        TextArea notes = new TextArea("Notes");
//        notes.setValue("Remember to:\n· Zoom in and out in the Sales view\n· Filter the transactions and drag a set of them to the Reports tab\n· Create a new report\n· Change the schedule of the movie theater");
//        notes.setSizeFull();
//        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
//        Component panel = createContentWrapper(notes);
//        panel.addStyleName("notes");
//        return panel;
//    }
//    
//    private Component buildChart() {
//    	
//		String jsonData = 
//				"var options = {\n" +
//                        "\ttitle: {\n" +
//                        "\t\ttext: 'test diagram'\n" +
//                        "\t},\n" +
//                        "\tseries: [\n" +
//                        "\t\t{\n" +
//                        "\t\t\tname: 's1',\n" +
//                        "\t\t\tdata: [1, 3, 2]\n" +
//                        "\t\t},\n" +
//                        "\t\t{\n" +
//                        "\t\t\tname: 's2',\n" +
//                        "\t\t\tdata: [2, 1, 3]\n" +
//                        "\t\t}\n" +
//                        "\t]\n" +
//                        "};";
//
//    	TestChart highchartsPie = new TestChart();
//    	//highchartsPie.setWidth("400px");
//    	//highchartsPie.setHeight("300px");
//    	
//    	return highchartsPie;
//    }
//
//    private Component buildTop10TitlesByRevenue() {
//        Component contentWrapper = createContentWrapper(new TopTenMoviesTable());
//        contentWrapper.addStyleName("top10-revenue");
//        return contentWrapper;
//    }
//
//    private Component buildPopularMovies() {
//        return createContentWrapper(new TopSixTheatersChart());
//    }
//
    private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {

            @Override
            public void menuSelected(final MenuItem selectedItem) {
                if (!slot.getStyleName().contains("max")) {
                    selectedItem.setIcon(FontAwesome.COMPRESS);
                    toggleMaximized(slot, true);
                } else {
                    slot.removeStyleName("max");
                    selectedItem.setIcon(FontAwesome.EXPAND);
                    toggleMaximized(slot, false);
                }
            }
        });
        max.setStyleName("icon-only");
        MenuItem root = tools.addItem("", FontAwesome.COG, null);
        root.addItem("Configure", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });
        root.addSeparator();
        root.addItem("Close", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }

    
    public void onViewNotifications(final ClickEvent event) {
    	UI.getCurrent().getNavigator().navigateTo(UserNotificationSearchView.VIEW_NAME);
    }
    
    @Override
    public void enter(final ViewChangeEvent event) {
        //notificationsButton.updateNotificationsCount(null);
    }

//    @Override
//    public void dashboardNameEdited(final String name) {
//        titleLabel.setValue(name);
//    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Iterator<Component> it = root.iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        dashboardPanels.setVisible(true);

        for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
            Component c = it.next();
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }
//
//    public final class NotificationsButton extends Button {
//        private static final String STYLE_UNREAD = "unread";
//        public static final String ID = "dashboard-notifications";
//
//        public NotificationsButton() {
//            setIcon(FontAwesome.BELL);
//            setId(ID);
//            addStyleName("notifications");
//            addStyleName(ValoTheme.BUTTON_ICON_ONLY);
//            DashboardEventBus.register(this);
//        }
//
//        @Subscribe
//        public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
//            setUnreadCount(userService.getUnreadNotificationsCount(getCurrentUser()));
//        }
//
//        public void setUnreadCount(final int count) {
//            setCaption(String.valueOf(count));
//
//            String description = "Notifications";
//            if (count > 0) {
//                addStyleName(STYLE_UNREAD);
//                description += " (" + count + " unread)";
//            } else {
//                removeStyleName(STYLE_UNREAD);
//            }
//            setDescription(description);
//        }
//    }
    
    protected User getCurrentUser() {
    	return ((MainUI) UI.getCurrent()).getCurrentUser();
    }
}

