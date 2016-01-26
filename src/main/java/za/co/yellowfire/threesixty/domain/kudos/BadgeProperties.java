package za.co.yellowfire.threesixty.domain.kudos;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "threesixty.badge")
public class BadgeProperties {

	private String uri;

	public String getUri() { return uri; }
	public void setUri(String uri) { this.uri = uri; }
}
