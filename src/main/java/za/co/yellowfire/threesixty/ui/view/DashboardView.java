package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import io.threesixty.ui.component.card.CounterStatisticModel;
import io.threesixty.ui.component.card.CounterStatisticsCard;
import io.threesixty.ui.view.AbstractDashboardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.kudos.KudosRepository;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.PeriodService;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;

@Secured("ROLE_ADMIN")
@SideBarItem(sectionId = Sections.DASHBOARD, caption = DashboardView.TITLE)
@FontAwesomeIcon(FontAwesome.HOME)
@SpringView(name = DashboardView.VIEW_NAME)
public class DashboardView extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;

    static final String TITLE = I8n.Dashboard.SINGULAR;
    static final String VIEW_NAME = "dashboard";

//    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private UserService userService;
    private AssessmentService assessmentService;
    private PeriodService periodService;
    private KudosRepository kudosRepository;
    
    @Autowired
    public DashboardView(
    		final UserService userService, 
    		final AssessmentService assessmentService, 
    		final PeriodService periodService,
    		final KudosRepository kudosRepository) {
        super(TITLE);

        this.userService = userService;
        this.assessmentService = assessmentService;
        this.periodService = periodService;
    	this.kudosRepository = kudosRepository;
//        this.notificationsButton = NotificationsButton.BELL("dashboard-notifications", userService, this::onViewNotifications);
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
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        
        Responsive.makeResponsive(dashboardPanels);

//        if (userService.getCurrentUser().isAdmin()) {
	        dashboardPanels.addComponent(buildUserCard());
	        dashboardPanels.addComponent(buildPeriodsCard());
	        dashboardPanels.addComponent(buildAssessmentsCard());
	        dashboardPanels.addComponent(buildPerformanceAreasCard());
	        //dashboardPanels.addComponent(buildPeriodProgressChart());
//        } else {
//        	dashboardPanels.addComponent(buildKudosCard());
//        	dashboardPanels.addComponent(buildAssessmentsDueCard());
//        }

        return dashboardPanels;
    }

    private Component buildUserCard() {
    	return new CounterStatisticsCard(
    			"Users",
    			VaadinIcons.USERS,
    			"The number of active users registered within the system.",
    			userService.getUsersCounterStatistic(),
    			"" //UserSearchView.VIEW_NAME
        );
    }
    
    private Component buildPeriodsCard() {
//    	DashboardViewType type = DashboardViewType.PERIOD_SEARCH;
    	return new CounterStatisticsCard(
    			"Periods",
                VaadinIcons.USERS, //type.getIcon(),
    			"The number of assessment period.",
    			periodService.getPeriodCounterStatistic(),
                "" //type.getViewName()
        );
    }

    private Component buildAssessmentsCard() {
//    	DashboardViewType type = DashboardViewType.ASSESSMENT_SEARCH;
    	return new CounterStatisticsCard(
    			"Assessments",
                VaadinIcons.USERS, //type.getIcon(),
    			"The number of assessment.",
    			assessmentService.getAssessmentsCounterStatistic(),
                "" //type.getViewName()
        );
    }

    private Component buildPerformanceAreasCard() {
//    	DashboardViewType type = DashboardViewType.PERFORMANCE_AREA_SEARCH;
    	return new CounterStatisticsCard(
    			"KPAs",
    			VaadinIcons.USERS, //type.getIcon(),
    			"The number of key performance areas tracked by the application.",
    			assessmentService.getPerformanceAreasCounterStatistic(),
                "" //type.getViewName()
        );
    }
    
    private Component buildKudosCard() {
    	CounterStatisticModel walletBalanceStatistic =
    			new CounterStatisticModel(
    					"WalletBalanceStatistic",
    					kudosRepository.getWallet(userService.getCurrentUser()).getBalance().getValue())
    			.prefix(I8n.Kudos.Wallet.CURRENCY_SYMBOL);

    	//DashboardViewType type = DashboardViewType.KUDOS;
    	return new CounterStatisticsCard(
    			"",
    			VaadinIcons.BRIEFCASE, //type.getIcon(),
    			"Kudos Wallet balance.",
    			walletBalanceStatistic,
    			"" //type.getViewName()
        );
    }

    private Component buildAssessmentsDueCard() {

    	//DashboardViewType type = DashboardViewType.ASSESSMENT_SEARCH;
    	return new CounterStatisticsCard(
    			"Assessments Due",
                VaadinIcons.BRIEFCASE, // type.getIcon(),
    			"The number of assessments that are due which includes self and subordinate assessments.",
    			assessmentService.getAssessmentsDueCounterStatistic(userService.getCurrentUser().getId()),
    			"" //type.getViewName()
        );
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

    
    public void onViewNotifications(final ClickEvent event) {
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
    
    protected User getCurrentUser() {
    	return ((MainUI) UI.getCurrent()).getCurrentUser();
    }
}

