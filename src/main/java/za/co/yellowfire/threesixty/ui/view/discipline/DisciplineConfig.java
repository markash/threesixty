package za.co.yellowfire.threesixty.ui.view.discipline;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.ListDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.rating.Discipline;
import za.co.yellowfire.threesixty.domain.rating.DisciplineRepository;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.Optional;

@Configuration
@SuppressWarnings("unused")
public class DisciplineConfig {

    @Bean @PrototypeScope
    DisciplineEntityEditForm disciplineEntityEditForm() {
        return new DisciplineEntityEditForm();
    }

    @Bean
    EntitySupplier<Discipline, Serializable> disciplineSupplier(
            final DisciplineRepository repository) {

        return id -> Optional.ofNullable(repository.findOne((String) id));
    }

    @Bean
    BlankSupplier<Discipline> blankDisciplineSupplier() {
        return Discipline::new;
    }

    @Bean
    EntityPersistFunction<Discipline> disciplinePersistFunction(
            final DisciplineRepository repository) {

        return discipline -> {
            try {
                return repository.save(discipline);
            } catch (Throwable e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return discipline;
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<Discipline> disciplineListDataProvider(
            final DisciplineRepository repository) {

        return new ListDataProvider<>(repository.findAll());
    }

    @Bean
    TableDefinition<Discipline> disciplineTableDefinition() {

        TableDefinition<Discipline> tableDefinition = new TableDefinition<>(DisciplineEditView.VIEW_NAME);
        tableDefinition
                .column(String.class)
                .withHeading(I8n.Discipline.Columns.NAME)
                .forProperty(Discipline.FIELD_ID)
                .display(Discipline.FIELD_NAME)
                .enableTextSearch()
                .identity();
        tableDefinition
                .column(Boolean.class)
                .withHeading(I8n.Discipline.Columns.ACTIVE)
                .forProperty(Discipline.FIELD_ACTIVE);
        return tableDefinition;
    }
}
