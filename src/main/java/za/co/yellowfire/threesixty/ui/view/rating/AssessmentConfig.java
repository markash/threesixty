package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.data.provider.ListDataProvider;
import io.threesixty.ui.component.BlankSupplier;
import io.threesixty.ui.component.EntityPersistFunction;
import io.threesixty.ui.component.EntitySupplier;
import io.threesixty.ui.component.notification.NotificationBuilder;
import io.threesixty.ui.security.CurrentUserProvider;
import io.threesixty.ui.view.TableDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import za.co.yellowfire.threesixty.domain.PersistenceException;
import za.co.yellowfire.threesixty.domain.mail.SendGridMailingService;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.view.period.PeriodModel;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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
        return new AssessmentEntityEditForm(activePeriodListDataProvider, activeUserListDataProvider, assessmentService, currentUserProvider, eventBus);
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
        return new EntityPersistFunction<Assessment>() {
            @Override
            public Assessment apply(final Assessment assessment) {
                try {
                    return assessmentService.save(assessment);
                } catch (PersistenceException e) {
                    NotificationBuilder.showNotification("Persist", e.getMessage());
                }
                return assessment;
            }
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<Assessment> assessmentListDataProvider(final AssessmentService assessmentService) {
        List<Assessment> list = assessmentService.getAssessmentRepository().findAll();
        return new ListDataProvider<>(list);
    }

    @Bean
    TableDefinition<Assessment> assessmentTableDefinition() {

        TableDefinition<Assessment> tableDefinition = new TableDefinition<>(AssessmentEditView.VIEW_NAME);
        tableDefinition.column(String.class).withHeading(I8n.Assessment.Columns.ID).forProperty(Assessment.FIELD_ID).identity();
        tableDefinition.column(User.class).withHeading(I8n.Assessment.Columns.EMPLOYEE).forProperty(Assessment.FIELD_EMPLOYEE);
        tableDefinition.column(User.class).withHeading(I8n.Assessment.Columns.PERIOD).forProperty(Assessment.FIELD_PERIOD);
        tableDefinition.column(Double.class).withHeading(I8n.Assessment.Columns.SCORE).forProperty(Assessment.FIELD_SCORE);
        tableDefinition.column(Boolean.class).withHeading(I8n.Assessment.Columns.STATUS).forProperty(Assessment.FIELD_STATUS);
        return tableDefinition;
    }
}
