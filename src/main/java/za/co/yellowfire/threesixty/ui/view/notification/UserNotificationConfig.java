package za.co.yellowfire.threesixty.ui.view.notification;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.ListDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.user.UserRepository;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotificationRepository;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.Optional;

@Configuration
@SuppressWarnings("unused")
public class UserNotificationConfig {

    @Bean @PrototypeScope
    UserNotificationEntityEditForm notificationEntityEditForm(
            final UserRepository userRepository) {

        return new UserNotificationEntityEditForm(userRepository);
    }

    @Bean
    EntitySupplier<UserNotification, Serializable> notificationSupplier(
            final UserNotificationRepository repository) {

        return id -> Optional.ofNullable(repository.findOne((String) id));
    }

    @Bean
    BlankSupplier<UserNotification> blankNotificationSupplier() {
        return UserNotification::new;
    }

    @Bean
    EntityPersistFunction<UserNotification> notificationPersistFunction(
            final UserNotificationRepository repository) {

        return notification -> {
            try {
                return repository.save(notification);
            } catch (Throwable e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return notification;
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<UserNotification> notificationListDataProvider(
            final UserNotificationRepository repository) {

        return new ListDataProvider<>(repository.findAll());
    }

    @Bean
    TableDefinition<UserNotification> notificationTableDefinition() {

        TableDefinition<UserNotification> tableDefinition = new TableDefinition<>(UserNotificationEditView.VIEW_NAME);
        tableDefinition
                .column(String.class)
                .withHeading(I8n.Notifications.Columns.ID)
                .forProperty(UserNotification.FIELD_ID)
                .enableTextSearch()
                .identity();
        tableDefinition
                .column(String.class)
                .withHeading(I8n.Notifications.Columns.CATEGORY)
                .forProperty(UserNotification.FIELD_CATEGORY);
        tableDefinition
                .column(String.class)
                .withHeading(I8n.Notifications.Columns.ACTION)
                .forProperty(UserNotification.FIELD_ACTION);
        tableDefinition
                .column(String.class)
                .withHeading(I8n.Notifications.Columns.TIME)
                .forProperty(UserNotification.FIELD_TIME);
        tableDefinition
                .column(String.class)
                .withHeading(I8n.Notifications.Columns.USER)
                .forProperty(UserNotification.FIELD_USER);
        return tableDefinition;
    }
}
