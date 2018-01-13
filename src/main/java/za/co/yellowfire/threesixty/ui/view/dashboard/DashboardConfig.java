package za.co.yellowfire.threesixty.ui.view.dashboard;

import com.vaadin.icons.VaadinIcons;
import io.threesixty.ui.component.card.CounterStatisticsCard;
import io.threesixty.ui.security.CurrentUserProvider;
import io.threesixty.ui.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.PeriodService;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.view.objective.ObjectiveSearchView;
import za.co.yellowfire.threesixty.ui.view.period.PeriodSearchView;
import za.co.yellowfire.threesixty.ui.view.rating.AssessmentSearchView;
import za.co.yellowfire.threesixty.ui.view.user.UserSearchView;

import java.util.Arrays;

@Configuration
@SuppressWarnings("unused")
public class DashboardConfig {

    @Bean @PrototypeScope
    public DashboardNotificationsModel notificationsModel(final CurrentUserProvider<User> currentUserProvider, final UserService userService) {
        return new DashboardNotificationsModel(currentUserProvider, userService);
    }

    @Bean @PrototypeScope
    public CounterStatisticsCards dashboardStatisticCards(
            final CurrentUserProvider<User> currentUserProvider,
            final UserService userService,
            final PeriodService periodService,
            final AssessmentService assessmentService) {

        return currentUserProvider
                .get()
                .map(principal -> dashboardStatisticCards(principal, userService, periodService, assessmentService))
                .orElse(new CounterStatisticsCards());
    }

    private CounterStatisticsCards dashboardStatisticCards(
            final UserPrincipal<User> principal,
            final UserService userService,
            final PeriodService periodService,
            final AssessmentService assessmentService) {

        if (principal.getUser().isAdmin()) {
            return new CounterStatisticsCards(
                    Arrays.asList(
                            usersCounterStatistic(userService),
                            periodsCounterStatistic(periodService),
                            assessmentsCounterStatistic(assessmentService),
                            performanceAreasCounterStatistic(assessmentService)
                    )
            );
        } else {
            return new CounterStatisticsCards(
                    Arrays.asList(
                            usersCounterStatistic(userService)
                    )
            );
        }
    }

    @Bean @PrototypeScope
    public CounterStatisticsCard usersCounterStatistic(final UserService userService) {
        return new CounterStatisticsCard(
                VaadinIcons.USERS,
                userService.getUsersCounterStatistic(),
                UserSearchView.VIEW_NAME);
    }

    @Bean @PrototypeScope
    public CounterStatisticsCard periodsCounterStatistic(final PeriodService periodService) {
        return new CounterStatisticsCard(
                PeriodSearchView.ICON,
                periodService.getPeriodCounterStatistic(),
                PeriodSearchView.VIEW_NAME);
    }

    @Bean @PrototypeScope
    public CounterStatisticsCard assessmentsCounterStatistic(final AssessmentService assessmentService) {
        return new CounterStatisticsCard(
                VaadinIcons.USERS, //type.getIcon(),
                assessmentService.getAssessmentsCounterStatistic(),
                AssessmentSearchView.VIEW_NAME);
    }

    @Bean @PrototypeScope
    public CounterStatisticsCard performanceAreasCounterStatistic(final AssessmentService assessmentService) {
        return new CounterStatisticsCard(
                VaadinIcons.CUBES,
                assessmentService.getPerformanceAreasCounterStatistic(),
                ObjectiveSearchView.VIEW_NAME);
    }

    //    private Component buildKudosCard() {
//    	CounterStatisticModel walletBalanceStatistic =
//    			new CounterStatisticModel(
//    					"WalletBalanceStatistic",
//    					kudosRepository.getWallet(userService.getCurrentUser()).getBalance().getValue())
//    			.prefix(I8n.Kudos.Wallet.CURRENCY_SYMBOL);
//
//    	//DashboardViewType type = DashboardViewType.KUDOS;
//    	return new CounterStatisticsCard(
//    			"",
//    			VaadinIcons.BRIEFCASE, //type.getIcon(),
//    			"Kudos Wallet balance.",
//                () -> walletBalanceStatistic,
//    			"" //type.getViewName()
//        );
//    }

//    private Component buildAssessmentsDueCard() {
//
//    	//DashboardViewType type = DashboardViewType.ASSESSMENT_SEARCH;
//    	return new CounterStatisticsCard(
//    			"Assessments Due",
//                VaadinIcons.BRIEFCASE, // type.getIcon(),
//    			"The number of assessments that are due which includes self and subordinate assessments.",
//                () -> assessmentService.getAssessmentsDueCounterStatistic(userService.getCurrentUser().getId()),
//    			"" //type.getViewName()
//        );
//    }
}
