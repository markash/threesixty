package za.co.yellowfire.threesixty.domain.mail;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mailing")
public class MailingProperties {

	private String key;
	
	private String provider;
	
	public String getKey() { return this.key; }
	public String getProvider() { return this.provider; }
	
	public void setKey(final String key) { this.key = key; }
	public void setProvider(final String provider) { this.provider = provider; }
	
	public boolean isKeyConfigured() { return !StringUtils.isBlank(this.key); }
	public boolean isProviderConfigured() { return !StringUtils.isBlank(this.provider); }
}
