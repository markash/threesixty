package za.co.yellowfire.threesixty.domain.user;

import io.threesixty.ui.security.DefaultUserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal extends DefaultUserPrincipal<User> {
    private final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    private UserPrincipal(final User user) {
        super(user);

        this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.isAdmin()) {
            this.authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
    }

    static  io.threesixty.ui.security.UserPrincipal wrap(final User user) {
        return new UserPrincipal(user);
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
