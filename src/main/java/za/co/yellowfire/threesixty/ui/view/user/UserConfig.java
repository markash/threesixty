package za.co.yellowfire.threesixty.ui.view.user;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.renderers.DateRenderer;
import io.threesixty.ui.component.BlankSupplier;
import io.threesixty.ui.component.EntityPersistFunction;
import io.threesixty.ui.component.EntitySupplier;
import io.threesixty.ui.component.notification.NotificationBuilder;
import io.threesixty.ui.security.CurrentUserProvider;
import io.threesixty.ui.view.TableDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.annotation.PrototypeScope;
import za.co.yellowfire.threesixty.domain.PersistenceException;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Configuration
@SuppressWarnings("unused")
public class UserConfig {

    @Bean @PrototypeScope
    UserEntityEditForm userEntityEditForm(final UserService userService, final CurrentUserProvider<User> currentUserProvider) {
        return new UserEntityEditForm(userService, currentUserProvider);
    }

    @Bean
    EntitySupplier<User, Serializable> userSupplier(
            final UserService userService) {

        return id -> Optional.of(userService.findUser((String) id));
    }

    @Bean
    BlankSupplier<User> blankUserSupplier() {
        return User::new;
    }

    @Bean
    EntityPersistFunction<User> userPersistFunction(
            final UserService userService) {

        return new EntityPersistFunction<User>() {
            @Override
            public User apply(final User user) {
                try {
                    return userService.save(user);
                } catch (IOException e) {
                    NotificationBuilder.showNotification("Persist", e.getMessage());
                }
                return user;
            }
        };
    }

    @Bean
    @PrototypeScope
    ListDataProvider<User> userListDataProvider(
            final UserService userService) {

        List<User> list = userService.getUserRepository().findAll();
        return new ListDataProvider<>(list);
    }

    @Bean
    TableDefinition<User> userTableDefinition() {

        DateRenderer dateRenderer = new DateRenderer("yyyy-MM-dd", "");
        TableDefinition<User> tableDefinition = new TableDefinition<>(UserEditView.VIEW_NAME);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.ID).forProperty(User.FIELD_ID).identity();
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.FIRST_NAME).forProperty(User.FIELD_FIRST_NAME);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.LAST_NAME).forProperty(User.FIELD_LAST_NAME);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.ROLE).forProperty(User.FIELD_ROLE);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.WEBSITE).forProperty(User.FIELD_WEBSITE);
        tableDefinition.column(Boolean.class).withHeading(I8n.User.Columns.ACTIVE).forProperty(User.FIELD_ACTIVE);
        return tableDefinition;
    }
}
