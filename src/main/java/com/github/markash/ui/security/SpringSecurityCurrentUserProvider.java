package com.github.markash.ui.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@SuppressWarnings("unused")
public class SpringSecurityCurrentUserProvider implements CurrentUserProvider<Object> {

    @Override
    public Optional<Object> get() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return Optional.ofNullable(authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
