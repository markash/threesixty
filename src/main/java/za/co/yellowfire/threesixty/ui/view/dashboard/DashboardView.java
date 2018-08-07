package za.co.yellowfire.threesixty.ui.view.dashboard;

import com.github.markash.ui.component.field.Toolbar;
import com.github.markash.ui.component.notification.NotificationsButton;
import com.github.markash.ui.view.AbstractDashboardView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.ui.I8n;

@SideBarItem(sectionId = Sections.DASHBOARD, caption = DashboardView.TITLE)
@VaadinFontIcon(VaadinIcons.HOME)
@SpringView(name = DashboardView.VIEW_NAME)
public class DashboardView extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;

    static final String TITLE = I8n.Dashboard.SINGULAR;
    static final String VIEW_NAME = "";
    static final VaadinIcons ICON = VaadinIcons.HOME;

    private final CounterStatisticsCards dashboardStatisticCards;

    @Autowired
    public DashboardView(
            final CounterStatisticsCards dashboardStatisticCards,
            final DashboardNotificationsModel notificationsModel) {
        super(TITLE);

    	this.dashboardStatisticCards = dashboardStatisticCards;
        getToolbar().add(
                NotificationsButton.BELL("dashboard-notifications", notificationsModel, this::onViewNotifications),
                Toolbar.ToolbarSection.ACTION);
    }

    private Component buildEditButton() {
        Button result = new Button();
        //result.setId(EDIT_ID);
        result.setIcon(VaadinIcons.EDIT);
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

    protected Component buildContent() {
        CssLayout dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        this.dashboardStatisticCards
                .stream()
                .forEach(card -> {
                    card.setWidth(258, Unit.PIXELS);
                    dashboardPanels.addComponent(card);
                });

//        if (this.user.isAdmin()) {
//	        dashboardPanels.addComponent(usersCounterStatistic);
//	        dashboardPanels.addComponent(periodsCounterStatistic);
//	        dashboardPanels.addComponent(buildAssessmentsCard());
//	        dashboardPanels.addComponent(buildPerformanceAreasCard());
//	        //dashboardPanels.addComponent(buildPeriodProgressChart());
//        } else {
////        	dashboardPanels.addComponent(buildKudosCard());
////        	dashboardPanels.addComponent(buildAssessmentsDueCard());
//        }

        return dashboardPanels;
    }

    //    private Component buildPeriodProgressChart() {
//    	LineChartConfig lineConfig = new LineChartConfig();
//        lineConfig.data()
//            .labels("January", "February", "March", "April", "May", "June", "July")
//            .addDataset(new LineDataset().label("My First dataset").fill(false))
//            .addDataset(new LineDataset().label("My Second dataset").fill(false))
//            .addDataset(new LineDataset().label("Hidden dataset").hidden(true))
//            .and()
//        .options()
//            .responsive(true)
//            .title()
//            .display(true)
//            .text("Chart.js Line Chart")
//            .and()
//        .tooltips()
//            .mode(Tooltips.Mode.LABEL)
//            .and()
//        .hover()
//            .mode(Hover.Mode.DATASET)
//            .and()
//        .scales()
//        .add(Axis.X, new CategoryScale()
//                .display(true)
//                .scaleLabel()
//                    .display(true)
//                    .labelString("Month")
//                    .and()
//                .position(Position.TOP))
//        .add(Axis.Y, new LinearScale()
//                .display(true)
//                .scaleLabel()
//                    .display(true)
//                    .labelString("Value")
//                    .and()
//                .ticks()
//                    .suggestedMin(-10)
//                    .suggestedMax(250)
//                    .and()
//                .position(Position.RIGHT))
//        .and()
//        .done();
//
//        // add random data for demo
//        List<String> labels = lineConfig.data().getLabels();
//        for (Dataset<?, ?> ds : lineConfig.data().getDatasets()) {
//            LineDataset lds = (LineDataset) ds;
//            List<Double> data = new ArrayList<>();
//            for (int i = 0; i < labels.size(); i++) {
//                data.add((double) Math.round(Math.random() * 100));
//            }
//            lds.dataAsList(data);
//            lds.borderColor("rgb(4, 73, 112)");
//            lds.backgroundColor("rgb(4, 73, 112)");
//        }
//
//        ChartJs chart = new ChartJs(lineConfig);
//        //chart.addClickListener((a,b) -> {
//        //    LineDataset dataset = (LineDataset) lineConfig.data().getDatasets().get(a);
//        //    ChartUtils.notification(a, b, dataset);
//        //});
//        chart.setWidth(70, Unit.PERCENTAGE);
//        chart.setJsLoggingEnabled(true);
//        return chart;
//    }

    @SuppressWarnings("unused")
    private void onViewNotifications(final ClickEvent event) {
//    	UI.getCurrent().getNavigator().navigateTo(UserNotificationSearchView.VIEW_NAME);
    }
    
    @Override
    public void enter(final ViewChangeEvent event) {
        //notificationsButton.updateNotificationsCount(null);
    }

//    @Override
//    public void dashboardNameEdited(final String name) {
//        titleLabel.setValue(name);
//    }

//    private void toggleMaximized(final Component panel, final boolean maximized) {
//        for (Iterator<Component> it = root.iterator(); it.hasNext();) {
//            it.next().setVisible(!maximized);
//        }
//        dashboardPanels.setVisible(true);
//
//        for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
//            Component c = it.next();
//            c.setVisible(!maximized);
//        }
//
//        if (maximized) {
//            panel.setVisible(true);
//            panel.addStyleName("max");
//        } else {
//            panel.removeStyleName("max");
//        }
//    }
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
}

