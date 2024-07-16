package za.co.yellowfire.threesixty.domain.user.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@SuppressWarnings("unused")
public interface UserNotificationRepository extends JpaRepository<UserNotification, String>, JpaSpecificationExecutor<UserNotification> {
	
//	@Query("{active: true}")
//	List<UserNotification> findAll();
//
//	@Query("{user.$id: {$eq: ?0}, read: false, active: true}")
//	List<UserNotification> findUnread(final String userName);
//
//	@Query(count = true, value = "{user.$id: {$eq: ?0}, read: false, active: true}")
//	int findUnreadCount(final String userName);
//
//	@Query("{user.$id: {$eq: ?0}, active: true}")
//	List<UserNotification> findNotifications(final String userName);
//
//	@Query("{user.$id: {$eq: ?0}, active: true}")
//	List<UserNotification> findNotifications(final String userName, final int limit, final Sort sort);
}
