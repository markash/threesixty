package za.co.yellowfire.threesixty.ui.view.objective;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.rating.Objective;
import za.co.yellowfire.threesixty.domain.rating.ObjectiveService;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.Optional;

import static com.github.markash.ui.view.ValueBuilder.bool;
import static com.github.markash.ui.view.ValueBuilder.string;

@Configuration
@SuppressWarnings("unused")
public class ObjectiveConfig {

    @Autowired
    private ApplicationContext applicationContext;

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
        return objective -> {
            try {
                return objectiveService.save(objective);
            } catch (Exception e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return objective;
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<Objective> objectiveListDataProvider(final ObjectiveService objectiveService) {
        return new ListDataProvider<>(objectiveService.getRepository().findAll());
    }

    @Bean
    TableDefinition<Objective> objectiveTableDefinition() {

        TableDefinition<Objective> tableDefinition = new TableDefinition<>(Objective.class, ObjectiveEditView.VIEW_NAME);
        tableDefinition
                .column(true)
                .withHeading(I8n.Objective.Columns.NAME)
                .withValue(string(Objective.FIELD_ID))
                .withDisplay(string(Objective.FIELD_NAME));
        tableDefinition
                .column()
                .withHeading(I8n.Objective.Columns.TEXT)
                .withValue(string(Objective.FIELD_TEXT))
                .enableTextSearch();
        tableDefinition
                .column()
                .withHeading(I8n.Objective.Columns.ACTIVE)
                .withValue(bool(Objective.FIELD_ACTIVE));

        return tableDefinition;
    }

    private boolean isPushStateNavigationEnabled() {

        String[] beanNames = applicationContext.getBeanNamesForAnnotation(PushStateNavigation.class);
        for (String beanName : beanNames) {
            Class<?> beanType = applicationContext.getType(beanName);
            if (UI.class.isAssignableFrom(beanType)) {
                return true;
            }
        }
        return false;
    }
}
