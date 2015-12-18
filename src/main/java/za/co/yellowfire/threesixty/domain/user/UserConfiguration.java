package za.co.yellowfire.threesixty.domain.user;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import za.co.yellowfire.threesixty.domain.user.notification.NotificationConfiguration;

@Component
@ConfigurationProperties(prefix="user")
public class UserConfiguration {
	
	@NestedConfigurationProperty
	private NotificationConfiguration notification;

	public NotificationConfiguration getNotification() { return notification; }

	public void setNotification(NotificationConfiguration notification) { this.notification = notification; }
}
