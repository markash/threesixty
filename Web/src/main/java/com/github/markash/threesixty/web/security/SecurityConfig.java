package com.github.markash.threesixty.web.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * Reference <a href="https://vaadin.com/blog/a-minimal-openid-connect-oidc-secured-vaadin-flow-application-with-spring-boot">A minimal OpenID Connect (OIDC)-secured Vaadin Flow application with Spring Boot</a>
 */
@Configuration
@EnableWebSecurity
class SecurityConfig extends VaadinWebSecurity {

    private final OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler;

    public SecurityConfig(@Autowired ClientRegistrationRepository clientRegistrationRepository) {

        this.logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        this.logoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8080/unsecured"); /* or logged-out */
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // This is important to let Spring Security know to redirect to external login page.
        http.oauth2Login(Customizer.withDefaults());

        // Logout with oauth2 must be handled with Keycloak
        http.logout(c -> c.logoutSuccessHandler(logoutSuccessHandler));
        super.configure(http);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        // Don't apply security rules on our static pages
        // /back-channel-logout should only be accessible from certain hosts/IPs. In this case we assume this has
        // been taken care of in a firewall outside this application.
        web.ignoring().requestMatchers("/logged-out", "/session-expired", "/back-channel-logout");
    }
}
