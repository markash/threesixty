package za.co.yellowfire.threesixty.ui.view.security;

import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.event.UserPasswordChangeEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.form.AbstractForm;
import za.co.yellowfire.threesixty.Response;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;

public class ChangePasswordHandler implements AbstractForm.SavedHandler<ChangePasswordModel> {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    public ChangePasswordHandler(
            final UserService userService,
            final ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.publisher = publisher;
    }

    @Override
    public void onSave(ChangePasswordModel model) {

        User user = userService.getCurrentUser();
        Response<User> response = userService.changePassword(user.getId(), model.getOldPassword(), model.getNewPassword());
        switch (response.getResult()) {
            case OK:
                this.publisher.publishEvent(UserPasswordChangeEvent.build("", response.getValue()));
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
