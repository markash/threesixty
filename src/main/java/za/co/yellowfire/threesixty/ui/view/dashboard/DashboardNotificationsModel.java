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

class DashboardNotificationsModel extends NotificationsModel {
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
        return new NotificationModel(mapCategoryToIcon(n.getCategory()), "Notification", n.getContent(), n.isRead());
    }

    private FontIcon mapCategoryToIcon(final NotificationCategory category) {
        switch (category) {
            case System: return VaadinIcons.SERVER;
            case Rating: return VaadinIcons.QUESTION_CIRCLE;
            case Kudos: return VaadinIcons.DIAMOND;
            case Alarm: return VaadinIcons.CLOCK;
            case Error: return VaadinIcons.AMBULANCE;
            case Message: return VaadinIcons.ENVELOPE_O;
            case Service: return VaadinIcons.SERVER;
            default: return VaadinIcons.BOOKMARK_O;
        }
    }
}