package za.co.yellowfire.threesixty.domain.kudos;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "threesixty.badge")
public class BadgeProperties {

	private String uri;

	public String getUri() { return uri; }
	public void setUri(String uri) { this.uri = uri; }
}
