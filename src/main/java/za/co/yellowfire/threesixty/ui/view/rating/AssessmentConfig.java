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
import org.vaadin.spring.events.EventBus;
import za.co.yellowfire.threesixty.domain.rating.*;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.view.period.PeriodModel;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static com.github.markash.ui.view.ValueBuilder.property;

@Configuration
@SuppressWarnings("unused")
public class AssessmentConfig {

    @Bean @PrototypeScope
    AssessmentEntityEditForm assessmentEntityEditForm(
            final ListDataProvider<PeriodModel> activePeriodListDataProvider,
            final ListDataProvider<User> activeUserListDataProvider,
            final AssessmentService assessmentService,
            final CurrentUserProvider<User> currentUserProvider,
            final EventBus.SessionEventBus eventBus) {
        return new AssessmentEntityEditForm(activePeriodListDataProvider, activeUserListDataProvider, assessmentService, currentUserProvider);
    }

    @Bean
    EntitySupplier<Assessment, Serializable> assessmentSupplier(final AssessmentService assessmentService) {
        return id -> Optional.ofNullable(assessmentService.findById((String) id));
    }

    @Bean
    BlankSupplier<Assessment> blankAssessmentSupplier() {
        return Assessment::new;
    }

    @Bean
    EntityPersistFunction<Assessment> assessmentPersistFunction(final AssessmentService assessmentService) {
        return assessment -> {
            try {
                return assessmentService.save(assessment);
            } catch (Exception e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return assessment;
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<Assessment> assessmentListDataProvider(final AssessmentService assessmentService) {
        List<Assessment> list = assessmentService.getAssessmentRepository().findAll();
        return new ListDataProvider<>(list);
    }

    @Bean
    TableDefinition<Assessment> assessmentTableDefinition(
            final PeriodRepository periodRepository) {

        TableDefinition<Assessment> tableDefinition =
                TableDefinition.forEntity(Assessment.class, AssessmentEditView.VIEW_NAME);

        tableDefinition
                .column(true)
                .withHeading(I8n.Assessment.Columns.ID)
                .withValue(property(String.class, Assessment.FIELD_ID))
        ;
        tableDefinition
                .column()
                .withHeading(I8n.Assessment.Columns.EMPLOYEE)
                .enableTextSearch()
                .withValue(property(User.class, Assessment.FIELD_EMPLOYEE))
                ;
        tableDefinition
                .column()
                .withHeading(I8n.Assessment.Columns.PERIOD)
                .enableTextSearch(periodRepository.findByActive(true))
                .withValue(property(Period.class, Assessment.FIELD_PERIOD))
                ;
        tableDefinition
                .column()
                .withHeading(I8n.Assessment.Columns.SCORE)
                .enableTextSearch()
                .withValue(property(Double.class, Assessment.FIELD_SCORE))
                ;
        tableDefinition
                .column()
                .withHeading(I8n.Assessment.Columns.STATUS)
                .enableTextSearch(AssessmentStatus.list())
                .withValue(property(AssessmentStatus.class, Assessment.FIELD_STATUS))
                ;
        return tableDefinition;
    }
}
