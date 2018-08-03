package za.co.yellowfire.threesixty.domain.user;

import com.github.markash.ui.security.DefaultUserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Principal extends DefaultUserPrincipal<User> {
    private final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    private Principal(final User user) {
        super(user);

        this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.isAdmin()) {
            this.authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
    }

    static Principal wrap(final User user) {
        return new Principal(user);
    }

    @Override
    public String getPassword() {
        return getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return getUser().getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(this.authorities);
    }
}
