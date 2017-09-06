package za.co.yellowfire.threesixty.ui.view.security;

import io.threesixty.ui.component.notification.NotificationBuilder;
import io.threesixty.ui.event.UserPasswordChangeEvent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.form.AbstractForm;
import za.co.yellowfire.threesixty.Response;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;

public class ChangePasswordHandler implements AbstractForm.SavedHandler<ChangePasswordModel> {

    private final UserService userService;
    private final EventBus.SessionEventBus eventBus;

    public ChangePasswordHandler(
            final UserService userService,
            final EventBus.SessionEventBus eventBus) {
        this.userService = userService;
        this.eventBus = eventBus;
    }

    @Override
    public void onSave(ChangePasswordModel model) {

        User user = userService.getCurrentUser();
        Response<User> response = userService.changePassword(user.getId(), model.getOldPassword(), model.getNewPassword());
        switch (response.getResult()) {
            case OK:
                this.eventBus.publish(this, UserPasswordChangeEvent.build("", user));
                break;
            case UNAUTHORIZED:
                NotificationBuilder.showNotification(
                        "Unable to authenticate",
                        "The user name and password provided is incorrect.",
                        20000);
                break;
        }
    }
}
