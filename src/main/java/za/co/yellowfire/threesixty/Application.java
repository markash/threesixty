package za.co.yellowfire.threesixty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import za.co.yellowfire.threesixty.domain.kudos.BadgeProperties;
import za.co.yellowfire.threesixty.domain.mail.MailingProperties;

@SpringBootApplication
@EnableConfigurationProperties({MailingProperties.class, BadgeProperties.class})
public class Application {	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
