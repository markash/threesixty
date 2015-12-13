package za.co.yellowfire.threesixty.ui;

//import com.vaadin.demo.dashboard.view.dashboard.DashboardView;
//import com.vaadin.demo.dashboard.view.reports.ReportsView;
//import com.vaadin.demo.dashboard.view.sales.SalesView;
//import com.vaadin.demo.dashboard.view.schedule.ScheduleView;
//import com.vaadin.demo.dashboard.view.transactions.TransactionsView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

import za.co.yellowfire.threesixty.domain.user.Role;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.view.DashboardView;
import za.co.yellowfire.threesixty.ui.view.QuestionaireSearchView;
import za.co.yellowfire.threesixty.ui.view.RatingView;
import za.co.yellowfire.threesixty.ui.view.UserSearchView;
import za.co.yellowfire.threesixty.ui.view.rating.AssessmentSearchView;
import za.co.yellowfire.threesixty.ui.view.rating.OutcomesSearchView;

public enum DashboardViewType {
    DASHBOARD("dashboard", DashboardView.class, FontAwesome.HOME, true, null), 
    USER_SEARCH(UserSearchView.VIEW_NAME, UserSearchView.class, FontAwesome.USERS, true, Role.ADMIN),
    RATING(RatingView.VIEW_NAME, RatingView.class, FontAwesome.QUESTION_CIRCLE, true, Role.ADMIN),
    QUESTIONAIRE_SEARCH(QuestionaireSearchView.VIEW_NAME, QuestionaireSearchView.class, FontAwesome.BRIEFCASE, true, Role.ADMIN),
    OUTCOME_SEARCH(OutcomesSearchView.VIEW_NAME, OutcomesSearchView.class, FontAwesome.BUILDING_O, true, Role.ADMIN),
    ASSESSMENT_SEARCH(AssessmentSearchView.VIEW_NAME, AssessmentSearchView.class, FontAwesome.BRIEFCASE, true, null),
    //SALES("sales", SalesView.class, FontAwesome.BAR_CHART_O, false), 
    //TRANSACTIONS("transactions", TransactionsView.class, FontAwesome.TABLE, false), 
    //REPORTS("reports", ReportsView.class, FontAwesome.FILE_TEXT_O, true), 
    //SCHEDULE("schedule", ScheduleView.class, FontAwesome.CALENDAR_O, false)
    ;

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;
    private final Role role;
    
    private DashboardViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful,
            final Role role) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
        this.role = role;
    }

    public boolean isStateful() { return stateful; }
    public String getViewName() { return viewName; }
    public Class<? extends View> getViewClass() { return viewClass; }
    public Resource getIcon() { return icon; }
    
    public boolean isAccessibleBy(final User user) {
    	if (this.role == null) { return true; }
    	if (user == null || user.getRole() == null) { return false; }
    	
    	return user.getRole().equals(this.role);
    }
    
    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }
}
