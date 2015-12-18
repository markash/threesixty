package za.co.yellowfire.threesixty.domain.user.notification;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserNotificationRepository extends MongoRepository<UserNotification, String> {
	
	@Query("{user.id: {$eq: ?0}, read: {$eq: false}}")
	List<UserNotification> findUnread(final String userName);
	
	@Query(count = true, value = "{user.id: {$eq: ?0}, read: {$eq: false}}")
	int findUnreadCount(final String userName);
	
	@Query("{user.id: {$eq: ?0}, read: {$eq: false}}")
	List<UserNotification> findNotifications(final String userName, final int limit, final Sort sort);
}
