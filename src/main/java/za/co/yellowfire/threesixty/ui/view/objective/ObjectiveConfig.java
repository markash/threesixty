package za.co.yellowfire.threesixty.ui.view.objective;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.DateField;
import io.threesixty.ui.component.BlankSupplier;
import io.threesixty.ui.component.EntityPersistFunction;
import io.threesixty.ui.component.EntitySupplier;
import io.threesixty.ui.component.notification.NotificationBuilder;
import io.threesixty.ui.view.TableDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import za.co.yellowfire.threesixty.domain.PersistenceException;
import za.co.yellowfire.threesixty.domain.rating.Outcome;
import za.co.yellowfire.threesixty.domain.rating.OutcomeService;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.Optional;

@Configuration
@SuppressWarnings("unused")
public class ObjectiveConfig {

    @Bean @PrototypeScope
    ObjectiveEntityEditForm objectiveEntityEditForm(final EventBus.SessionEventBus eventBus) {
        return new ObjectiveEntityEditForm(eventBus);
    }

    @Bean
    EntitySupplier<Outcome, Serializable> objectiveSupplier(final OutcomeService outcomeService) {
        return id -> Optional.ofNullable(outcomeService.findById((String) id));
    }

    @Bean
    BlankSupplier<Outcome> blankObjectiveSupplier() {
        return Outcome::new;
    }

    @Bean
    EntityPersistFunction<Outcome> objectivePersistFunction(final OutcomeService outcomeService) {
        return new EntityPersistFunction<Outcome>() {
            @Override
            public Outcome apply(final Outcome objective) {
                try {
                    return outcomeService.save(objective);
                } catch (PersistenceException e) {
                    NotificationBuilder.showNotification("Persist", e.getMessage());
                }
                return objective;
            }
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<Outcome> objectiveListDataProvider(final OutcomeService outcomeService) {
        return new ListDataProvider<>(outcomeService.getRepository().findAll());
    }

    @Bean
    TableDefinition<Outcome> objectiveTableDefinition() {

        TableDefinition<Outcome> tableDefinition = new TableDefinition<>(ObjectiveEditView.VIEW_NAME);
        tableDefinition.column(DateField.class).withHeading(I8n.Objective.Columns.ID).forProperty(Outcome.FIELD_ID).identity();
        tableDefinition.column(Boolean.class).withHeading(I8n.Objective.Columns.ACTIVE).forProperty(Outcome.FIELD_ACTIVE);
        return tableDefinition;
    }
}
