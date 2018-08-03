package za.co.yellowfire.threesixty.config;

import com.github.markash.ui.security.SpringSecurityCurrentUserProvider;
import com.vaadin.data.provider.ListDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.view.security.ChangePasswordForm;
import za.co.yellowfire.threesixty.ui.view.security.ChangePasswordHandler;

import java.util.List;

@Configuration
public class SecurityConfig implements AuthenticationManagerConfigurer {

    @Autowired
    private UserService userService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        //return new BCryptPasswordEncoder(11);
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SpringSecurityCurrentUserProvider<User> currentUserProvider() {
        return new SpringSecurityCurrentUserProvider<>();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
//        auth.inMemoryAuthentication()
//                .withUser("user").password("user").roles("USER")
//                .and()
//                .withUser("admin").password("password").roles("ADMIN");
    }

    @Bean
    @PrototypeScope
    public ChangePasswordHandler changePasswordHandler(final UserService userService, final EventBus.SessionEventBus eventBus) {
        return new ChangePasswordHandler(userService, eventBus);
    }

    @Bean
    @PrototypeScope
    public ChangePasswordForm changePasswordForm(final ChangePasswordHandler changePasswordHandler) {
        return new ChangePasswordForm(changePasswordHandler);
    }

    @Bean
    @PrototypeScope
    ListDataProvider<User> activeUserListDataProvider(final UserRepository userRepository) {
        List<User> list = userRepository.findByActive(true);
        return new ListDataProvider<>(list);
    }
}
