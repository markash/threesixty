package za.co.yellowfire.threesixty.ui.view.objective;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import io.threesixty.ui.component.BlankSupplier;
import io.threesixty.ui.component.EntityPersistFunction;
import io.threesixty.ui.component.EntitySupplier;
import io.threesixty.ui.component.notification.NotificationBuilder;
import io.threesixty.ui.view.TableDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.PersistenceException;
import za.co.yellowfire.threesixty.domain.rating.Objective;
import za.co.yellowfire.threesixty.domain.rating.ObjectiveService;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.Optional;

@Configuration
@SuppressWarnings("unused")
public class ObjectiveConfig {

    @Bean @PrototypeScope
    ObjectiveEntityEditForm objectiveEntityEditForm() {
        return new ObjectiveEntityEditForm();
    }

    @Bean
    EntitySupplier<Objective, Serializable> objectiveSupplier(final ObjectiveService objectiveService) {
        return id -> Optional.ofNullable(objectiveService.findById((String) id));
    }

    @Bean
    BlankSupplier<Objective> blankObjectiveSupplier() {
        return Objective::new;
    }

    @Bean
    EntityPersistFunction<Objective> objectivePersistFunction(final ObjectiveService objectiveService) {
        return new EntityPersistFunction<Objective>() {
            @Override
            public Objective apply(final Objective objective) {
                try {
                    return objectiveService.save(objective);
                } catch (PersistenceException e) {
                    NotificationBuilder.showNotification("Persist", e.getMessage());
                }
                return objective;
            }
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<Objective> objectiveListDataProvider(final ObjectiveService objectiveService) {
        return new ListDataProvider<>(objectiveService.getRepository().findAll());
    }

    @Bean
    TableDefinition<Objective> objectiveTableDefinition() {

        TableDefinition<Objective> tableDefinition = new TableDefinition<>(ObjectiveEditView.VIEW_NAME);
        tableDefinition.column(DateField.class).withHeading(I8n.Objective.Columns.NAME).forProperty(Objective.FIELD_ID).identity().display(Objective.FIELD_NAME);
        tableDefinition.column(TextField.class).withHeading(I8n.Objective.Columns.TEXT).forProperty(Objective.FIELD_TEXT).enableTextSearch();
        tableDefinition.column(Boolean.class).withHeading(I8n.Objective.Columns.ACTIVE).forProperty(Objective.FIELD_ACTIVE);
        return tableDefinition;
    }
}
