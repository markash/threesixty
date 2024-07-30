package com.github.markash.threesixty.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class AzureGrantedAuthoritiesMapper implements GrantedAuthoritiesMapper {
    private static final Logger LOG = LoggerFactory.getLogger(AzureGrantedAuthoritiesMapper.class);

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {

        authorities.forEach(a -> LOG.info("Granted authority: {}", a));

        return authorities;
    }
}
