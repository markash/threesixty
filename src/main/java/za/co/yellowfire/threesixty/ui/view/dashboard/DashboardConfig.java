package za.co.yellowfire.threesixty.ui.view.dashboard;

import com.github.markash.ui.component.card.CounterStatisticModel;
import com.github.markash.ui.component.card.CounterStatisticsCard;
import com.github.markash.ui.component.card.StatisticShow;
import com.github.markash.ui.security.CurrentUserProvider;
import com.vaadin.icons.VaadinIcons;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.rating.AssessmentRepository;
import za.co.yellowfire.threesixty.domain.rating.DisciplineRepository;
import za.co.yellowfire.threesixty.domain.rating.PeriodRepository;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.view.period.PeriodSearchView;
import za.co.yellowfire.threesixty.ui.view.rating.AssessmentSearchView;
import za.co.yellowfire.threesixty.ui.view.discipline.DisciplineSearchView;
import za.co.yellowfire.threesixty.ui.view.user.UserSearchView;

import java.util.Arrays;
import java.util.Collections;

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
            final PeriodRepository periodRepository,
            final AssessmentRepository assessmentRepository,
            final DisciplineRepository disciplineRepository) {

        return currentUserProvider
                .get()
                .map(principal -> dashboardStatisticCards(principal, userService, periodRepository, assessmentRepository, disciplineRepository))
                .orElse(new CounterStatisticsCards());
    }

    private CounterStatisticsCards dashboardStatisticCards(
            final User principal,
            final UserService userService,
            final PeriodRepository periodRepository,
            final AssessmentRepository assessmentRepository,
            final DisciplineRepository disciplineRepository) {

        if (principal.isAdmin()) {
            return new CounterStatisticsCards(
                    Arrays.asList(
                            usersCounterStatistic(userService),
                            periodsCounterStatistic(periodRepository),
                            assessmentsCounterStatistic(assessmentRepository),
                            performanceAreasCounterStatistic(disciplineRepository)
                    )
            );
        } else {
            return new CounterStatisticsCards(
                    Collections.singletonList(
                            usersCounterStatistic(userService)
                    )
            );
        }
    }

    @Bean @PrototypeScope
    public CounterStatisticModel usersCounterModel(final UserService userService) {
        return new CounterStatisticModel(
                "Registered Users",
                userService.getUserRepository().countActive())
                .withShow(StatisticShow.Sum)
                .withIconHidden()
                .withShowOnlyStatistic(true);
    }

    @Bean @PrototypeScope
    public CounterStatisticsCard usersCounterStatistic(final UserService userService) {
        return new CounterStatisticsCard(
                VaadinIcons.USERS,
                usersCounterModel(userService),
                UserSearchView.VIEW_NAME);
    }

    @Bean @PrototypeScope
    public CounterStatisticModel periodsCounterModel(final PeriodRepository repository) {
        return new CounterStatisticModel(
                "Periods",
                repository.countActive())
                .withShow(StatisticShow.Sum)
                .withIconHidden()
                .withShowOnlyStatistic(true);
    }

    @Bean @PrototypeScope
    public CounterStatisticsCard periodsCounterStatistic(final PeriodRepository repository) {
        return new CounterStatisticsCard(
                PeriodSearchView.ICON,
                periodsCounterModel(repository),
                PeriodSearchView.VIEW_NAME);
    }

    @Bean @PrototypeScope
    public CounterStatisticModel assessmentsCounterModel(final AssessmentRepository repository) {
        return new CounterStatisticModel(
                "Assessments",
                repository.countActive())
                .withShow(StatisticShow.Sum)
                .withIconHidden()
                .withShowOnlyStatistic(true);
    }

    @Bean @PrototypeScope
    public CounterStatisticsCard assessmentsCounterStatistic(final AssessmentRepository repository) {
        return new CounterStatisticsCard(
                VaadinIcons.USERS,
                assessmentsCounterModel(repository),
                AssessmentSearchView.VIEW_NAME);
    }

    @Bean @PrototypeScope
    public CounterStatisticModel performanceAreasCounterModel(final DisciplineRepository repository) {
        return new CounterStatisticModel(
                "Performance Areas",
                repository.countActive())
                .withShow(StatisticShow.Sum)
                .withIconHidden()
                .withShowOnlyStatistic(true);
    }

    @Bean @PrototypeScope
    public CounterStatisticsCard performanceAreasCounterStatistic(
            final DisciplineRepository repository) {

        return new CounterStatisticsCard(
                VaadinIcons.CUBES,
                performanceAreasCounterModel(repository),
                DisciplineSearchView.VIEW_NAME);
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
