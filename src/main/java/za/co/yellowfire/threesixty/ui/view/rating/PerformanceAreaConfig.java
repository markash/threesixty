package za.co.yellowfire.threesixty.ui.view.rating;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.security.CurrentUserProvider;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.ListDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.rating.*;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.view.period.PeriodModel;

import java.io.Serializable;
import java.util.Optional;

@Configuration
@SuppressWarnings("unused")
public class PerformanceAreaConfig {

    @Bean @PrototypeScope
    PerformanceAreaEntityEditForm performaceAreaEntityEditForm(
            final ListDataProvider<PeriodModel> activePeriodListDataProvider,
            final ListDataProvider<User> activeUserListDataProvider,
            final PerformanceAreaRepository performanceAreaRepository,
            final CurrentUserProvider<User> currentUserProvider) {

        return new PerformanceAreaEntityEditForm(
                performanceAreaRepository,
                currentUserProvider);
    }

    @Bean
    EntitySupplier<PerformanceArea, Serializable> performanceAreaSupplier(
            final PerformanceAreaRepository repository) {

        return id -> Optional.ofNullable(repository.findOne((String) id));
    }

    @Bean
    BlankSupplier<PerformanceArea> blankPerformanceSupplier() {
        return PerformanceArea::new;
    }

    @Bean
    EntityPersistFunction<PerformanceArea> performanceAreaPersistFunction(
            final PerformanceAreaRepository repository) {

        return performanceArea -> {
            try {
                return repository.save(performanceArea);
            } catch (Throwable e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return performanceArea;
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<PerformanceArea> performanceAreaListDataProvider(
            final PerformanceAreaRepository repository) {

        return new ListDataProvider<>(repository.findAll());
    }

    @Bean
    TableDefinition<PerformanceArea> performanceAreaTableDefinition(
            final PeriodRepository periodRepository) {

        TableDefinition<PerformanceArea> tableDefinition = new TableDefinition<>(PerformanceAreaEditView.VIEW_NAME);
        tableDefinition.column(String.class).withHeading(I8n.Assessment.Columns.ID).forProperty(Assessment.FIELD_ID).identity();
        tableDefinition
                .column(User.class)
                .withHeading(I8n.Assessment.Columns.EMPLOYEE)
                .forProperty(Assessment.FIELD_EMPLOYEE)
                .enableTextSearch();
        tableDefinition
                .column(User.class)
                .withHeading(I8n.Assessment.Columns.PERIOD)
                .forProperty(Assessment.FIELD_PERIOD)
                .enableTextSearch(periodRepository.findByActive(true));
        tableDefinition
                .column(Double.class)
                .withHeading(I8n.Assessment.Columns.SCORE)
                .forProperty(Assessment.FIELD_SCORE)
                .enableTextSearch();
        tableDefinition
                .column(AssessmentStatus.class)
                .withHeading(I8n.Assessment.Columns.STATUS)
                .forProperty(Assessment.FIELD_STATUS)
                .enableTextSearch(AssessmentStatus.list());
        return tableDefinition;
    }
}
