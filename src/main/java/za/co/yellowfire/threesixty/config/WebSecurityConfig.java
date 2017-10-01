package za.co.yellowfire.threesixty.config;

import com.microsoft.azure.autoconfigure.aad.AADAuthenticationFilter;
import com.vaadin.data.provider.ListDataProvider;
import io.threesixty.ui.security.SpringSecurityCurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.view.security.ChangePasswordForm;
import za.co.yellowfire.threesixty.ui.view.security.ChangePasswordHandler;

import java.util.List;

//@Configuration
//@EnableOAuth2Sso
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired
    private AADAuthenticationFilter aadAuthFilter;

    //@Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/**").authenticated();
//        http.authorizeRequests().antMatchers("/api/**").authenticated();

        http.logout().logoutSuccessUrl("/").permitAll();
        http.authorizeRequests().anyRequest().permitAll();
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.addFilterBefore(aadAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
//
//    @Bean
//    public SpringSecurityCurrentUserProvider<User> currentUserProvider() {
//        return new SpringSecurityCurrentUserProvider<User>();
//    }

//    @Bean
//    @PrototypeScope
//    public ChangePasswordHandler changePasswordHandler(final UserService userService, final EventBus.SessionEventBus eventBus) {
//        return new ChangePasswordHandler(userService, eventBus);
//    }
//
//    @Bean
//    @PrototypeScope
//    public ChangePasswordForm changePasswordForm(final ChangePasswordHandler changePasswordHandler) {
//        return new ChangePasswordForm(changePasswordHandler);
//    }

//    @Bean
//    @PrototypeScope
//    ListDataProvider<User> activeUserListDataProvider(final UserRepository userRepository) {
//        List<User> list = userRepository.findByActive(true);
//        return new ListDataProvider<>(list);
//    }
}
