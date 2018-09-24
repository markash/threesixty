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

import static com.github.markash.ui.view.ValueBuilder.bool;
import static com.github.markash.ui.view.ValueBuilder.string;

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

        return user -> {
            try {
                return userService.save(user);
            } catch (IOException e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return user;
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

        TableDefinition<User> tableDefinition = new TableDefinition<>(User.class, UserEditView.VIEW_NAME);
        tableDefinition
                .column(true)
                .withHeading(I8n.User.Columns.ID)
                .withValue(string(User.FIELD_ID))
                .enableTextSearch();
        tableDefinition
                .column()
                .withHeading(I8n.User.Columns.FIRST_NAME)
                .withValue(string(User.FIELD_FIRST_NAME))
                .enableTextSearch();
        tableDefinition
                .column()
                .withHeading(I8n.User.Columns.LAST_NAME)
                .withValue(string(User.FIELD_LAST_NAME))
                .enableTextSearch();
        tableDefinition
                .column()
                .withHeading(I8n.User.Columns.ROLE)
                .withValue(string(User.FIELD_ROLE));
        tableDefinition
                .column()
                .withHeading(I8n.User.Columns.WEBSITE)
                .withValue(string(User.FIELD_WEBSITE));
        tableDefinition
                .column()
                .withHeading(I8n.User.Columns.ACTIVE)
                .withValue(bool(User.FIELD_ACTIVE));
        return tableDefinition;
    }

    @Bean
    @PrototypeScope
    List<UserImportModel> userImportData() {

        return new ArrayList<>();
    }

    @Bean
    TableDefinition<UserImportModel> userImportTableDefinition() {

        TableDefinition<UserImportModel> tableDefinition = new TableDefinition<>(UserImportModel.class, UserEditView.VIEW_NAME);
        tableDefinition.column(true).withHeading(I8n.User.Columns.ID).withValue(string(User.FIELD_ID)).enableTextSearch();
        tableDefinition.column().withHeading(I8n.User.Columns.FIRST_NAME).withValue(string(User.FIELD_FIRST_NAME)).enableTextSearch();
        tableDefinition.column().withHeading(I8n.User.Columns.LAST_NAME).withValue(string(User.FIELD_LAST_NAME)).enableTextSearch();
        tableDefinition.column().withHeading(I8n.User.Columns.ROLE).withValue(string(User.FIELD_ROLE));
        tableDefinition.column().withHeading(I8n.User.Columns.WEBSITE).withValue(string(User.FIELD_WEBSITE));
        tableDefinition.column().withHeading(I8n.User.Columns.PHONE).withValue(string(User.FIELD_PHONE));
        tableDefinition.column().withHeading(I8n.User.Columns.EMAIL).withValue(string(User.FIELD_EMAIL));
        tableDefinition.column().withHeading(I8n.User.Columns.IMPORT_STATUS).withValue(string(UserImportModel.FIELD_IMPORT_STATUS));
        return tableDefinition;
    }
}
