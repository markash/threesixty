package za.co.yellowfire.threesixty.domain.user;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification")
public class NotificationConfiguration {
	public static final int DEFAULT_NOTIFICATION_LIMIT = 10;
	
	//user.notification.limit=10
	private int limit = DEFAULT_NOTIFICATION_LIMIT;
	
	public int getLimit() { return limit; }
	public void setLimit(final int notificationLimit) { this.limit = notificationLimit; }
}
