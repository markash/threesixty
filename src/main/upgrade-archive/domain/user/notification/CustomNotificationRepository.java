package za.co.yellowfire.threesixty.domain.user.notification;

import java.util.List;

import za.co.yellowfire.threesixty.domain.user.User;

public interface CustomNotificationRepository {
	List<NotificationSummary> getNotificationSummaries(final User user);
}
