package za.co.yellowfire.threesixty.config;

import com.github.markash.ui.security.CurrentUserProvider;
import com.github.markash.ui.security.SpringSecurityCurrentUserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.io.IOException;
import java.util.Optional;

//@Configuration
public class SecurityConfig /*implements AuthenticationManagerConfigurer*/ {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private GridFsClient client;

    //@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(
//                    final String userName) throws UsernameNotFoundException {
//
//                LOG.info("Looking for user {}", userName);
//
//                UserDetails user =
//                        findUser(userName)
//                        .orElseThrow(() -> new UsernameNotFoundException("Unable to find user " + userName));
//
//                LOG.info("Found user {}", user);
//
//                return user;
//            }
//
//            private Optional<User> findUser(final String id) {
//                Optional<User> user =  userRepository.findById(id);
//                if (user.isPresent()) {
//                    try {
//                        user.get().retrievePicture(client);
//                    } catch (IOException e) {
//                        LOG.warn("Unable to load the profile picture for user {}: {}", id, e.getMessage());
//                    }
//                }
//                return user;
//            }
//        });
//
//        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        //return new BCryptPasswordEncoder(11);
        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//    public CurrentUserProvider<User> currentUserProvider() {
//
//        return () -> new SpringSecurityCurrentUserProvider().get().map(o -> (User) o);
//    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(authenticationProvider());
//    }
//
//    @Bean
//    @PrototypeScope
//    public ChangePasswordHandler changePasswordHandler(
//            final UserService userService,
//            final ApplicationEventPublisher publisher) {
//
//        return new ChangePasswordHandler(userService, publisher);
//    }
//
//    @Bean
//    @PrototypeScope
//    public ChangePasswordForm changePasswordForm(final ChangePasswordHandler changePasswordHandler) {
//        return new ChangePasswordForm(changePasswordHandler);
//    }
//
//    @Bean
//    @PrototypeScope
//    ListDataProvider<User> activeUserListDataProvider(final UserRepository userRepository) {
//        List<User> list = userRepository.findByActive(true);
//        return new ListDataProvider<>(list);
//    }
}
