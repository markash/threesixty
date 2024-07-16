package za.co.yellowfire.threesixty;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.CustomizedSystemMessages;
import com.vaadin.flow.server.SystemMessages;
import com.vaadin.flow.server.SystemMessagesInfo;
import com.vaadin.flow.server.SystemMessagesProvider;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.context.annotation.Bean;
import za.co.yellowfire.threesixty.domain.user.CountryRepository;

import javax.sql.DataSource;

//@EnableConfigurationProperties({MailingProperties.class, BadgeProperties.class})
@Theme(value = "my-vaadin-app")
@SpringBootApplication(/*exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class*/)
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Provide custom system messages to make sure the application is reloaded when the session expires.
     */
    @Bean
    SystemMessagesProvider systemMessagesProvider() {
        return new SystemMessagesProvider() {
            @Override
            public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
                CustomizedSystemMessages systemMessages = new CustomizedSystemMessages();
                systemMessages.setSessionExpiredNotificationEnabled(false);
                return systemMessages;
            }
        };
    }

    @Bean
    SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(
            DataSource dataSource,
            SqlInitializationProperties properties,
            CountryRepository repository) {

        // This bean ensures the database is only initialized when empty
        return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties) {
            @Override
            public boolean initializeDatabase() {
                if (repository.count() == 0L) {
                    return super.initializeDatabase();
                }
                return false;
            }
        };
    }

//    /**
//     * Configure the authentication manager.
//     */
//    @Configuration
//    static class AuthenticationConfiguration implements AuthenticationManagerConfigurer {
//
//        @Override
//        public void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.inMemoryAuthentication()
//                    .withUser("user").password("user").roles("USER")
//                    .and()
//                    .withUser("admin").password("password").roles("ADMIN");
//        }
//    }
}
