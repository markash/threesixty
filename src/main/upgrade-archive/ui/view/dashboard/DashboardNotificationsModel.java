package za.co.yellowfire.threesixty.ui.view.dashboard;

import com.github.markash.ui.component.notification.NotificationModel;
import com.github.markash.ui.component.notification.NotificationsModel;
import com.github.markash.ui.security.CurrentUserProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontIcon;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.domain.user.notification.NotificationCategory;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;

import java.util.stream.Collectors;

public class DashboardNotificationsModel extends NotificationsModel {
    private final User currentUser;
    private final UserService userService;

    DashboardNotificationsModel(final CurrentUserProvider<User> currentUserProvider, final UserService userService) {
        if (currentUserProvider.get().isPresent()) {
            this.currentUser = currentUserProvider.get().get();
        } else {
            this.currentUser = null;
        }

        this.userService = userService;
        refresh();
    }

    private void refresh() {
        if (currentUser != null) {
            setNotifications(userService
                    .findNotifications(currentUser)
                    .stream()
                    .map(this::mapToNotificationModel)
                    .collect(Collectors.toList()));
        } else {
            clearNotifications();
        }
    }

    private NotificationModel mapToNotificationModel(UserNotification n) {
        return new NotificationModel()
                .withIcon(mapCategoryToIcon(n.getCategory()))
                .withTitle("Notification")
                .withMessage(n.getContent())
                .withRead(n.isRead());
    }

    private FontIcon mapCategoryToIcon(final NotificationCategory category) {
        switch (category) {
            case NotificationCategory.System: return VaadinIcons.SERVER;
            case NotificationCategory.Rating: return VaadinIcons.QUESTION_CIRCLE;
            case NotificationCategory.Kudos: return VaadinIcons.DIAMOND;
            case NotificationCategory.Alarm: return VaadinIcons.CLOCK;
            case NotificationCategory.Error: return VaadinIcons.AMBULANCE;
            case NotificationCategory.Message: return VaadinIcons.ENVELOPE_O;
            case NotificationCategory.Service: return VaadinIcons.SERVER;
            default: return VaadinIcons.BOOKMARK_O;
        }
    }
}