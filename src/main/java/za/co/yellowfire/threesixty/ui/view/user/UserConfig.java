package za.co.yellowfire.threesixty.ui.view.user;

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
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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

        return id -> Optional.ofNullable(userService.findUser((String) id));
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

        TableDefinition<User> tableDefinition = new TableDefinition<>(UserEditView.VIEW_NAME);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.ID).forProperty(User.FIELD_ID).identity().enableTextSearch();
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.FIRST_NAME).forProperty(User.FIELD_FIRST_NAME).enableTextSearch();
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.LAST_NAME).forProperty(User.FIELD_LAST_NAME).enableTextSearch();
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.ROLE).forProperty(User.FIELD_ROLE);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.WEBSITE).forProperty(User.FIELD_WEBSITE);
        tableDefinition.column(Boolean.class).withHeading(I8n.User.Columns.ACTIVE).forProperty(User.FIELD_ACTIVE);
        return tableDefinition;
    }

    @Bean
    @PrototypeScope
    List<UserImportModel> userImportData() {

        return new ArrayList<>();
    }

    @Bean
    TableDefinition<UserImportModel> userImportTableDefinition() {

        TableDefinition<UserImportModel> tableDefinition = new TableDefinition<>(UserEditView.VIEW_NAME);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.ID).forProperty(User.FIELD_ID).identity().enableTextSearch();
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.FIRST_NAME).forProperty(User.FIELD_FIRST_NAME).enableTextSearch();
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.LAST_NAME).forProperty(User.FIELD_LAST_NAME).enableTextSearch();
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.ROLE).forProperty(User.FIELD_ROLE);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.WEBSITE).forProperty(User.FIELD_WEBSITE);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.PHONE).forProperty(User.FIELD_PHONE);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.EMAIL).forProperty(User.FIELD_EMAIL);
        tableDefinition.column(String.class).withHeading(I8n.User.Columns.IMPORT_STATUS).forProperty(UserImportModel.FIELD_IMPORT_STATUS);
        return tableDefinition;
    }
}
