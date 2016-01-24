package za.co.yellowfire.threesixty.domain.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnClass(SendGrid.class)
//@ConditionalOnProperty(prefix = "mailing.provider", value = "sendgrid")
@EnableConfigurationProperties(MailingProperties.class)
public class MailingAutoConfiguration {

	@Autowired
	private MailingProperties properties;

	@Bean
	//@ConditionalOnMissingBean(SendGrid.class)
	public SendGridMailingService sendGridService() {
		
		return new SendGridMailingService(properties);
	}
}