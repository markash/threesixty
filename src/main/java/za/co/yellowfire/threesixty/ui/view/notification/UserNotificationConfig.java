package za.co.yellowfire.threesixty.ui.view.notification;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.security.CurrentUserProvider;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.*;
import com.vaadin.shared.Registration;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotificationRepository;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.markash.ui.view.ValueBuilder.property;
import static com.github.markash.ui.view.ValueBuilder.string;

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

//    @Bean
//    @PrototypeScope
//    AbstractDataProvider<UserNotification, UserNotificationFilter> notificationListDataProvider(
//            final UserNotificationRepository repository,
//            final CurrentUserProvider<User> currentUserProvider) {
//
//        return new AbstractBackEndDataProvider<UserNotification, UserNotificationFilter>() {
//            @Override
//            protected Stream<UserNotification> fetchFromBackEnd(
//                    final Query<UserNotification, UserNotificationFilter> query) {
//
//                List<UserNotification> results;
//                if (query.getFilter().isPresent()) {
//                    results = repository.findNotifications(query.getFilter().get().getUserName());
//                } else {
//                    results = repository.findAll();
//                }
//                return results.stream();
//            }
//
//            @Override
//            protected int sizeInBackEnd(
//                    final Query<UserNotification, UserNotificationFilter> query) {
//
//                return (int) fetchFromBackEnd(query).count();
//            }
//        };
//    }

    @Bean
    TableDefinition<UserNotification> notificationTableDefinition() {


        Formatter<DateTime> formatter = new Formatter<DateTime>() {

            @Override
            public DateTime parse(String s, Locale locale) {
                return null;
            }

            @Override
            public String print(
                    DateTime dateTime, Locale locale) {

                return dateTime.toString("yyyy-MM-dd hh:mm:ss", locale);
            }
        };

        TableDefinition<UserNotification> tableDefinition =
                TableDefinition.forEntity(UserNotification.class, UserNotificationEditView.VIEW_NAME);

        tableDefinition
                .column(true)
                .withHeading(I8n.Notifications.Columns.TIME)
                .enableTextSearch()
                .withValue(
                        string(UserNotification.FIELD_ID)
                )
                .withDisplay(
                        property(DateTime.class, UserNotification.FIELD_TIME)
                                .withFormatter(formatter)
                );
        tableDefinition
                .column()
                .withHeading(I8n.Notifications.Columns.CATEGORY)
                .withValue(string(UserNotification.FIELD_CATEGORY))
                .enableTextSearch();
        tableDefinition
                .column()
                .withHeading(I8n.Notifications.Columns.ACTION)
                .withValue(string(UserNotification.FIELD_ACTION))
                .enableTextSearch();
        tableDefinition
                .column()
                .withHeading(I8n.Notifications.Columns.USER)
                .withValue(string(UserNotification.FIELD_USER))
                .enableTextSearch();
        tableDefinition
                .column()
                .withHeading(I8n.Notifications.Columns.CONTENT)
                .withValue(string(UserNotification.FIELD_CONTENT))
                .enableTextSearch();
        return tableDefinition;
    }
}
