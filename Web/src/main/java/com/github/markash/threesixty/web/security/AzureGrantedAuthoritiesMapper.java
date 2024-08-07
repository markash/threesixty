package com.github.markash.threesixty.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class AzureGrantedAuthoritiesMapper implements GrantedAuthoritiesMapper {
    private static final Logger LOG = LoggerFactory.getLogger(AzureGrantedAuthoritiesMapper.class);

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {

        List<SimpleGrantedAuthority> grantedAuthorities = authorities
            .stream()
            .filter(authority -> authority.getAuthority().equalsIgnoreCase("OIDC_USER"))
            .map(authority -> {
                if (authority instanceof OidcUserAuthority userAuthority && userAuthority.getAttributes().containsKey("roles")) {
                    if (userAuthority.getAttributes().get("roles") instanceof List<?> roles) {
                        return roles.stream()
                                    .map(role -> new SimpleGrantedAuthority(role.toString()))
                                    .toList();
                    }
                }
                return new ArrayList<SimpleGrantedAuthority>();
            })
            .flatMap(Collection::stream)
            .toList();

        grantedAuthorities.forEach(x -> LOG.info("GrantedAuthority: {}", x));

        return grantedAuthorities;
    }
}
