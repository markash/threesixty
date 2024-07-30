package com.github.markash.threesixty.web.security;

import com.github.markash.threesixty.web.service.CurrentSession;
import com.github.markash.threesixty.web.service.UserInfo;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.hilla.BrowserCallable;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.time.ZoneId;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

@PermitAll
@BrowserCallable
public class CurrentUserProvider implements CurrentSession {
    private static final Logger LOG = LoggerFactory.getLogger(CurrentUserProvider.class);

    @Autowired
    private AuthenticationContext authenticationContext;

    public Optional<UserInfo> getCurrentUser() {
        return getOauth2User().map(user -> new UserInfo() {
            @Override
            public String getUsername() {
                return requireNonNullElse(user.getAttribute("preferred_username"), "");
            }

            @Override
            public String getFirstName() {
                return requireNonNullElse(user.getAttribute("given_name"), "");
            }

            @Override
            public String getLastName() {
                return requireNonNullElse(user.getAttribute("family_name"), "");
            }

            @Override
            public String getName() {
                return requireNonNull(user.getName());
            }
        });
    }

    private Optional<OidcUser> getOauth2User() {
        return authenticationContext.getAuthenticatedUser(OidcUser.class);
    }

    @Override
    public ZoneId getTimeZone() {
        return getOauth2User()
                .map(user -> user.<String>getAttribute("zoneinfo"))
                .map(ZoneId::of)
                .orElse(ZoneId.systemDefault());
    }

    @Override
    public boolean hasRole(String role) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(role));
        }
        return false;
    }
}
