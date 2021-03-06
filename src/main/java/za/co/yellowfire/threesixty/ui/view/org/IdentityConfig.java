package za.co.yellowfire.threesixty.ui.view.org;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.view.TableDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.organization.Identity;
import za.co.yellowfire.threesixty.domain.organization.IdentityRepository;
import za.co.yellowfire.threesixty.domain.organization.IdentityService;
import za.co.yellowfire.threesixty.domain.rating.Discipline;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.Optional;

import static com.github.markash.ui.view.ValueBuilder.bool;
import static com.github.markash.ui.view.ValueBuilder.string;

@Configuration
@SuppressWarnings("unused")
public class IdentityConfig {

//    @Bean @PrototypeScope
//    DisciplineEntityEditForm disciplineEntityEditForm(
//            final ListDataProvider<Discipline> activePeriodListDataProvider,
//            final ListDataProvider<User> activeUserListDataProvider,
//            final DisciplineRepository disciplineRepository,
//            final CurrentUserProvider<User> currentUserProvider) {
//
//        return new DisciplineEntityEditForm();
//    }

    @Bean
    EntitySupplier<Identity, Serializable> identitySupplier(
            final IdentityRepository repository) {

        return id -> Optional.ofNullable(repository.findOne((String) id));
    }

    @Bean
    BlankSupplier<Identity> blankIdentitySupplier() {
        return Identity::new;
    }

    @Bean
    EntityPersistFunction<Identity> identityEntityPersistFunction(
            final IdentityRepository repository) {

        return identity -> {
            try {
                return repository.save(identity);
            } catch (Throwable e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return identity;
        };
    }

    @Bean
    @PrototypeScope
    IdentityDataProvider identityListDataProvider(
            final IdentityService service) {

        return new IdentityDataProvider(service);
    }

    @Bean
    TableDefinition<Identity> identityTableDefinition() {

        TableDefinition<Identity> tableDefinition = new TableDefinition<>(Identity.class, IdentityView.VIEW_NAME);
        tableDefinition
                .column(true)
                .withHeading(I8n.Discipline.Columns.NAME)
                .withValue(string(Discipline.FIELD_ID))
                .withDisplay(string(Discipline.FIELD_NAME))
                .enableTextSearch();
        tableDefinition
                .column()
                .withHeading(I8n.Discipline.Columns.ACTIVE)
                .withValue(bool(Discipline.FIELD_ACTIVE));
        return tableDefinition;
    }
}
